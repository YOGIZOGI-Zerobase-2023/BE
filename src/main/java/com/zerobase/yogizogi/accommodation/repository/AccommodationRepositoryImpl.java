package com.zerobase.yogizogi.accommodation.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.yogizogi.accommodation.domain.entity.Accommodation;
import java.awt.print.Pageable;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class AccommodationRepositoryImpl extends QuerydslRepositorySupport implements
    AccommodationRepositoryCustom {

    @Autowired
    private JPAQueryFactory jpaQueryFactory;

    public AccommodationRepositoryImpl() {
        super(Accommodation.class);
    }

    @Override
    public Page<Accommodation> findBySearchOption(Pageable pageable, String keyword,
        Date checkInDate, Date checkOutDate, Integer people, String sort, String direction,
        Integer minPrice, Integer maxPrice, Integer category, Double lat, Double lon) {
        return null;
    }
}
