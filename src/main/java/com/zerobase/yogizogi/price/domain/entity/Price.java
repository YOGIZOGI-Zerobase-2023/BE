package com.zerobase.yogizogi.price.domain.entity;

import com.zerobase.yogizogi.book.domain.entity.Book;
import com.zerobase.yogizogi.room.domain.entity.Room;
import java.time.LocalDate;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "price")
public class Price {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "priceId")
  private Long id;
  @Column(name = "price")
  private Integer price;
  @Column(name = "date")
  private LocalDate date;
  @Column(name = "roomCnt")
  private int roomCnt;
  @ManyToOne
  @JoinColumn(name = "roomId")
  private Room room;

  @ManyToOne
  @JoinColumn(name = "bookId")
  private Book book;
}
