package com.zerobase.yogizogi.review.domain.entity;

import com.zerobase.yogizogi.book.domain.entity.Book;
import com.zerobase.yogizogi.global.entity.BaseEntity;
import com.zerobase.yogizogi.user.domain.entity.AppUser;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
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
    @OneToOne
    private Book book;

    @ManyToOne
    private AppUser user;

    //@OneToMany(평점 반영을 위한 값)
    //private Accommodation accommodation;
    private int rate;

    private String contents;
}
