package study.querydsl.entity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

import static study.querydsl.entity.QMember.member;

@SpringBootTest
@Transactional
class MemberTest {
  @Autowired
  EntityManager em;

  JPAQueryFactory queryFactory;

  @BeforeEach
  public void before() {
    queryFactory = new JPAQueryFactory(em);
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
}