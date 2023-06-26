package com.zerobase.yogizogi.accommodation.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.zerobase.yogizogi.accommodation.domain.entity.Accommodation;
import java.awt.print.Pageable;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;

@SpringBootTest
class AccommodationRepositoryImplTest {

    @Autowired
    private JPAQueryFactory jpaQuery;

    @Autowired
    private AccommodationRepository accommodationRepository;

    // 사람수
    @Test
    void findBySearchOptionPeople() {
        //given
        List<Accommodation> bySearchOption = accommodationRepository.findBySearchOption(null,
            null, null, 4, null,
            null, null, null, null, null, null);
        //when
        //then
        System.out.println(bySearchOption.size());

    }

    // category 테스트
    @Test
    void findBySearchOptionCategory() {
        //given
        List<Accommodation> category1 = accommodationRepository.findBySearchOption(null,
            null, null, null, null,
            null, null, null, 1, null, null);

        List<Accommodation> category2 = accommodationRepository.findBySearchOption(null,
            null, null, null, null,
            null, null, null, 2, null, null);

        List<Accommodation> category3 = accommodationRepository.findBySearchOption(null,
            null, null, null, null,
            null, null, null, 3, null, null);
        //when
        //then
        assertEquals(94, category1.size());
        assertEquals(35, category2.size());
        assertEquals(137, category3.size());
    }

    @Test
    void findBySearchOptionPrice() {
        //given
        List<Accommodation> bySearchOption = accommodationRepository.findBySearchOption(null,
            null, null, null, null,
            null, 10000, 50000, null, null, null);

        //when
        //then
        System.out.println(bySearchOption.size());
        assertEquals(45, bySearchOption.size());

    }

    @Test
    void findBySearchOptionDate() {
        //given
        List<Accommodation> bySearchOption = accommodationRepository.findBySearchOption(null,
            LocalDate.of(2023, 07, 8), LocalDate.of(2023, 07, 10), null, null,
            null, 10000, 50000, null, null, null);

        //when
        //then
        System.out.println(bySearchOption.size());
        assertEquals(43, bySearchOption.size());
    }

    @Test
    void findBySearchOptionKeyword() {
        //given
        List<Accommodation> bySearchOption = accommodationRepository.findBySearchOption("역삼",
            LocalDate.of(2023, 07, 8), LocalDate.of(2023, 07, 10), null, null,
            null, 10000, 50000, null, null, null);

        //when
        //then
        System.out.println(bySearchOption.size());
        assertEquals(25, bySearchOption.size());
    }

    @Test
    void findBySearchOptionSort() {
        //given
        List<Accommodation> bySearchOption = accommodationRepository.findBySearchOption("역삼",
            LocalDate.of(2023, 07, 8), LocalDate.of(2023, 07, 10), null, "rate",
            "desc", 10000, 50000, null, null, null);

        List<Accommodation> bySearchOptionPrice = accommodationRepository.findBySearchOption("역삼",
            LocalDate.of(2023, 07, 8), LocalDate.of(2023, 07, 10), null, "price",
            "desc", 10000, 50000, null, null, null);

        //when
        //then
        System.out.println(bySearchOption.size());
        assertTrue(bySearchOption.get(0).getScore() >= bySearchOption.get(1).getScore());
        assertTrue(bySearchOption.get(5).getScore() >= bySearchOption.get(6).getScore());
        assertTrue(bySearchOption.get(10).getScore() >= bySearchOption.get(15).getScore());

        // To-Do
        // price test 추가 (mysql 로 확인 완료)
//        assertTrue(bySearchOptionPrice.get(0).getRooms().stream().min(x -> x.getPrices().stream().sorted((a,b)-> a.getPrice() - b.getPrice()))>= bySearchOptionPrice.get(1).getScore());
//        assertTrue(bySearchOptionPrice.get(5).getScore() >= bySearchOptionPrice.get(6).getScore());
//        assertTrue(bySearchOptionPrice.get(10).getScore() >= bySearchOptionPrice.get(15).getScore());

    }





}