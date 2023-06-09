package com.zerobase.yogizogi.review.domain.entity;

import com.zerobase.yogizogi.global.entity.BaseEntity;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int rate;
    private String contents;
    //외래키
    //bookId 저장할 필요가 없습니다.
    private Long userId;//삭제에 필요합니다.
    private Long accommodationId;
    //@OneToMany(평점 반영을 위한 값)<- 해당 방식도 고민이 좀 더 필요해 보임.
    //private Accommodation accommodation;

}
