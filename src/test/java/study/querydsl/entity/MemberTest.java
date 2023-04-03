package study.querydsl.entity;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import study.querydsl.dto.MemberDto;
import study.querydsl.dto.QMemberDto;
import study.querydsl.dto.UserDto;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.Objects;

import static com.querydsl.jpa.JPAExpressions.*;
import static study.querydsl.entity.QMember.member;
import static study.querydsl.entity.QTeam.team;

@SpringBootTest
@Transactional
class MemberTest {
  @Autowired
  EntityManager em;

  JPAQueryFactory queryFactory;

  @BeforeEach
  public void before() {
    queryFactory = new JPAQueryFactory(em);

    Team teamA = new Team("TeamA");
    Team teamB = new Team("TeamB");
    em.persist(teamA);
    em.persist(teamB);

    Member member1 = new Member("Member1", 10, teamA);
    Member member2 = new Member("Member2", 20, teamA);

    Member member3 = new Member("Member3", 30, teamB);
    Member member4 = new Member("Member4", 40, teamB);

    em.persist(member1);
    em.persist(member2);
    em.persist(member3);
    em.persist(member4);

    em.flush();
    em.clear();
  }

  @Test
  public void testEntity() {
    Team teamA = new Team("TeamA");
    Team teamB = new Team("TeamB");
    em.persist(teamA);
    em.persist(teamB);

    Member member1 = new Member("Member1", 10, teamA);
    Member member2 = new Member("Member2", 20, teamA);

    Member member3 = new Member("Member3", 30, teamB);
    Member member4 = new Member("Member4", 40, teamB);

    em.persist(member1);
    em.persist(member2);
    em.persist(member3);
    em.persist(member4);

    em.flush();
    em.clear();

//    List<Member> members = em.createQuery("SELECT m FROM Member m", Member.class).getResultList();

    List<Member> membersQ = queryFactory
        .selectFrom(member)
        .fetch();

    for (Member member : membersQ) {
      System.out.println("member = " + member);
      System.out.println("member > Team = " + member.getTeam());
    }
  }

  @Test
  public void testSearch() {
    Member fetch = queryFactory
        .selectFrom(member)
        .where(member.username.eq("Member2"))
        .fetchOne();

    System.out.println("fetch = " + fetch);
  }

  @Test
  public void testSort() {
    em.persist(new Member(null, 120));
    em.persist(new Member(null, 150));
    em.persist(new Member("Member5", 120));
    em.flush();

    List<Member> memberList = queryFactory
        .selectFrom(member)
        .orderBy(member.age.desc(), member.username.asc().nullsLast())
        .fetch();

    for (Member member : memberList) {
      System.out.println("member = " + member);
    }
  }

  @Test
  public void testPaging1() {
    List<Member> memberList = queryFactory
        .selectFrom(member)
        .orderBy(member.id.desc())
        .offset(0)
        .limit(10)
        .fetch();

    for (Member member : memberList) {
      System.out.println("member = " + member);
    }
  }

  @Test
  public void testAggregation() {
    List<Tuple> list = queryFactory
        .select(
            member.count(),
            member.age.max(),
            member.age.min(),
            member.age.avg(),
            member.age.sum()
        )
        .from(member)
        .fetch();

    for (Tuple tuple : list) {
      System.out.println("tuple.get(member.count()) = " + tuple.get(member.count()));
      System.out.println("tuple.get(member.age.max()) = " + tuple.get(member.age.max()));
      System.out.println("tuple.get(member.age.min()) = " + tuple.get(member.age.min()));
      System.out.println("tuple.get(member.age.avg()) = " + tuple.get(member.age.avg()));
      System.out.println("tuple.get(member.age.sum()) = " + tuple.get(member.age.sum()));
    }
  }

  @Test
  public void testGroup() {
    List<Tuple> list = queryFactory
        .select(team.name, member.age.avg())
        .from(member)
        .join(member.team, team)
        .groupBy(team.name)
        .fetch();

    for (Tuple tuple : list) {
      System.out.println("tuple.get(team.name) = " + tuple.get(team.name));
      System.out.println("tuple.get(member.age.avg()) = " + tuple.get(member.age.avg()));
    }
  }

  @Test
  public void testJoin1() {
    List<Member> teamA = queryFactory
        .selectFrom(member)
        .join(member.team, team)
        .where(team.name.equalsIgnoreCase("teamA"))
        .fetch();

    for (Member member : teamA) {
      System.out.println("member = " + member);
    }
  }
  
  @Test
  public void testThetaJoin() {
    em.persist(new Member("TeamA"));
    em.persist(new Member("TeamB"));

    List<Member> list = queryFactory
        .select(member)
        .from(member.team, team)
        .where(member.username.equalsIgnoreCase(team.name))
        .fetch();

    for (Member member : list) {
      System.out.println("member = " + member);
    }
  }
  
  @Test
  public void testJoinFiltering() {
    List<Tuple> memberList = queryFactory
        .select(member, team)
        .from(member)
        .leftJoin(member.team, team).on(team.name.equalsIgnoreCase("teamA"))
        .fetch();

    for (Tuple member1 : memberList) {
      System.out.println("member1 = " + member1);
    }
  }

  @Test
  public void testSubQuery() {
    QMember memberSub = new QMember("memberSub");

    List<Member> members = queryFactory
        .selectFrom(member)
        .where(member.age.eq(
            select(memberSub.age.max())
                .from(memberSub)
        ))
        .fetch();

    for (Member member1 : members) {
      System.out.println("member1 = " + member1);
    }
  }

