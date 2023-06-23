package com.zerobase.yogizogi.review.dto;

import com.zerobase.yogizogi.review.domain.entity.Review;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {
    private Long id;
    private Long userId;
    private Long accommodationId;
    private Integer rate;
    private String description;
    public ReviewDto(Review review) {
        this.id = review.getId();
        this.userId = review.getUser().getId();
        this.accommodationId = review.getAccommodation().getId();
        this.rate = review.getRate();
        this.description = review.getContents();
    }

}
