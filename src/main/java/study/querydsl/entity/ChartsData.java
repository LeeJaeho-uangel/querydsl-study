package study.querydsl.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@ToString
@Table(name = "charts_data")
@NoArgsConstructor
public class ChartsData {
  @Id
  @GeneratedValue
  private long id;
  private int sq;
  private String name;
  private int val;

  public ChartsData(int sq, String name, int val) {
    this.sq = sq;
    this.name = name;
    this.val = val;
  }
}
