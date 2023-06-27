package com.zerobase.yogizogi.accommodation.controller;


import com.zerobase.yogizogi.accommodation.service.AccommodationService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accommodations")
@RequiredArgsConstructor
public class AccommodationController {

    private final AccommodationService accommodationService;

    // 숙소 리스트 조회

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam(required = false) String keyword,
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

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{accommodationId}/")
    public ResponseEntity<?> getAccommodationDetail(@PathVariable Long accommodationId,
        @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate checkindate,
        @RequestParam @DateTimeFormat(iso = ISO.DATE) LocalDate checkoutdaet,
        @RequestParam Integer people) {
        var result = accommodationService.getAccommodationDetail(accommodationId, checkindate,
            checkoutdaet, people);
        return ResponseEntity.ok(result);
    }
}
