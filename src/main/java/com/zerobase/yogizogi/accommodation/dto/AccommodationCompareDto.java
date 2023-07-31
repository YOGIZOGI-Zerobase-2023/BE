package com.zerobase.yogizogi.accommodation.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.zerobase.yogizogi.accommodation.domain.entity.Accommodation;
import java.util.ArrayList;
import lombok.Getter;

@Getter
public class AccommodationCompareDto {

    @QueryProjection
    public AccommodationCompareDto(Accommodation accommodation, Integer price) {
        ArrayList<String> facility = new ArrayList<>();
        accommodation.getConveniences().forEach(x -> facility.add(x.getFacility()));

        String conveniences = facility.toString().replace("[", "").replace("]", "");

        this.id = accommodation.getId();
        this.accommodationName = accommodation.getName();
        this.rate = accommodation.getRate();
        this.address = accommodation.getAddress();
        this.convenience = conveniences;
        this.picUrl = accommodation.getPicUrl();
        this.price = price;
    }

    private final Long id;
    private final String accommodationName;
    private final Double rate;
    private final String address;
    private final String convenience;
    private final String picUrl;
    private final Integer price;

}
