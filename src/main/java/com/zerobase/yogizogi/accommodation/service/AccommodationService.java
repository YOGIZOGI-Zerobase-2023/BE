package com.zerobase.yogizogi.accommodation.service;

import com.zerobase.yogizogi.accommodation.domain.entity.Accommodation;
import com.zerobase.yogizogi.accommodation.domain.model.RoomDetailForm;
import com.zerobase.yogizogi.accommodation.dto.AccommodationCompareDto;
import com.zerobase.yogizogi.accommodation.dto.AccommodationDetailDto;
import com.zerobase.yogizogi.accommodation.dto.AccommodationSearchDto;
import com.zerobase.yogizogi.accommodation.dto.RoomCompareDto;
import com.zerobase.yogizogi.accommodation.repository.AccommodationRepository;
import com.zerobase.yogizogi.global.exception.CustomException;
import com.zerobase.yogizogi.global.exception.ErrorCode;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccommodationService {

    private final AccommodationRepository accommodationRepository;

    public AccommodationDetailDto getAccommodationDetail(Long id, LocalDate checkInDate,
        LocalDate checkOutDate, Integer people) {

        Accommodation accommodation = accommodationRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ACCOMMODATION));

        List<RoomDetailForm> rooms = accommodationRepository.findRoomDetailByIdAndDateAndPeople(id,
            checkInDate, checkOutDate, people);

        return new AccommodationDetailDto(accommodation, rooms);
    }

    public Page<AccommodationSearchDto> searchAccommodation(String keyword, LocalDate checkInDate,
        LocalDate checkOutDate,
        Integer people, String sort, String direction, Integer minPrice, Integer maxPrice,
        Integer category,
        Double lat, Double lon, Pageable pageable) {
        if (checkInDate != null & checkOutDate != null) {
            if (checkInDate.isBefore(LocalDate.of(2023, 7, 1))
                || checkOutDate.isAfter(LocalDate.of(2023, 10, 1))) {
                throw new CustomException(ErrorCode.NOT_CORRECT_DATE);
            } else if (checkOutDate.isAfter(checkInDate.plusDays(7))) {
                throw new CustomException(ErrorCode.NOT_CORRECT_DATE_RANGE);
            }
        }

        List<AccommodationSearchDto> resultList = accommodationRepository.findBySearchOption(
            keyword,
            checkInDate, checkOutDate, people, sort,
            direction, minPrice, maxPrice, category, lat, lon);
        System.out.println("total : " + resultList.size());
        int start = (int) pageable.getOffset();
        int end = Math.min((start + pageable.getPageSize()), resultList.size());
        System.out.println("페이지 내의 값의 개수 : " + (end - start));
        List<AccommodationSearchDto> pagedResultList = resultList.subList(start, end);

        return new PageImpl<>(pagedResultList, pageable, resultList.size());
    }

    public List<Accommodation> getAccommodationsByArea(double leftUpLat, double rightDownLat,
        double leftUpLon, double rightDownLon, LocalDate checkInDate, LocalDate checkOutDate) {
        return accommodationRepository.findInArea(leftUpLat, rightDownLat,
            leftUpLon, rightDownLon, checkInDate, checkOutDate);
    }

    public RoomCompareDto getCompareRoom(Long roomId,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        Integer people) {

        return accommodationRepository.findRoomByIdAndDateAndPeople(
            roomId, checkInDate, checkOutDate, people);

    }

    public AccommodationCompareDto getCompareAccommodation(Long accommodationId,
        LocalDate checkInDate,
        LocalDate checkOutDate,
        Integer people) {

        return accommodationRepository.findAccommodationByIdAndDateAndPeople(
            accommodationId, checkInDate, checkOutDate, people);
    }
}
