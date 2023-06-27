package com.zerobase.yogizogi.accommodation.controller;


import com.zerobase.yogizogi.accommodation.domain.entity.Accommodation;
import com.zerobase.yogizogi.accommodation.domain.model.PositionRequestForm;
import com.zerobase.yogizogi.accommodation.dto.AccommodationDto;
import com.zerobase.yogizogi.accommodation.service.AccommodationService;
import com.zerobase.yogizogi.global.ApiResponse;
import com.zerobase.yogizogi.global.ResponseCode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/accommodation")
@RequiredArgsConstructor
public class AccommodationController {

    private final AccommodationService accommodationService;

    // 숙소 리스트 조회

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<Object>> search(
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate checkindate,
        @RequestParam(required = false) @DateTimeFormat(iso = ISO.DATE) LocalDate checkoutdate,
        @RequestParam(required = false) Integer people,
        @RequestParam(required = false) String sort,
        @RequestParam(required = false) String direction,
        @RequestParam(required = false) Integer minprice,
        @RequestParam(required = false) Integer maxprice,
        @RequestParam(required = false) Integer category,
        @RequestParam(required = false) Double lat, @RequestParam(required = false) Double lon) {
        var result = accommodationService.searchAccommodation(keyword, checkindate, checkoutdate,
            people,
            sort, direction, minprice, maxprice, category, lat, lon);

        System.out.println(result.size());

        return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
    }

    @GetMapping("/")
    public ResponseEntity<ApiResponse<Object>> autocomplete(@RequestParam Long id) {
        var result = accommodationService.getAccommodation(id);
        return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
    }

    @PostMapping("/map")
    public ResponseEntity<ApiResponse<Object>> getAccommodationsByArea(
        @RequestBody PositionRequestForm positionRequestForm) {
        List<Accommodation> accommodations = accommodationService.getAccommodationsByArea(
            positionRequestForm.getLeftUpLat(), positionRequestForm.getRightDownLat(),
            positionRequestForm.getLeftUpLon(), positionRequestForm.getRightDownLon());
        List<AccommodationDto> result = accommodations.stream()
            .filter(accommodation -> !accommodation.getRooms().isEmpty())
            .filter(
                accommodation -> accommodation.getRooms().stream().flatMap(room -> room.getPrices()
                    .stream()).anyMatch(price -> price.getRoomCnt() > 0)) //방 수가 0 즉 예약 불가능은 가져오지 않음.
            .map(AccommodationDto::from).collect(
                Collectors.toList());
        System.out.println(result.size());
        return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
    }
}
