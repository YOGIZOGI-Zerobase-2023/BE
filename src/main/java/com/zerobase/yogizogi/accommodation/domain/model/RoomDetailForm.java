package com.zerobase.yogizogi.accommodation.domain.model;

import com.querydsl.core.annotations.QueryProjection;
import com.zerobase.yogizogi.accommodation.domain.entity.Room;
import com.zerobase.yogizogi.accommodation.domain.entity.RoomPicture;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
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

    private Long id;
    private String roomName;
    private String checkInTime;
    private String checkOutTime;
    private Integer defaultPeople;
    private Integer maxPeople;
    private Set<RoomPicture> pictureUrlList;
    private Integer price;
}
