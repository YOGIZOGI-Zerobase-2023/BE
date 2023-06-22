package com.zerobase.yogizogi.accommodation.repository;

import com.zerobase.yogizogi.accommodation.domain.entity.Accommodation;
import java.awt.print.Pageable;
import java.util.Date;
import org.springframework.data.domain.Page;

public interface AccommodationRepositoryCustom {

    Page<Accommodation> findBySearchOption(Pageable pageable, String keyword, Date checkInDate,
        Date checkOutDate, Integer people, String sort, String direction, Integer minPrice,
        Integer maxPrice,
        Integer category, Double lat, Double lon);

}
