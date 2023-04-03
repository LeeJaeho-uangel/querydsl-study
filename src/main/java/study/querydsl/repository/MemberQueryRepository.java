package study.querydsl.repository;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import study.querydsl.dto.QUserDto;
import study.querydsl.dto.UserDto;
import study.querydsl.entity.Member;

import java.util.List;

import static study.querydsl.entity.QMember.member;

@Repository
@RequiredArgsConstructor
public class MemberQueryRepository {
  private final JPAQueryFactory queryFactory;

  public List<UserDto> searchUserDto() {
    return queryFactory
        .select(new QUserDto(member.username, member.age))
        .from(member)
        .fetch();
  }

  public Page<UserDto> pageTest(Pageable pageable) {
    List<UserDto> content = queryFactory
        .select(new QUserDto(member.username, member.age))
        .from(member)
        .offset(0)
        .limit(10)
        .fetch();

    Long count = queryFactory
        .select(member.count())
        .from(member)
        .fetchFirst();

//    return new PageImpl<>(content, pageable, count);
    return PageableExecutionUtils.getPage(content, pageable, () -> count);
  }
}
