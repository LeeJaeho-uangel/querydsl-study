package study.querydsl.entity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static study.querydsl.entity.QHello.hello;

@SpringBootTest
@Transactional
class HelloTest {

  @Autowired
  EntityManager em;

  JPAQueryFactory queryFactory;

  @BeforeEach
  public void before() {
    queryFactory = new JPAQueryFactory(em);
  }

  @Test
  public void helloTest() {
    Hello hello1 = new Hello();
    em.persist(hello1);
    em.flush();

//    QHello q = new QHello("q");
    Hello result = queryFactory
        .selectFrom(hello)
        .fetchOne();

    System.out.println("result = " + result);
  }
}