package com.zerobase.yogizogi.accommodation.domain.model;

import com.querydsl.core.annotations.QueryProjection;
import com.zerobase.yogizogi.accommodation.domain.entity.Room;
import com.zerobase.yogizogi.accommodation.domain.entity.RoomPicture;
import java.util.Set;
import lombok.Getter;

@Getter
public class RoomDetailForm {

    @QueryProjection
    public RoomDetailForm(Room room, Integer minPrice) {
        this.id = room.getId();
        this.roomName = room.getName();
        this.checkInTime = room.getCheckInTime();
        this.checkOutTime = room.getCheekOutTime();
        this.defaultPeople = room.getDefaultPeople();
        this.maxPeople = room.getMaxPeople();
        this.pictureUrlList = room.getPictures();
        this.price = minPrice;
    }

    private final Long id;
    private final String roomName;
    private final String checkInTime;
    private final String checkOutTime;
    private final Integer defaultPeople;
    private final Integer maxPeople;
    private final Set<RoomPicture> pictureUrlList;
    private final Integer price;
}
