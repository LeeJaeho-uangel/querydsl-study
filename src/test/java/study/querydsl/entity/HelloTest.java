package study.querydsl.entity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

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
    Hello hello = new Hello();
    em.persist(hello);
    em.flush();

    QHello q = new QHello("q");
    Hello result = queryFactory
        .selectFrom(q)
        .fetchOne();

    System.out.println("result = " + result);
  }
}