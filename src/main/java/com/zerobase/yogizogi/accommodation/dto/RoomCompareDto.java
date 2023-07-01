package com.zerobase.yogizogi.accommodation.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.zerobase.yogizogi.accommodation.domain.entity.Accommodation;
import com.zerobase.yogizogi.accommodation.domain.entity.Room;
import lombok.Getter;

@Getter
public class RoomCompareDto {


    @QueryProjection
    public RoomCompareDto(Room room, Integer price) {
        Accommodation accommodation = room.getAccommodation();

        this.id = room.getId();
        this.accommodationName = accommodation.getName();
        this.rate = accommodation.getRate();
        this.address = accommodation.getAddress();
        this.roomName = room.getName();
        this.convenience = room.getConveniences();
        // TODO
        // 사진 없을 시 null 처리 필요
        this.picUrl = room.getPictures().stream().findFirst().get().getUrl();
        this.price = price;
    }

    private final Long id;
    private final String accommodationName;
    private final Double rate;
    private final String address;
    private final String roomName;
    private final String convenience;
    private final String picUrl;
    private final Integer price;

}
