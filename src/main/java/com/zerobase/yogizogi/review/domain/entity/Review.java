package com.zerobase.yogizogi.review.domain.entity;

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
    private int rate;
    private String contents;
    //외래키
    //bookId 저장할 필요가 없습니다.
    @OneToOne
    private AppUser user;//삭제에 필요합니다.
    @ManyToOne
    @JoinColumn(name = "accommodationId")
    private Accommodation accommodation;

}
