package com.zerobase.yogizogi.accommodation.repository;

import static com.zerobase.yogizogi.accommodation.domain.entity.QAccommodation.accommodation;

import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.yogizogi.accommodation.domain.entity.Accommodation;
import java.awt.print.Pageable;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;

public class AccommodationRepositoryImpl extends QuerydslRepositorySupport implements
    AccommodationRepositoryCustom {

    @Autowired
    private JPAQueryFactory queryFactory;

    public AccommodationRepositoryImpl() {
        super(Accommodation.class);
    }


    @Override
    public Page<Accommodation> findBySearchOption(Pageable pageable, String keyword,
        Date checkInDate, Date checkOutDate, Integer people, String sort, String direction,
        Integer minPrice, Integer maxPrice, Integer category, Double lat, Double lon) {

        // query 생성
        JPQLQuery<Accommodation> query = queryFactory.selectFrom(accommodation);
//            .where(containKeywordRegion(keyword), checkDate(checkInDate, checkOutDate), checkPeople(people),
//                checkPrice(minPrice, maxPrice), eqCategory(category))
//            .orderBy(sort, direction);

        List<Accommodation> accommodations = this.getQuerydsl().applyPagination(
                (org.springframework.data.domain.Pageable) pageable, query)
            .fetch();

        return new PageImpl<Accommodation>((List<Accommodation>) accommodation,
            (org.springframework.data.domain.Pageable) pageable, query.fetchCount());
    }

//    // where
//
//    private BooleanExpression containKeywordRegion(String keyword) {
//        if (keyword == null || keyword.isEmpty()) {
//            return null;
//        }
//        // 지역에 keyword 가 있다면
//        return accommodation.region.containsIgnoreCase(keyword);
//    }
//
//    private BooleanExpression containKeywordName(String keyword) {
//        if (keyword == null || keyword.isEmpty()) {
//            return null;
//        }
//        // 이름에 keyword 가 있다면
//        return accommodation.name.containsIgnoreCase(keyword);
//    }
//
//    private BooleanExpression eqCategory(Integer category) {
//        if (category == null) {
//            return null;
//        }
//        return accommodation.category.eq(category);
//    }
//
//    private BooleanExpression checkDate(Date checkInDate, Date checkOutDate) {
//        if (checkInDate == null || checkOutDate == null) {
//            return null;
//        }
//        return accommodation;
//    }
//
//    private BooleanExpression checkPeople(Integer people) {
//        if (people == null) {
//            return null;
//        }
//        return accommodation.rooms.any(();
//    }
//
//    private BooleanExpression checkPrice(Integer minPrice, Integer maxPrice) {
//        if (minPrice == null || maxPrice == null) {
//            return null;
//        }
//        return accommodation.rooms;
//    }

    // orderBy


}
