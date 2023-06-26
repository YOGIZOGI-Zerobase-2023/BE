package com.zerobase.yogizogi.review.service;

import com.zerobase.yogizogi.accommodation.domain.entity.Accommodation;
import com.zerobase.yogizogi.accommodation.repository.AccommodationRepository;
import com.zerobase.yogizogi.book.domain.entity.Book;
import com.zerobase.yogizogi.book.repository.BookRepository;
import com.zerobase.yogizogi.global.exception.CustomException;
import com.zerobase.yogizogi.global.exception.ErrorCode;
import com.zerobase.yogizogi.review.domain.entity.Review;
import com.zerobase.yogizogi.review.domain.model.ReviewForm;
import com.zerobase.yogizogi.review.domain.model.ReviewUpdateForm;
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
    private final AccommodationRepository accommodationRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public Page<Review> reviewList(Long accommodationId, Pageable pageable) {
        if (!accommodationRepository.existsById(accommodationId)) {
            throw new CustomException(ErrorCode.NOT_FOUND_ACCOMMODATION);
        }
        return reviewRepository.findAllByAccommodation_Id(accommodationId, pageable);
    }

    public void makeReview(Long accommodationId, String token, ReviewForm reviewForm) {
        if (provider.validateToken(token)) {
            throw new CustomException(ErrorCode.DO_NOT_ALLOW_TOKEN);
        }

        if (reviewForm.getRate() < 0 || reviewForm.getRate() > 10) {
            throw new CustomException(ErrorCode.NOT_CORRECT_RANGE);
        }

        UserDto userDto = provider.getUserDto(token);
        AppUser user = userRepository.findById(userDto.getId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        Book book = bookRepository.findById(reviewForm.getBookId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_BOOK));
        //리뷰 작성 가능 여부 확인
        if (book.getReviewRegistered()) {
            throw new CustomException(ErrorCode.AlREADY_REGISTER_REVIEW);
        }
        Accommodation accommodation = accommodationRepository.findById(accommodationId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ACCOMMODATION));

        Review review = reviewRepository.save(Review.builder().user(user)
            .accommodation(book.getRoom().getAccommodation())
            .rate(reviewForm.getRate())
            .description(reviewForm.getDescription()).build());

        // 숙소 평점 업데이트
        accommodation.getReviews().add(review);
        accommodation.updateScore(accommodation.getRate());
        accommodationRepository.save(accommodation);

        // review 했다고 업데이트
        book.setReviewRegistered(true);
        bookRepository.save(book);
    }

    public void updateReview(Long accommodationId, Long reviewId, String token,
        ReviewUpdateForm reviewForm) {
        if (provider.validateToken(token)) {
            throw new CustomException(ErrorCode.DO_NOT_ALLOW_TOKEN);
        }
        UserDto userDto = provider.getUserDto(token);
        AppUser user = userRepository.findById(userDto.getId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REVIEW));
        //점수 업데이트도 필요 할 수 있음.
        Accommodation accommodation = accommodationRepository.findById(accommodationId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ACCOMMODATION));

        if (!Objects.equals(accommodationId, review.getAccommodation().getId())) {
            throw new CustomException(ErrorCode.NOT_ALLOW_ACCESS);
        }

        if (!Objects.equals(review.getUser().getId(), user.getId())) {
            throw new CustomException(ErrorCode.NOT_ALLOW_ACCESS);
        }

        if (reviewForm.getRate() != null && (reviewForm.getRate() < 0
            || reviewForm.getRate() > 10)) {
            throw new CustomException(ErrorCode.NOT_CORRECT_RANGE);
        }
        int beforeRate = -1;
        if (reviewForm.getRate() != null && !Objects.equals(review.getRate(),
            reviewForm.getRate())) {
            beforeRate = review.getRate();
            review.setRate(reviewForm.getRate());
        }

        if (reviewForm.getDescription() != null && !Objects.equals(review.getDescription(),
            reviewForm.getDescription())) {
            review.setDescription(reviewForm.getDescription());
        }

        reviewRepository.save(review);

        if (beforeRate != -1) {
            accommodation.updateScore(accommodation.getRate());
            accommodationRepository.save(accommodation);
        }

    }

    public void deleteReview(Long accommodationId, String token, Long reviewId) {
        if (provider.validateToken(token)) {
            throw new CustomException(ErrorCode.DO_NOT_ALLOW_TOKEN);
        }
        UserDto userDto = provider.getUserDto(token);
        AppUser user = userRepository.findById(userDto.getId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_REVIEW));

        if (!Objects.equals(accommodationId, review.getAccommodation().getId())) {
            throw new CustomException(ErrorCode.NOT_ALLOW_ACCESS);
        }

        if (!Objects.equals(review.getUser().getId(), user.getId())) {
            throw new CustomException(ErrorCode.NOT_ALLOW_ACCESS);
        }

        //TODO 리뷰 삭제 시 평점 변경.
        reviewRepository.delete(review);
    }
}
