package com.zerobase.yogizogi.accommodation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
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
}
