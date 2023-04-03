package study.querydsl.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import study.querydsl.dto.QUserDto;
import study.querydsl.dto.UserDto;

import java.util.List;

import static study.querydsl.entity.QMember.member;

@RequiredArgsConstructor
public class MemberRepositoryImpl implements MemberRepositoryCustom {

  private final JPAQueryFactory queryFactory;

  @Override
  public List<UserDto> findByNameAndAgeBetweenAge(String name, Integer age) {
    return queryFactory.
        select(new QUserDto(member.username, member.age))
        .from(member)
        .fetch();
  }

}
