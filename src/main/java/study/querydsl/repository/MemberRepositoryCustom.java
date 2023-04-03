package study.querydsl.repository;

import org.springframework.stereotype.Repository;
import study.querydsl.dto.MemberDto;
import study.querydsl.dto.UserDto;

import java.util.List;

@Repository
public interface MemberRepositoryCustom {
  List<UserDto> findByNameAndAgeBetweenAge(String name, Integer age);
}
