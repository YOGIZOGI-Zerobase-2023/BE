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
import com.zerobase.yogizogi.accommodation.domain.entity.QAccommodation;
import com.zerobase.yogizogi.accommodation.domain.entity.QPrice;
import com.zerobase.yogizogi.accommodation.domain.entity.QRoom;
import com.zerobase.yogizogi.accommodation.domain.model.QRoomDetailForm;
import com.zerobase.yogizogi.accommodation.domain.model.RoomDetailForm;
import com.zerobase.yogizogi.accommodation.dto.AccommodationCompareDto;
import com.zerobase.yogizogi.accommodation.dto.AccommodationSearchDto;
import com.zerobase.yogizogi.accommodation.dto.QAccommodationCompareDto;
import com.zerobase.yogizogi.accommodation.dto.QAccommodationSearchDto;
import com.zerobase.yogizogi.accommodation.dto.QRoomCompareDto;
import com.zerobase.yogizogi.accommodation.dto.RoomCompareDto;
import com.zerobase.yogizogi.global.exception.CustomException;
import com.zerobase.yogizogi.global.exception.ErrorCode;
import java.time.LocalDate;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;


public class AccommodationRepositoryImpl extends QuerydslRepositorySupport implements
    AccommodationRepositoryCustom {

    @Autowired
    private JPAQueryFactory queryFactory;

    @PersistenceContext
    private EntityManager em;

    public AccommodationRepositoryImpl() {
        super(Accommodation.class);
    }

    public List<RoomDetailForm> findRoomDetailByIdAndDateAndPeople(Long accommodationId,
        LocalDate checkInDate,
        LocalDate checkOutDate, Integer people) {

        JPAQuery<RoomDetailForm> query = queryFactory.select(
                new QRoomDetailForm(room, price1.price.sum()))
            .from(room)
            .leftJoin(room.prices, price1)
            .where(checkAccommodationId_room(accommodationId), checkDate(checkInDate, checkOutDate),
                checkPeople(people), checkRoomCnt())
            .groupBy(room.id);

        return query.fetch();
    }

    public RoomCompareDto findRoomByIdAndDateAndPeople(Long roomId,
        LocalDate checkInDate,
        LocalDate checkOutDate, Integer people) {

        JPAQuery<RoomCompareDto> query = queryFactory.select(
                new QRoomCompareDto(room, price1.price.sum()))
            .from(room)
            .leftJoin(room.prices, price1)
            .where(checkRoomId(roomId), checkDate(checkInDate, checkOutDate),
                checkPeople(people), checkRoomCnt())
            .groupBy(room.id);

        return query.fetch().get(0);
    }

    public AccommodationCompareDto findAccommodationByIdAndDateAndPeople(Long accommodationId,
        LocalDate checkInDate,
        LocalDate checkOutDate, Integer people) {

        JPAQuery<AccommodationCompareDto> query = queryFactory.selectDistinct(
                new QAccommodationCompareDto(accommodation, price1.price.min()))
            .from(accommodation)
            .leftJoin(accommodation.rooms, room)
            .fetchJoin()
            .leftJoin(room.prices, price1)
            .where(checkAccommodationId(accommodationId), checkDate(checkInDate, checkOutDate),
                checkPeople(people), checkRoomCnt())
            .groupBy(accommodation.id);

        if (query.fetch().isEmpty()) {
            throw new CustomException(ErrorCode.NOT_EXISTED_ROOM);
        }

        return query.fetch().get(0);
    }


    public List<AccommodationSearchDto> findBySearchOption(String keyword,
        LocalDate checkInDate, LocalDate checkOutDate, Integer people, String sort,
        String direction,
        Integer minPrice, Integer maxPrice, Integer category, Double lat, Double lon) {

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

        return query.fetch();
    }

    private BooleanExpression checkAccommodationId(Long accommodationId) {
        return accommodation.id.eq(accommodationId);
    }

    private BooleanExpression checkRoomId(Long roomId) {
        return room.id.eq(roomId);
    }


    private BooleanExpression checkAccommodationId_room(Long accommodationId) {
        return room.accommodation.id.eq(accommodationId);
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
        if (people == null) {
            return null;
        }
        return room.maxPeople.goe(people);
    }

    private BooleanExpression checkPrice(Integer minPrice, Integer maxPrice) {
        if (minPrice == null || maxPrice == null) {
            return null;
        }

        return price1.price.min().loe(maxPrice).and(price1.price.min().goe(minPrice));
    }

    private static OrderSpecifier<?> sortBy(String sort, String direction, Double baseLat,
        Double baseLng) {
        if (sort == null) {
            return accommodation.rate.desc();
        }

        switch (sort) {
            case "price":
                return direction.equals("desc") ? price1.price.min().desc()
                    : price1.price.min().asc();
            case "rate":
                return direction.equals("desc") ? accommodation.rate.desc()
                    : accommodation.rate.asc();
            case "distance":
                return direction.equals("desc") ? Expressions.stringTemplate(
                    "get_distance({0},{1},{2},{3})",
                    accommodation.lat, accommodation.lon, baseLat, baseLng).desc()
                    : Expressions.stringTemplate("get_distance({0},{1},{2},{3})", accommodation.lat,
                        accommodation.lon, baseLat, baseLng).asc();
            default:
                return accommodation.rate.desc();
        }
    }

    @Override
    public List<Accommodation> findInArea(double leftUpLat, double rightDownLat, double leftUpLon,
        double rightDownLon, LocalDate checkInDate, LocalDate checkOutDate) {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QAccommodation accommodation = QAccommodation.accommodation;
        QRoom room = QRoom.room;
        QPrice price = price1;

        return queryFactory.selectFrom(accommodation)
            .join(accommodation.rooms, room).fetchJoin()
            .join(room.prices, price).fetchJoin()
            .where(accommodation.lat.between(rightDownLat, leftUpLat)
                .and(accommodation.lon.between(leftUpLon, rightDownLon))
                .and(price.date.between(checkInDate, checkOutDate))
                .and(price.price.gt(0))
                .and(price.roomCnt.gt(0)))
            .distinct()
            .fetch();
    }

}
