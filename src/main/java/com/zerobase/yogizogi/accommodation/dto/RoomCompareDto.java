package com.zerobase.yogizogi.accommodation.dto;

import com.zerobase.yogizogi.accommodation.domain.entity.Accommodation;
import com.zerobase.yogizogi.accommodation.domain.entity.Price;
import com.zerobase.yogizogi.accommodation.domain.entity.Room;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RoomCompareDto {

    private Long id;
    private String accommodationName;
    private Double rate;
    private String address;
    private String roomName;
    private String convenience;
    private String picUrl;
    private Set<Price> prices;

    public static RoomCompareDto from(Room room) {
        Accommodation accommodation = room.getAccommodation();

//날짜값이 추가가 된다면, 성능이 개선될 것으로 생각되어짐.
        return RoomCompareDto.builder()
            .id(accommodation.getId())
            .accommodationName(accommodation.getName())
            .rate(accommodation.getRate())
            .address(accommodation.getAddress())
            .roomName(room.getName())
            .convenience(room.getConveniences())
            .picUrl(String.valueOf(room.getPictures().stream().findFirst().orElse(null)))
            .prices(room.getPrices())
            .build();
    }
}
