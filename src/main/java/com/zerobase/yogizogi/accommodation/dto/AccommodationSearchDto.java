package com.zerobase.yogizogi.accommodation.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.zerobase.yogizogi.accommodation.domain.entity.Accommodation;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class AccommodationSearchDto {

    @QueryProjection
    public AccommodationSearchDto(Accommodation accommodation, Integer price, Integer peopleMax) {
        this.id = accommodation.getId();
        this.name = accommodation.getName();
        this.category = accommodation.getCategory();
        this.score = accommodation.getScore();
        this.picUrl = accommodation.getPicUrl();
        this.address = accommodation.getAddress();
        this.lat = accommodation.getLat();
        this.lon = accommodation.getLng();
        this.price = price;
        this.peopleMax = peopleMax;
    }

    private Long id;
    private String name;
    private int category;
    private Double score;
    private String picUrl;
    private String address;
    private Double lat;
    private Double lon;
    private Integer price;
    private Integer peopleMax;

}
