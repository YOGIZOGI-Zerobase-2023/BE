package com.zerobase.yogizogi.accommodation.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.zerobase.yogizogi.accommodation.domain.entity.Accommodation;
import lombok.Getter;

@Getter
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

    private final Long id;
    private final String accommodationName;
    private final String picUrl;
    private final String address;
    private final Double rate;
    private final Integer price;
    private final Double lat;
    private final Double lon;
    private final int category;
    private final Integer peopleMax;
    private final String info;

}
