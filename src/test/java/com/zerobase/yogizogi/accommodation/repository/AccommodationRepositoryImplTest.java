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

    }





}