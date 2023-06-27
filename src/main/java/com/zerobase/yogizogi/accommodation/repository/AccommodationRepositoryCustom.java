package com.zerobase.yogizogi.accommodation.repository;

import com.zerobase.yogizogi.accommodation.domain.entity.Accommodation;
import com.zerobase.yogizogi.accommodation.dto.AccommodationDto;
import com.zerobase.yogizogi.accommodation.dto.AccommodationSearchDto;
import java.time.LocalDate;
import java.util.List;

public interface AccommodationRepositoryCustom {

    List<AccommodationSearchDto> findBySearchOption(String keyword, LocalDate checkInDate,
        LocalDate checkOutDate, Integer people, String sort, String direction, Integer minPrice,
        Integer maxPrice,
        Integer category, Double lat, Double lon);

//    Page<Accommodation> findBySearchOption(Pageable pageable, String keyword, LocalDate checkInDate,
//        LocalDate checkOutDate, Integer people, String sort, String direction, Integer minPrice,
//        Integer maxPrice,
//        Integer category, Double lat, Double lon);

}
