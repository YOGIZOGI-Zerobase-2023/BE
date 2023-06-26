package com.zerobase.yogizogi.accommodation.repository;

import static com.querydsl.jpa.JPAExpressions.selectDistinct;
import static com.zerobase.yogizogi.accommodation.domain.entity.QAccommodation.accommodation;
import static com.zerobase.yogizogi.accommodation.domain.entity.QPrice.*;
import static com.zerobase.yogizogi.accommodation.domain.entity.QRoom.room;
import static org.springframework.data.relational.core.sql.StatementBuilder.select;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.yogizogi.accommodation.domain.entity.Accommodation;
import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
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
        JPAQuery<Accommodation> query = queryFactory.selectDistinct(accommodation).from(accommodation)
            .leftJoin(accommodation.rooms, room)
            .fetchJoin()
            .leftJoin(room.prices, price1)
            .fetchJoin()
            .where(containKeyword(keyword), checkDate(checkInDate, checkOutDate),
                checkPeople(people), eqCategory(category))
            .groupBy(accommodation.id)
            .having(checkPrice(minPrice, maxPrice))
            .orderBy(sortBy(sort, direction));

//        List<Accommodation> accommodations = this.getQuerydsl().applyPagination(
//                (org.springframework.data.domain.Pageable) pageable, query)
//            .fetch();

        return query.fetch();
    }

    // where

    // 지역, 이름에 키워드가 들어가면 true
    private BooleanExpression containKeyword(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return null;
        }
        // 지역에 keyword 가 있다면
        return accommodation.region.containsIgnoreCase(keyword).or(accommodation.name.containsIgnoreCase(keyword));
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

        return price1.date.goe(checkInDate)
            .and(price1.date.lt(checkOutDate));
    }


    private BooleanExpression checkPeople(Integer people) {
        // 숙소의 방들중 가장 많은 인원 수 >= 예약하려는 인원 수
        if (people == null) {
            return null;
        }
        return room.maxPeople.goe(people);
    }


    // 날짜의 범위가 1 이상일 때 특정 날짜 하루만 최저, 최대 사이더라도 true
    // or 날짜 범위의 합이 최저, 최대 사이 -> 방별 가격 합을 구해야함 (group by roomId)
    private BooleanExpression checkPrice(Integer minPrice, Integer maxPrice) {
        if (minPrice == null || maxPrice == null) {
            return null;
        }

        return price1.price.min().loe(maxPrice).and(price1.price.min().goe(minPrice));
    }

    // orderBy
    //sort 를 enum 으로 변경?
    // double, integer 이 같이 있음 integer 을 double 로 변환 or object를 쓸지
    private static OrderSpecifier<?> sortBy(String sort, String direction) {
        if (sort.equals("price")) {
            return direction == "desc" ? price1.price.min().desc() : price1.price.min().asc();
        } else if (sort.equals("rate")) {
            return direction.equals("desc") ? accommodation.score.desc() : accommodation.score.asc();
        } else if (sort.equals("distance")) {
            // To-DO 경도, 위도로 거리를 구하여 정렬
//            return direction == "desc" ? accommodation.
            return null;
        }
        return null;
    }


    //두 지점 간의 거리 계산 (킬로미터)
    private static double distance(double lat1, double lon1, double lat2, double lon2) {

        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta));

        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        dist = dist * 1.609344;

        return dist;
    }


    // This function converts decimal degrees to radians
    private static double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    // This function converts radians to decimal degrees
    private static double rad2deg(double rad) {
        return (rad * 180 / Math.PI);
    }


}
