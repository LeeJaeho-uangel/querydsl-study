package study.querydsl.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Info {
  @Id
  @GeneratedValue
  private long id;
  private String shipNumber;
  private String shipName;
  private String desc;

  public Info(String shipNumber, String shipName, String desc) {
    this.shipName = shipName;
    this.shipNumber = shipNumber;
    this.desc = desc;
  }
}
