package study.querydsl.entity;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
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
  @Commit
  public void helloTest() {
    Hello hello1 = new Hello();
    Hello hello2 = new Hello();
    Hello hello3 = new Hello();
    em.persist(hello1);
    em.persist(hello2);
    em.persist(hello3);
    em.flush();

//    QHello q = new QHello("q");
    Hello result = queryFactory
        .selectFrom(hello)
        .where(hello.id.eq(1L))
        .fetchOne();

    System.out.println("result = " + result);
  }

  @Test
  @Commit
  public void test() {
    Info i1 = new Info("111", "TestShip1", "this is T1");
    Info i2 = new Info("112", "TestShip2", "this is T2");
    Info i3 = new Info("113", "TestShip3", "this is T3");
    Info i4 = new Info("114", "TestShip4", "this is T4");
    Info i5 = new Info("115", "TestShip5", "this is T5");

    em.persist(i1);
    em.persist(i2);
    em.persist(i3);
    em.persist(i4);
    em.persist(i5);
    em.flush();

    ChartsData c1_1 = new ChartsData(1, "TestShip1", 1000);
    ChartsData c1_2 = new ChartsData(2, "TestShip1", 500);
    ChartsData c1_3 = new ChartsData(3, "TestShip1", 100);
    ChartsData c1_4 = new ChartsData(4, "TestShip1", 3000);
    ChartsData c1_5 = new ChartsData(5, "TestShip1", 2000);

    em.persist(c1_1);
    em.persist(c1_2);
    em.persist(c1_3);
    em.persist(c1_4);
    em.persist(c1_5);
    em.flush();

    ChartsData c2_1 = new ChartsData(1, "TestShip2", 100);
    ChartsData c2_2 = new ChartsData(2, "TestShip2", 800);
    ChartsData c2_3 = new ChartsData(3, "TestShip2", 1500);
    ChartsData c2_4 = new ChartsData(4, "TestShip2", 1000);
    ChartsData c2_5 = new ChartsData(5, "TestShip2", 300);

    em.persist(c2_1);
    em.persist(c2_2);
    em.persist(c2_3);
    em.persist(c2_4);
    em.persist(c2_5);
    em.flush();

    ChartsData c3_1 = new ChartsData(1, "TestShip3", 1300);
    ChartsData c3_2 = new ChartsData(2, "TestShip3", 1500);
    ChartsData c3_3 = new ChartsData(3, "TestShip3", 500);
    ChartsData c3_4 = new ChartsData(4, "TestShip3", 300);
    ChartsData c3_5 = new ChartsData(5, "TestShip3", 1000);

    em.persist(c3_1);
    em.persist(c3_2);
    em.persist(c3_3);
    em.persist(c3_4);
    em.persist(c3_5);
    em.flush();

    ChartsData c4_1 = new ChartsData(1, "TestShip4", 200);
    ChartsData c4_2 = new ChartsData(2, "TestShip4", 1800);
    ChartsData c4_3 = new ChartsData(3, "TestShip4", 1200);
    ChartsData c4_4 = new ChartsData(4, "TestShip4", 1900);
    ChartsData c4_5 = new ChartsData(5, "TestShip4", 700);

    em.persist(c4_1);
    em.persist(c4_2);
    em.persist(c4_3);
    em.persist(c4_4);
    em.persist(c4_5);
    em.flush();

    ChartsData c5_1 = new ChartsData(1, "TestShip5", 600);
    ChartsData c5_2 = new ChartsData(2, "TestShip5", 200);
    ChartsData c5_3 = new ChartsData(3, "TestShip5", 1300);
    ChartsData c5_4 = new ChartsData(4, "TestShip5", 1500);
    ChartsData c5_5 = new ChartsData(5, "TestShip5", 3000);

    em.persist(c5_1);
    em.persist(c5_2);
    em.persist(c5_3);
    em.persist(c5_4);
    em.persist(c5_5);
    em.flush();
  }
}