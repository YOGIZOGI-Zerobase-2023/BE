package com.zerobase.yogizogi.review.dto;

import com.zerobase.yogizogi.review.domain.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Builder
@AllArgsConstructor
public class ReviewDto {
    private Long id;
    private String nickName;
    private Long accommodationId;
    private Integer rate;
    private String description;
    public static ReviewDto from(Review review) {
       return ReviewDto.builder()
            .id(review.getId())
            .nickName(review.getUser().getNickName())
            .accommodationId(review.getAccommodation().getId())
            .rate(review.getRate())
            .description(review.getDescription())
            .build();
    }

}
