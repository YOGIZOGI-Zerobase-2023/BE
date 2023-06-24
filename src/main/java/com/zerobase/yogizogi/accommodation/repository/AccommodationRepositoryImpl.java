package com.zerobase.yogizogi.accommodation.repository;

import static com.querydsl.jpa.JPAExpressions.selectDistinct;
import static com.zerobase.yogizogi.accommodation.domain.entity.QAccommodation.accommodation;
import static com.zerobase.yogizogi.accommodation.domain.entity.QPrice.*;
import static com.zerobase.yogizogi.accommodation.domain.entity.QRoom.room;
import static org.springframework.data.relational.core.sql.StatementBuilder.select;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.yogizogi.accommodation.domain.entity.Accommodation;
import java.awt.print.Pageable;
import java.time.LocalDate;
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

//    @Override
//    public Page<Accommodation> findBySearchOption(Pageable pageable, String keyword,
//        LocalDate checkInDate, LocalDate checkOutDate, Integer people, String sort,
//        String direction,
//        Integer minPrice, Integer maxPrice, Integer category, Double lat, Double lon) {
//
//        // query 생성
//        JPQLQuery<Accommodation> query = queryFactory.selectDistinct(accommodation).from(accommodation)
//            .join(accommodation.rooms, room)
//            .fetchJoin()
//            .where(containKeywordRegion(keyword), checkDate(checkInDate, checkOutDate),
//                checkPeople(people), checkPrice(minPrice, maxPrice), eqCategory(category));
////            .orderBy(sortBy(sort, direction));
//
//        List<Accommodation> accommodations = this.getQuerydsl().applyPagination(
//                (org.springframework.data.domain.Pageable) pageable, query)
//            .fetch();
//
//        return new PageImpl<Accommodation>((List<Accommodation>) accommodation,
//            (org.springframework.data.domain.Pageable) pageable, query.fetchCount());
//    }

    public List<Accommodation> findBySearchOption( String keyword,
        LocalDate checkInDate, LocalDate checkOutDate, Integer people, String sort,
        String direction,
        Integer minPrice, Integer maxPrice, Integer category, Double lat, Double lon) {

        // query 생성
        JPQLQuery<Accommodation> query = queryFactory.selectDistinct(accommodation).from(accommodation)
            .leftJoin(accommodation.rooms, room)
            .fetchJoin()
            .where(containKeywordRegion(keyword), checkDate(checkInDate, checkOutDate),
                checkPeople(people), checkPrice(minPrice, maxPrice), eqCategory(category));
//            .orderBy(sortBy(sort, direction));

//        List<Accommodation> accommodations = this.getQuerydsl().applyPagination(
//                (org.springframework.data.domain.Pageable) pageable, query)
//            .fetch();

        return query.fetch();
    }

    // where

    private BooleanExpression containKeywordRegion(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return null;
        }
        // 지역에 keyword 가 있다면
        return accommodation.region.containsIgnoreCase(keyword);
    }

    private BooleanExpression containKeywordName(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return null;
        }
        // 이름에 keyword 가 있다면
        return accommodation.name.containsIgnoreCase(keyword);
    }

    private BooleanExpression eqCategory(Integer category) {
        if (category == null) {
            return null;
        }
        return accommodation.category.eq(category);
    }

    private BooleanExpression checkDate(LocalDate checkInDate, LocalDate checkOutDate) {
        if (checkInDate == null || checkOutDate == null) {
            return null;
        }

        selectDistinct(room).from(room)
            .leftJoin(room.prices, price1);

        return room.prices.any().date.goe(checkInDate)
            .and(room.prices.any().date.loe(checkOutDate));
    }


    private BooleanExpression checkPeople(Integer people) {
        // 숙소의 방들중 가장 많은 인원 수 >= 예약하려는 인원 수
        if (people == null) {
            return null;
        }
        return room.maxPeople.goe(people);
    }

    private BooleanExpression checkPrice(Integer minPrice, Integer maxPrice) {
        if (minPrice == null || maxPrice == null) {
            return null;
        }

        selectDistinct(room).from(room)
            .leftJoin(room.prices, price1);

        return price1.price.loe(maxPrice).and(price1.price.goe(minPrice));
    }

    // orderBy
    //sort 를 enum 으로 변경?
    private static OrderSpecifier<Double> sortBy(String sort, String direction) {
        if (sort.equals("price")) {
//            return direction == "desc" ? accommodation..price.desc() : accommodation.price.asc();
        } else if (sort.equals("rate")) {
            return direction.equals("desc") ? accommodation.score.desc() : accommodation.score.asc();
        } else if (sort.equals("distance")) {
            // 경도, 위도로 거리를 구하여 정렬
//            return direction == "desc" ? accommodation.
        }
        return null;
    }


}
