package com.zerobase.yogizogi.accommodation.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.zerobase.yogizogi.accommodation.domain.entity.Accommodation;
import com.zerobase.yogizogi.accommodation.domain.entity.Picture;
import com.zerobase.yogizogi.accommodation.domain.model.RoomDetailForm;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
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
        this.convenienceList = accommodation.getConveniences().stream().map(ConvenienceDto::from)
            .collect(Collectors.toList());
    }

    private final Long id;
    private final String accommodationName;
    private final int category;
    private final Double rate;
    private final String address;
    private final String region;
    private final Double lat;
    private final Double lon;
    private final String info;
    private final Set<Picture> picUrlList;
    private final List<ConvenienceDto> convenienceList;
    private final List<RoomDetailForm> rooms;

}
