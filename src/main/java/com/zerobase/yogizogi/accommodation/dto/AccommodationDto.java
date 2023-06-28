package com.zerobase.yogizogi.accommodation.dto;

import com.zerobase.yogizogi.accommodation.domain.entity.Accommodation;
import com.zerobase.yogizogi.global.exception.CustomException;
import com.zerobase.yogizogi.global.exception.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccommodationDto {

    private Long id;
    private String name;
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
            .name(accommodation.getName())
            .category(accommodation.getCategory())
            .address(accommodation.getAddress())
            .rate(accommodation.getRate())
            .picUrl(accommodation.getPicUrl())
            .lon(accommodation.getLon())
            .lat(accommodation.getLat())
            .price(
                accommodation.getRooms().stream().findFirst()
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ROOM)).getPrices()
                    .stream().findFirst()
                    .orElseThrow(() -> new CustomException(ErrorCode.NOT_ALLOW_ACCESS)).getPrice())
            .build();
    }
}
