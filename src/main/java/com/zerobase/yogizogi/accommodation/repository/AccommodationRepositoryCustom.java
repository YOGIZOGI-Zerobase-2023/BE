package com.zerobase.yogizogi.accommodation.repository;

import com.zerobase.yogizogi.accommodation.domain.model.RoomDetailForm;
import com.zerobase.yogizogi.accommodation.domain.entity.Accommodation;
import com.zerobase.yogizogi.accommodation.dto.AccommodationSearchDto;
import java.time.LocalDate;
import java.util.List;

public interface AccommodationRepositoryCustom {

    List<AccommodationSearchDto> findBySearchOption(String keyword, LocalDate checkInDate,
        LocalDate checkOutDate, Integer people, String sort, String direction, Integer minPrice,
        Integer maxPrice,
        Integer category, Double lat, Double lon);

    List<RoomDetailForm> findRoomDetailByIdAndDateAndPeople(Long accommodationId,
        LocalDate checkInDate,
        LocalDate checkOutDate, Integer people);

//    Page<Accommodation> findBySearchOption(Pageable pageable, String keyword, LocalDate checkInDate,
//        LocalDate checkOutDate, Integer people, String sort, String direction, Integer minPrice,
//        Integer maxPrice,
//        Integer category, Double lat, Double lon);
    List<Accommodation> findInArea(double leftUpLat, double rightDownLat, double leftUpLon, double rightDownLon);
}
