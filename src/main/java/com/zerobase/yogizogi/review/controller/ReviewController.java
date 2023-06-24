package com.zerobase.yogizogi.review.controller;

import com.zerobase.yogizogi.global.ApiResponse;
import com.zerobase.yogizogi.global.ResponseCode;
import com.zerobase.yogizogi.review.domain.model.ReviewForm;
import com.zerobase.yogizogi.review.domain.model.ReviewUpdateForm;
import com.zerobase.yogizogi.review.dto.ReviewDto;
import com.zerobase.yogizogi.review.service.ReviewService;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
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
    public ResponseEntity<ApiResponse<Object>> reviewsList(
        @PathVariable(name = "accommodationId") Long accommodationId,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "pagesize", defaultValue = "20") int size) {//페이지 사이즈 20 정도로 수정

        //desc 외에는 필요가 없으므로, Default로 넘겨주는 아래의 형태로.
        Pageable pageable = PageRequest.of(page, size, Sort.by(Direction.DESC, "id"));
        Page<ReviewDto> result = reviewService.reviewList(accommodationId, pageable)
            .map(ReviewDto::from);
        return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(result).toEntity();
    }

    @PostMapping("")
    public ResponseEntity<ApiResponse<Object>> makeReview(
        @PathVariable(name = "accommodationId") Long accommodationId,
        @RequestHeader(name = TOKEN) String token,
        @RequestBody ReviewForm reviewForm) {
        reviewService.makeReview(accommodationId,token, reviewForm);
        Map<String, String> msg = new HashMap<>();
        msg.put("msg", "성공적으로 리뷰를 작성 했습니다.");
        return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(msg).toEntity();
    }
    @PatchMapping("{reviewId}")
    public ResponseEntity<ApiResponse<Object>> updateReview(
        @PathVariable(name = "accommodationId") Long accommodationId,
        @PathVariable(name = "reviewId") Long reviewId,
        @RequestHeader(name = TOKEN) String token,
        @RequestBody ReviewUpdateForm reviewForm) {
        reviewService.updateReview(accommodationId,reviewId,token, reviewForm);
        Map<String, String> msg = new HashMap<>();
        msg.put("msg", "성공적으로 작업을 수행 했습니다.");
        return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(msg).toEntity();
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<ApiResponse<Object>> deleteReview(
        @PathVariable(name = "accommodationId") Long accommodationId,
        @RequestHeader(name = TOKEN) String token,
        @PathVariable(name = "reviewId") Long reviewId) {
        reviewService.deleteReview(accommodationId, token, reviewId);
        Map<String, String> msg = new HashMap<>();
        msg.put("msg", "성공적으로 작업을 수행 했습니다.");
        return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(msg).toEntity();
    }
}
