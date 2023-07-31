package com.zerobase.yogizogi.accommodation.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.zerobase.yogizogi.accommodation.domain.entity.Accommodation;
import com.zerobase.yogizogi.accommodation.domain.entity.Room;
import com.zerobase.yogizogi.accommodation.domain.entity.RoomPicture;
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
        this.picUrl = room.getPictures().stream().filter(picture -> picture.getRoom() != null)
            .findFirst().map(RoomPicture::getUrl).orElse("");
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
