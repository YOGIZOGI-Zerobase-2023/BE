package com.zerobase.yogizogi.accommodation.dto;

import com.zerobase.yogizogi.accommodation.domain.entity.Accommodation;
import com.zerobase.yogizogi.accommodation.domain.entity.Price;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class AccommodationDto {

    private Long id;
    private String accommodationName;
    private int category;
    private Double rate;
    private String picUrl;
    private String address;
    private Double lat;
    private Double lon;
    private Integer price;

    public static AccommodationDto from(Accommodation accommodation) {
        int minPrice = accommodation.getRooms().stream()
            .filter(room -> !room.getPrices().isEmpty())
            .flatMap(room -> room.getPrices().stream())
            .map(Price::getPrice)
            .filter(Objects::nonNull)
            .min(Integer::compare).orElse(40000);
//날짜값이 추가가 된다면, 성능이 개선될 것으로 생각되어짐.
        return AccommodationDto.builder()
            .id(accommodation.getId())
            .accommodationName(accommodation.getName())
            .category(accommodation.getCategory())
            .address(accommodation.getAddress())
            .lat(accommodation.getLat())
            .lon(accommodation.getLon())
            .picUrl(accommodation.getPicUrl())
            .rate(accommodation.getRate())
            .price(minPrice)
            .build();
    }
}
