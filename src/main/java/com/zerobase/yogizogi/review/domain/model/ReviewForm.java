package com.zerobase.yogizogi.review.domain.model;

import lombok.Getter;

@Getter
public class ReviewForm {

    private Long bookId;
    private Integer rate;
    private String description;

}
