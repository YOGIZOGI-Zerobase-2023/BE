package com.zerobase.yogizogi.review.controller;

import com.zerobase.yogizogi.review.domain.model.ReviewForm;
import com.zerobase.yogizogi.review.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accommodation/{accommodationId}/review")
public class ReviewController {

    private final String TOKEN = "X-AUTH-TOKEN";
    private final ReviewService reviewService;

    @GetMapping("")
    public ResponseEntity<?> reviewsList(
        @PathVariable(name = "accommodationId") Long accommodationId,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "pagesize", defaultValue = "2") int size,
        @RequestParam(name = "sort", defaultValue = "id,desc") String sort) {
        String[] sortProperties = sort.split(",");
        Sort.Direction direction = sortProperties[1].equalsIgnoreCase("desc")
            ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortProperties[0]));
        return ResponseEntity.ok(reviewService.reviewList(accommodationId, pageable));
    }

    @PostMapping("")
    public ResponseEntity<?> makeReview(
        @PathVariable(name = "accommodationId") Long accommodationId,
        @RequestHeader(name = TOKEN) String token,
        @RequestBody ReviewForm reviewForm) {
        return ResponseEntity.ok(reviewService.makeReview(accommodationId, token, reviewForm));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<?> deleteReview(
        @PathVariable(name = "accommodationId") Long accommodationId,
        @RequestHeader(name = TOKEN) String token,
        @PathVariable(name = "reviewId") Long reviewId) {
        return ResponseEntity.ok().body(reviewService.deleteReview(token, reviewId));
    }
}
