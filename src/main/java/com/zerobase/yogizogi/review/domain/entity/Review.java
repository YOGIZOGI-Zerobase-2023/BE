package com.zerobase.yogizogi.review.domain.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.zerobase.yogizogi.accommodation.domain.entity.Accommodation;
import com.zerobase.yogizogi.global.entity.BaseEntity;
import com.zerobase.yogizogi.user.domain.entity.AppUser;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
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
    private Integer score;
    private String contents;
    //외래키
    //bookId 저장할 필요가 없습니다.
    @OneToOne
    @JoinColumn(name = "appUser_id")
    private AppUser user;//삭제에 필요합니다.
    @ManyToOne//QnA
    @JoinColumn(name = "accommodation_id")//정말 사소한 문제다... accommodationId로 하면 안 되고, _id로 하면 된다.
    @JsonBackReference
    private Accommodation accommodation;

}
