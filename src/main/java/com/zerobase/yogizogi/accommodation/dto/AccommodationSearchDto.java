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
        this.accommodationName = accommodation.getName();
        this.category = accommodation.getCategory();
        this.rate = accommodation.getRate();
        this.picUrl = accommodation.getPicUrl();
        this.address = accommodation.getAddress();
        this.lat = accommodation.getLat();
        this.lon = accommodation.getLon();
        this.price = price;
        this.peopleMax = peopleMax;
        this.info = accommodation.getDetail();
    }

    private Long id;
    private String accommodationName;
    private String picUrl;
    private String address;
    private Double rate;
    private Integer price;
    private Double lat;
    private Double lon;
    private int category;
    private Integer peopleMax;
    private String info;

}
