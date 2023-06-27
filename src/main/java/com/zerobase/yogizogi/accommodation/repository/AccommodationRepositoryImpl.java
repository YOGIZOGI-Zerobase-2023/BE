package com.zerobase.yogizogi.accommodation.repository;

import static com.querydsl.jpa.JPAExpressions.selectDistinct;
import static com.zerobase.yogizogi.accommodation.domain.entity.QAccommodation.accommodation;
import static com.zerobase.yogizogi.accommodation.domain.entity.QPrice.price1;
import static com.zerobase.yogizogi.accommodation.domain.entity.QRoom.room;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.yogizogi.accommodation.domain.entity.Accommodation;
import com.zerobase.yogizogi.accommodation.dto.AccommodationDto;
import com.zerobase.yogizogi.accommodation.dto.AccommodationSearchDto;
import com.zerobase.yogizogi.accommodation.dto.QAccommodationSearchDto;
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

    public List<AccommodationSearchDto> findBySearchOption(String keyword,
        LocalDate checkInDate, LocalDate checkOutDate, Integer people, String sort,
        String direction,
        Integer minPrice, Integer maxPrice, Integer category, Double lat, Double lon) {

        // query 생성성
        JPAQuery<AccommodationSearchDto> query = queryFactory.selectDistinct(
                new QAccommodationSearchDto(accommodation, price1.price.min(), room.maxPeople.max()))
            .from(accommodation)
            .leftJoin(accommodation.rooms, room)
            .fetchJoin()
            .leftJoin(room.prices, price1)
            .where(containKeyword(keyword), checkDate(checkInDate, checkOutDate),
                checkPeople(people), eqCategory(category), checkRoomCnt())
            .groupBy(accommodation.id)
            .having(checkPrice(minPrice, maxPrice))
            .orderBy(sortBy(sort, direction, lat, lon));

//        List<Accommodation> accommodations = this.getQuerydsl().applyPagination(
//                (org.springframework.data.domain.Pageable) pageable, query)
//            .fetch();

        return query.fetch();
    }

    private BooleanExpression checkRoomCnt() {
        return price1.roomCnt.gt(0);
    }

    // where

    // 지역, 이름에 키워드가 들어가면 true
    private BooleanExpression containKeyword(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return null;
        }
        // 지역에 keyword 가 있다면
        return accommodation.region.containsIgnoreCase(keyword)
            .or(accommodation.name.containsIgnoreCase(keyword));
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
    // double, integer 이 같이 있음 integer 을 double 로 변환 or object 를 쓸지
    private static OrderSpecifier<?> sortBy(String sort, String direction, Double baseLat,
        Double baseLng) {
        if (sort == null) {
            // sort 가 Null일 시 평점 순 desc
            return accommodation.score.desc();
        }

        if (sort.equals("price")) {
            return direction.equals("desc") ? price1.price.min().desc() : price1.price.min().asc();
        } else if (sort.equals("rate")) {
            return direction.equals("desc") ? accommodation.score.desc()
                : accommodation.score.asc();
        } else if (sort.equals("distance")) {
            // 경도, 위도로 거리를 구하여 정렬 (get_distance 사용자 정의 함수 사용)
            // 경도, 위도 값이 없을 때는 경도, 위도를 0으로 설정하여 정렬
            return direction.equals("desc") ? Expressions.stringTemplate(
                "get_distance({0},{1},{2},{3})",
                accommodation.lat, accommodation.lng, baseLat, baseLng).desc()
                : Expressions.stringTemplate("get_distance({0},{1},{2},{3})", accommodation.lat,
                    accommodation.lng, baseLat, baseLng).asc();
        } else {
            // sort 가 Null일 시 평점 순 desc
            return accommodation.score.desc();
        }
    }


}