  @Test
  public void testSelectSubQuery() {
    QMember memberSub = new QMember("memberSub");

    List<Tuple> members = queryFactory
        .select(member.username,
            select(memberSub.age.max().as("maxAge"))
                .from(memberSub))
        .from(member)
        .fetch();

    for (Tuple member1 : members) {
      System.out.println("member1 = " + member1);
    }
  }

  @Test
  public void testCase() {
    List<Tuple> tupleList = queryFactory
        .select(member.username,
            new CaseBuilder()
                .when(member.age.eq(10)).then("1011")
                .when(member.age.eq(20)).then("2022")
                .otherwise("3040")
        )
        .from(member)
        .fetch();

    for (Tuple tuple : tupleList) {
      System.out.println("tuple = " + tuple);
    }
  }
  
  @Test
  public void testConstant() {
    List<Tuple> tupleList = queryFactory
        .select(member.username,
            Expressions.asString("123"),
            Expressions.constant("zz")
        )
        .from(member)
        .fetch();

    for (Tuple tuple : tupleList) {
      System.out.println("tuple = " + tuple);
    }
  }

  @Test
  public void testCancat() {
    List<String> stringList = queryFactory
        .select(member.username.concat("_").concat(member.age.stringValue()))
        .from(member)
        .fetch();

    for (String s : stringList) {
      System.out.println("s = " + s);
    }
  }

  @Test
  public void testDtoSetter() {
    List<MemberDto> memberDtoList = queryFactory
        .select(Projections.bean(MemberDto.class,
            member.username,
            member.age))
        .from(member)
        .fetch();

    for (MemberDto memberDto : memberDtoList) {
      System.out.println("memberDto = " + memberDto);
    }
  }
  
  @Test
  public void testDtoField() {
    List<MemberDto> memberDtoList = queryFactory
        .select(Projections.fields(MemberDto.class,
            member.username,
            member.age))
        .from(member)
        .fetch();

    for (MemberDto memberDto : memberDtoList) {
      System.out.println("memberDto = " + memberDto);
    }
  }

  @Test
  public void testDtoConstructor() {
    List<MemberDto> memberDtoList = queryFactory
        .select(Projections.constructor(MemberDto.class,
            member.username,
            member.age))
        .from(member)
        .fetch();

    for (MemberDto memberDto : memberDtoList) {
      System.out.println("memberDto = " + memberDto);
    }
  }

  @Test
  public void testUserDto() {
    List<UserDto> memberDtoList = queryFactory
        .select(Projections.fields(UserDto.class,
            member.username.as("name"),
            member.age))
        .from(member)
        .fetch();

    for (UserDto memberDto : memberDtoList) {
      System.out.println("memberDto = " + memberDto);
    }
  }
  
  @Test
  public void testUserDto2() {
    QMember memberSub = new QMember("memberSub");

    List<UserDto> memberDtoList = queryFactory
        .select(Projections.fields(UserDto.class,
            member.username.as("name"),
            ExpressionUtils.as(JPAExpressions
                .select(memberSub.age.sum())
                .from(memberSub), "age")))
        .from(member)
        .fetch();

    for (UserDto memberDto : memberDtoList) {
      System.out.println("memberDto = " + memberDto);
    }
  }

  @Test
  public void testQueryProjection() {
    List<MemberDto> memberDtoList = queryFactory
        .select(new QMemberDto(member.username, member.age))
        .from(member)
        .fetch();

    for (MemberDto memberDto : memberDtoList) {
      System.out.println("memberDto = " + memberDto);
    }
  }

  @Test
  public void testBooleanBuilder() {
    String strParam = "member1";
    Integer intParam = 10;

    List<MemberDto> memberList = searchMember1(strParam, intParam);

    for (MemberDto memberDto : memberList) {
      System.out.println("memberDto = " + memberDto);
    }
  }

  private List<MemberDto> searchMember1(String strParam, Integer intParam) {
    BooleanBuilder builder = new BooleanBuilder();

    if (!Objects.equals(strParam, null))
      builder.and(member.username.equalsIgnoreCase(strParam));

    if (!Objects.equals(intParam, null))
      builder.and(member.age.eq(intParam));

    return queryFactory
        .select(new QMemberDto(member.username, member.age))
        .from(member)
        .where(builder)
        .fetch();
  }

  @Test
  public void testDynamicWhere() {
//    String strParam = "member1";
    String strParam = null;
//    Integer intParam = 10;
    Integer intParam = null;

    List<MemberDto> memberList = searchMember2(strParam, intParam);

    for (MemberDto memberDto : memberList) {
      System.out.println("memberDto = " + memberDto);
    }
  }

  private List<MemberDto> searchMember2(String strParam, Integer intParam) {
    return queryFactory
        .select(Projections.constructor(MemberDto.class,
            member.username,
            member.age))
        .from(member)
        .where(usernameEq(strParam), ageEq(intParam))
        .fetch();
  }
  private BooleanExpression usernameEq(String strParam) {
    return StringUtils.hasText(strParam) ? member.username.equalsIgnoreCase(strParam) : null;
  }

  private BooleanExpression ageEq(Integer intParam) {
    return !Objects.equals(intParam, null) ? member.age.eq(intParam) : null;
  }



}