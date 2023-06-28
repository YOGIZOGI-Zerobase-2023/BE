package com.zerobase.yogizogi.accommodation.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.zerobase.yogizogi.accommodation.domain.entity.Accommodation;
import com.zerobase.yogizogi.accommodation.domain.entity.Picture;
import com.zerobase.yogizogi.accommodation.domain.model.RoomDetailForm;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class AccommodationDetailDto {

    @QueryProjection
    public AccommodationDetailDto(Accommodation accommodation,
        List<RoomDetailForm> roomDetailForm) {
        this.id = accommodation.getId();
        this.accommodationName = accommodation.getName();
        this.category = accommodation.getCategory();
        this.rate = accommodation.getRate();
        this.address = accommodation.getAddress();
        this.region = accommodation.getRegion();
        this.lat = accommodation.getLat();
        this.lon = accommodation.getLon();
        this.info = accommodation.getDetail();
        this.picUrlList = accommodation.getPicUrls();
        this.rooms = roomDetailForm;
        this.convenienceList = accommodation.getConveniences().stream()
            .map(ConvenienceDto::from).collect(Collectors.toList());
    }

    private Long id;
    private String accommodationName;
    private int category;
    private Double rate;
    private String address;
    private String region;
    private Double lat;
    private Double lon;
    private String info;
    private Set<Picture> picUrlList;
    private List<ConvenienceDto> convenienceList;
    private List<RoomDetailForm> rooms;

}
