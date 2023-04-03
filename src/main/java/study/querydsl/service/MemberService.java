package study.querydsl.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import study.querydsl.repository.MemberQueryRepository;
import study.querydsl.repository.MemberRepository;

@Service
@RequiredArgsConstructor
public class MemberService {

  // Case 1
  private final MemberRepository memberRepository;
  public void test1() {
    memberRepository.findByNameAndAgeBetweenAge("name", 10);
  }


  // Case 2
  private final MemberQueryRepository memberQueryRepository;
  public void test2() {
    memberQueryRepository.searchUserDto();
  }

}
