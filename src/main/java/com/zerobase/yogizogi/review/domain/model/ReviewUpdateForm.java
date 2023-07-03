package com.zerobase.yogizogi.review.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor //for test
public class ReviewUpdateForm {
    private Integer rate;
    private String description;

}
