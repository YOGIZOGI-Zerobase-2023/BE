package com.zerobase.yogizogi.accommodation.repository;

import com.zerobase.yogizogi.accommodation.domain.entity.Accommodation;
import java.awt.print.Pageable;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;

public interface AccommodationRepositoryCustom {

    List<Accommodation> findBySearchOption(String keyword, LocalDate checkInDate,
        LocalDate checkOutDate, Integer people, String sort, String direction, Integer minPrice,
        Integer maxPrice,
        Integer category, Double lat, Double lon);

//    Page<Accommodation> findBySearchOption(Pageable pageable, String keyword, LocalDate checkInDate,
//        LocalDate checkOutDate, Integer people, String sort, String direction, Integer minPrice,
//        Integer maxPrice,
//        Integer category, Double lat, Double lon);

}
