//package com.zerobase.yogizogi.review.controller;
//
//import com.zerobase.yogizogi.review.domain.model.ReviewForm;
//import com.zerobase.yogizogi.review.service.ReviewService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Sort;
//import org.springframework.data.web.PageableDefault;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestHeader;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/reviews")
//public class ReviewController {
//
//    private final String TOKEN = "X-AUTH-TOKEN";
//    private final ReviewService reviewService;
//
//    @GetMapping()
//    public ResponseEntity<?> reviewsList(@RequestParam(name = "id", defaultValue = "1") Long accommodationId,
//        @PageableDefault(page = 0, size = 2,sort = "id", direction = Sort.Direction.DESC)
//        Pageable pageable){
//       return ResponseEntity.ok(reviewService.reviewList(accommodationId,pageable));
//    }
//
//    @PostMapping()
//    public ResponseEntity<?> makeReview(@RequestHeader(name = TOKEN) String token,
//        @RequestBody ReviewForm reviewForm) {
//        return ResponseEntity.ok(reviewService.makeReview(token,reviewForm));
//    }
//
//    @DeleteMapping("/{reviewId}")
//    public ResponseEntity<?> deleteReview(@RequestHeader(name = TOKEN) String token,
//        @PathVariable(name = "reviewId") Long reviewId) {
//        return ResponseEntity.ok().body(reviewService.deleteReview(token, reviewId));
//    }
//}
