package com.zerobase.yogizogi.accommodation.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.zerobase.yogizogi.accommodation.domain.entity.Convenience;
import com.zerobase.yogizogi.accommodation.domain.model.RoomDetailForm;
import com.zerobase.yogizogi.accommodation.dto.AccommodationSearchDto;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class AccommodationRepositoryImplTest {


    @Autowired
    private AccommodationRepository accommodationRepository;

    @Autowired
    private ConvenienceRepository convenienceRepository;

    @Test
    void convenienceTest() {
        //given
        List<Convenience> allByAccommodationId = convenienceRepository.findAllByAccommodationId(
            100L);
        //when

        //then
        System.out.println(allByAccommodationId.size());
    }


    @Test
    void findByIdAndDateAndPeople() {

        //given
        List<RoomDetailForm> results = accommodationRepository.findRoomDetailByIdAndDateAndPeople(
            1L,
            LocalDate.of(2023, 7, 6), LocalDate.of(2023, 7, 8),
            2);

        //when
        //then
        System.out.println(results.size());
        results.forEach(result -> System.out.println(result.getId()));

    }

    // 사람수
    @Test
    void findBySearchOptionPeople() {
        //given
        List<AccommodationSearchDto> bySearchOption = accommodationRepository.findBySearchOption(
            null,
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
        List<AccommodationSearchDto> category1 = accommodationRepository.findBySearchOption(null,
            null, null, null, null,
            null, null, null, 1, null, null);

        List<AccommodationSearchDto> category2 = accommodationRepository.findBySearchOption(null,
            null, null, null, null,
            null, null, null, 2, null, null);

        List<AccommodationSearchDto> category3 = accommodationRepository.findBySearchOption(null,
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
        List<AccommodationSearchDto> bySearchOption = accommodationRepository.findBySearchOption(
            null,
            null, null, 2, null,
            null, 10000, 50000, null, null, null);

        //when
        //then
        System.out.println(bySearchOption.size());
        assertEquals(45, bySearchOption.size());
    }

    @Test
    void findBySearchOptionDate() {
        //given
        List<AccommodationSearchDto> bySearchOption = accommodationRepository.findBySearchOption(
            null,
            LocalDate.of(2023, 7, 8), LocalDate.of(2023, 7, 10), 2, null,
            null, 10000, 50000, null, null, null);

        //when
        //then
        System.out.println(bySearchOption.size());

        assertEquals(43, bySearchOption.size());
    }

    @Test
    void findBySearchOptionKeyword() {
        //given
        List<AccommodationSearchDto> bySearchOption = accommodationRepository.findBySearchOption(
            "역삼",
            LocalDate.of(2023, 7, 8), LocalDate.of(2023, 7, 10), null, null,
            null, 10000, 50000, null, null, null);

        //when
        //then
        System.out.println(bySearchOption.size());
        assertEquals(25, bySearchOption.size());
    }

    @Test
    void findBySearchOptionSort() {
        //given
        List<AccommodationSearchDto> bySearchOption = accommodationRepository.findBySearchOption(
            "역삼",
            LocalDate.of(2023, 7, 8), LocalDate.of(2023, 7, 10), null, "rate",
            "desc", 10000, 50000, null, null, null);

        List<AccommodationSearchDto> bySearchOptionPrice = accommodationRepository.findBySearchOption(
            "역삼",
            LocalDate.of(2023, 7, 8), LocalDate.of(2023, 7, 10), null, "price",
            "desc", 10000, 50000, null, null, null);

        List<AccommodationSearchDto> bySearchOptionDistance = accommodationRepository.findBySearchOption(
            "역삼",
            LocalDate.of(2023, 7, 8), LocalDate.of(2023, 7, 10), null, "distance",
            "desc", 10000, 50000, null, 37.563724611104156, 126.97775897488705);

        //when
        //then
        System.out.println(bySearchOption.size());

        // score sort test
        assertTrue(bySearchOption.get(0).getRate() >= bySearchOption.get(1).getRate());
        assertTrue(bySearchOption.get(5).getRate() >= bySearchOption.get(6).getRate());
        assertTrue(bySearchOption.get(10).getRate() >= bySearchOption.get(15).getRate());

        // price sort test
        assertEquals(bySearchOptionPrice.get(0).getId(), 4);
        assertEquals(bySearchOptionPrice.get(5).getId(), 1);
        assertEquals(bySearchOptionPrice.get(10).getId(), 26);

        assertEquals(bySearchOptionDistance.get(0).getId(), 90);
        assertEquals(bySearchOptionDistance.get(5).getId(), 30);
        assertEquals(bySearchOptionDistance.get(10).getId(), 15);

    }


}