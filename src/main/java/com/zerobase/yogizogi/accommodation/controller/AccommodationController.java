package com.zerobase.yogizogi.accommodation.controller;


import com.zerobase.yogizogi.accommodation.domain.entity.Accommodation;
import com.zerobase.yogizogi.accommodation.domain.model.PositionRequestForm;
import com.zerobase.yogizogi.accommodation.dto.AccommodationDto;
import com.zerobase.yogizogi.accommodation.dto.AccommodationSearchDto;
import com.zerobase.yogizogi.accommodation.service.AccommodationService;
import com.zerobase.yogizogi.global.ApiResponse;
import com.zerobase.yogizogi.global.ResponseCode;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
        @RequestParam(required = false) Double lat,
        @RequestParam(required = false) Double lon,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "20") int pagesize) {

        if (sort == null) {
            sort = "rate";
        }
        if (direction == null) {
            direction = "desc";
        }

        PageRequest pageRequest = PageRequest.of(page, pagesize,
            Sort.Direction.fromString(direction), sort);

        Page<AccommodationSearchDto> result = accommodationService
            .searchAccommodation(
                keyword, checkindate, checkoutdate,
                people, sort, direction, minprice, maxprice,
                category, lat, lon, pageRequest);

        return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
    }

    @GetMapping("/{accommodationId}")
    public ResponseEntity<?> getAccommodationDetail(@PathVariable Long accommodationId,
        @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate checkindate,
        @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate checkoutdate,
        @RequestParam Integer people) {
        var result = accommodationService.getAccommodationDetail(accommodationId, checkindate,
            checkoutdate, people);

        return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
    }

    @PostMapping("/map")
    public ResponseEntity<ApiResponse<Object>> getAccommodationsByArea(
        @RequestBody PositionRequestForm positionRequestForm) {
        List<Accommodation> accommodations = accommodationService.getAccommodationsByArea(
            positionRequestForm.getLeftUpLat(), positionRequestForm.getRightDownLat(),
            positionRequestForm.getLeftUpLon(), positionRequestForm.getRightDownLon(),
            positionRequestForm.getCheckInDate(), positionRequestForm.getCheckOutDate());
        List<AccommodationDto> result = accommodations.stream()
            //방 수가 0 즉 예약 불가능은 가져오지 않음.
            .map(AccommodationDto::from).collect(
                Collectors.toList());
        System.out.println(result.size());
        return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
    }

    @GetMapping("/compare/accommodation")
    public ResponseEntity<ApiResponse<Object>> getCompareAccommodation(
        @RequestParam Long accommodationid,
        @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate checkindate,
        @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate checkoutdate,
        @RequestParam Integer people
    ) {
        var result = accommodationService.getCompareAccommodation(accommodationid, checkindate,
            checkoutdate, people);

        return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
    }

    @GetMapping("/compare/room")
    public ResponseEntity<ApiResponse<Object>> getCompareRoom(
        @RequestParam Long roomid,
        @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate checkindate,
        @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate checkoutdate,
        @RequestParam Integer people
    ) {
        var result = accommodationService.getCompareRoom(roomid, checkindate, checkoutdate, people);

        return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
    }
}
