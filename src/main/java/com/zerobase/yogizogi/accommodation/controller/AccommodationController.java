package com.zerobase.yogizogi.accommodation.controller;

import com.zerobase.yogizogi.accommodation.domain.model.AccommodationForm;
import com.zerobase.yogizogi.accommodation.service.AccommodationService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    return ResponseEntity.ok(accommodationService.makeAccommodation(token, accommodationForm));
  }

  // 숙소 리스트 조회
  @GetMapping("/")
  public ResponseEntity<?> autocomplete(@RequestParam Long id) {
    var result = accommodationService.getAccommodation(id);
    return ResponseEntity.ok(result);
  }
}
