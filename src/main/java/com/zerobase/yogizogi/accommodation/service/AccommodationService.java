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
        return AccommodationDto.builder()
            .id(accommodation.getId())
            .name(accommodation.getName())
            .category(accommodation.getCategory())
            .address(accommodation.getAddress())
            .rate(accommodation.getRate())
            .picUrl(accommodation.getPicUrl())
            .lon(accommodation.getLon())
            .lat(accommodation.getLat())
            .price(
                accommodation.getRooms().stream().findFirst().orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND_ROOM)).getPrices().stream().findFirst()
                    .orElseThrow(()->new CustomException(ErrorCode.NOT_ALLOW_ACCESS)).getPrice())
            .build();
    }

    // 숙소 검색

    public List<AccommodationSearchDto> searchAccommodation(String keyword, LocalDate checkInDate,
        LocalDate checkOutDate,
        Integer people, String sort, String direction, Integer minPrice, Integer maxPrice,
        Integer category,
        Double lat, Double lon) {

        return accommodationRepository.findBySearchOption(keyword,
            checkInDate, checkOutDate, people, sort,
            direction, minPrice, maxPrice, category, lat, lon);
    }
}
