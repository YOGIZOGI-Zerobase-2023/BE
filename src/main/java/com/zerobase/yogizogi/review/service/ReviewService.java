package com.zerobase.yogizogi.review.service;

import com.zerobase.yogizogi.book.domain.entity.Book;
import com.zerobase.yogizogi.book.repository.BookRepository;
import com.zerobase.yogizogi.global.exception.CustomException;
import com.zerobase.yogizogi.global.exception.ErrorCode;
import com.zerobase.yogizogi.review.domain.entity.Review;
import com.zerobase.yogizogi.review.domain.model.ReviewForm;
import com.zerobase.yogizogi.review.repository.ReviewRepository;
import com.zerobase.yogizogi.user.domain.entity.AppUser;
import com.zerobase.yogizogi.user.dto.UserDto;
import com.zerobase.yogizogi.user.repository.UserRepository;
import com.zerobase.yogizogi.user.token.JwtAuthenticationProvider;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final JwtAuthenticationProvider provider;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    public Page<Review> reviewList(Long accommodationId, Pageable pageable) {

        if(reviewRepository.findFirstByAccommodationId(accommodationId).isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND_ACCOMMODATION);
        }
        return  reviewRepository.findAllByAccommodationId(accommodationId, pageable);
    }

    public String makeReview(String token,ReviewForm reviewForm) {
        if(!provider.validateToken(token)){
            throw new CustomException(ErrorCode.DO_NOT_ALLOW_TOKEN);
        }

        if(reviewForm.getRate() < 0 || reviewForm.getRate() > 10){
            throw new CustomException(ErrorCode.NOT_CORRECT_RANGE);
        }
        UserDto userDto = provider.getUserDto(token);
        AppUser user = userRepository.findById(userDto.getId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        Book book = bookRepository.findById(reviewForm.getBookId())
            .orElseThrow(()-> new CustomException(ErrorCode.NOT_FOUND_BOOK));

        if(book.isReviewRegistered()){
            throw new CustomException(ErrorCode.AlREADY_REGISTER_REVIEW);
        }

        book.setReviewRegistered(true);

        bookRepository.save(book);
        //예약 단계로 접어들며 한 번 더 예약 가능한지의 확인을 진행** 해당 숙소가 해당 기간 동안에 예약이 가능한지로 검색할 것**
        reviewRepository.save(Review.builder().userId(user.getId())
            .accommodationId(book.getAccommodationId())
            .rate(reviewForm.getRate())
            .contents(reviewForm.getContents()).build());
        return "/success";
    }

    public String deleteReview(String token, Long reviewId) {
        if (!provider.validateToken(token)) {
            throw new CustomException(ErrorCode.DO_NOT_ALLOW_TOKEN);
        }
        UserDto userDto = provider.getUserDto(token);
        AppUser user = userRepository.findById(userDto.getId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REVIEW));

        if (!Objects.equals(review.getUserId(), user.getId())) {
            throw new CustomException(ErrorCode.NOT_ALLOW_DELETE);
        }

        reviewRepository.delete(review);

        return "/delete/success";
    }
}
