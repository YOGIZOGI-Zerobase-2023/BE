package com.zerobase.yogizogi.review.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor //for test
public class ReviewForm {

    private Long bookId;
    private Integer rate;
    private String description;

}
