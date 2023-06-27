package com.zerobase.yogizogi.accommodation.service;

import com.zerobase.yogizogi.accommodation.domain.entity.Accommodation;
import com.zerobase.yogizogi.accommodation.dto.AccommodationDto;
import com.zerobase.yogizogi.accommodation.dto.AccommodationSearchDto;
import com.zerobase.yogizogi.accommodation.repository.AccommodationRepository;
import com.zerobase.yogizogi.global.exception.CustomException;
import com.zerobase.yogizogi.global.exception.ErrorCode;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccommodationService {

    private final AccommodationRepository accommodationRepository;

    // TEST
    // 숙소 조회
    public AccommodationDto getAccommodation(Long id) {
        Accommodation accommodation = accommodationRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ACCOMMODATION));

        // TO-DO
        // price 결측치에 대한 처리 필요 -> price, room을 list -> set으로 변경하여 get() 사용 불가
        return AccommodationDto.from(accommodation);//팩토리 메서드로 변환
    }

    // 숙소 검색

    public List<AccommodationSearchDto> searchAccommodation(String keyword, LocalDate checkInDate,
        LocalDate checkOutDate,
        Integer people, String sort, String direction, Integer minPrice, Integer maxPrice,
        Integer category,
        Double lat, Double lon) {

        if (checkInDate != null & checkOutDate != null) {
            if ((checkInDate.isBefore(LocalDate.now()) || checkInDate.isBefore(
                LocalDate.of(2023, 7, 1))
                || checkOutDate.isAfter(LocalDate.of(2023, 10, 1)))) {
                throw new CustomException(ErrorCode.NOT_CORRECT_DATE);
            } else if (checkOutDate.isAfter(checkInDate.plusDays(7))) {
                throw new CustomException(ErrorCode.NOT_CORRECT_DATE_RANGE);
            }
        }

        return accommodationRepository.findBySearchOption(keyword,
            checkInDate, checkOutDate, people, sort,
            direction, minPrice, maxPrice, category, lat, lon);
    }

    public List<Accommodation> getAccommodationsByArea(double leftUpLat, double rightDownLat,
        double leftUpLon, double rightDownLon) {
        return accommodationRepository.findInArea(leftUpLat, rightDownLat,
            leftUpLon, rightDownLon);
    }
}
