package com.zerobase.yogizogi.accommodation.service;

import com.zerobase.yogizogi.accommodation.domain.entity.Accommodation;
import com.zerobase.yogizogi.accommodation.domain.model.RoomDetailForm;
import com.zerobase.yogizogi.accommodation.dto.AccommodationDetailDto;
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
        return AccommodationDto.from(accommodation);
    }

    // 숙소 상세정보 조회
    public AccommodationDetailDto getAccommodationDetail(Long id, LocalDate checkInDate,
        LocalDate checkOutDate, Integer people) {

        // 숙소 정보 가져오기
        Accommodation accommodation = accommodationRepository.findById(id)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ACCOMMODATION));

        // repository 에서 가져올것인가? 아래 getRoomDetail 을 사용할 것인가?
        // 방 정보 가져오기 : min(price)를 가져옴 모든 price 정보 X
        List<RoomDetailForm> rooms = accommodationRepository.findRoomDetailByIdAndDateAndPeople(id,
            checkInDate, checkOutDate, people);

        return new AccommodationDetailDto(accommodation, rooms);
    }

    public List<RoomDetailForm> getRoomDetail(Long id, LocalDate checkInDate,
        LocalDate checkOutDate, Integer people) {
        return accommodationRepository.findRoomDetailByIdAndDateAndPeople(id,
            checkInDate, checkOutDate, people);
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
}
