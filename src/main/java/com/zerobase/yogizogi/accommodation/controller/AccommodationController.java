package com.zerobase.yogizogi.accommodation.controller;

import com.zerobase.yogizogi.accommodation.domain.model.AccommodationForm;
import com.zerobase.yogizogi.accommodation.service.AccommodationService;
import com.zerobase.yogizogi.review.domain.model.ReviewForm;
import com.zerobase.yogizogi.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accommodations")
@RequiredArgsConstructor
public class AccommodationController {
    private final String TOKEN = "X-AUTH-TOKEN";
    private final AccommodationService accommodationService;
    //숙소 리스트 검색<- 엘라스틱 서치로 빌드해야 함.
    @PostMapping()
    public ResponseEntity<?> makeReview(@RequestHeader(name = TOKEN) String token,
        @RequestBody AccommodationForm accommodationForm) {
        return ResponseEntity.ok(accommodationService.makeAccommodation(token,accommodationForm));
    }
}
