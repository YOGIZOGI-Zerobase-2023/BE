package com.zerobase.yogizogi.accommodation.dto;

import com.zerobase.yogizogi.accommodation.domain.entity.Accommodation;
import com.zerobase.yogizogi.accommodation.domain.entity.Price;
import com.zerobase.yogizogi.global.exception.CustomException;
import com.zerobase.yogizogi.global.exception.ErrorCode;
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
        return AccommodationDto.builder()
            .id(accommodation.getId())
            .accommodationName(accommodation.getName())
            .category(accommodation.getCategory())
            .address(accommodation.getAddress())
            .lat(accommodation.getLat())
            .lon(accommodation.getLon())
            .picUrl(accommodation.getPicUrl())
            .rate(accommodation.getRate())
            .price(accommodation.getRooms().stream().findFirst()
                .map(room -> room.getPrices().stream().findFirst()
                    .map(Price::getPrice)
                    .orElse(0)).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ROOM)))
                .build();
    }
}
