package com.zerobase.yogizogi.book.domain.entity;

import com.zerobase.yogizogi.global.entity.BaseEntity;
import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Builder
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class Book extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private String bookName;
    private int people;
    private int payAmount;
    private boolean reviewRegistered;
    //외래키
    private Long userId;
    private Long roomId;
    private Long accommodationId;

    //private boolean accept; 락을 걸고 예약을 홀드하는 기능 스케쥴러 활용 동시 예약을 막을 수 있는 기능
    //예약을 확정한다는 내용을 줄려면 결제에 관련된 내용이 있고 해당 결제가 완료되어야 해당 형태의 기능이 작성 가능.
}
