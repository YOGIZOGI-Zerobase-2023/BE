package com.zerobase.yogizogi.review.service;

import com.zerobase.yogizogi.accommodation.domain.entity.Accommodation;
import com.zerobase.yogizogi.accommodation.repository.AccommodationRepository;
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
    private final AccommodationRepository accommodationRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public Page<?> reviewList(Long accommodationId, Pageable pageable) {
        Accommodation accommodation = accommodationRepository.findById(accommodationId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ACCOMMODATION));
        Page<Review> page = reviewRepository.findAllByAccommodation(accommodation, pageable);

        return page.map(ReviewDto::new);
    }

    public String makeReview(Long accommodationId, String token, ReviewForm reviewForm) {
        if (!provider.validateToken(token)) {
            throw new CustomException(ErrorCode.DO_NOT_ALLOW_TOKEN);
        }

        if (reviewForm.getScore() < 0 || reviewForm.getScore() > 10) {
            throw new CustomException(ErrorCode.NOT_CORRECT_RANGE);
        }

        UserDto userDto = provider.getUserDto(token);
        AppUser user = userRepository.findById(userDto.getId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        Book book = bookRepository.findById(reviewForm.getBookId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_BOOK));

        if (book.getReviewRegistered()) {
            throw new CustomException(ErrorCode.AlREADY_REGISTER_REVIEW);
        }
        Accommodation accommodation = accommodationRepository.findById(accommodationId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ACCOMMODATION));

        Review review = reviewRepository.save(Review.builder().user(user)
            .accommodation(book.getRoom().getAccommodation())
            .score(reviewForm.getScore())
            .contents(reviewForm.getContents()).build());

        // 숙소 평점 업데이트
        accommodation.getReviews().add(review);
        accommodation.updateScore(accommodation.getScore());
        accommodationRepository.save(accommodation);

        // review 했다고 업데이트
        book.setReviewRegistered(true);
        bookRepository.save(book);

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

        if (!Objects.equals(review.getUser().getId(), user.getId())) {
            throw new CustomException(ErrorCode.NOT_ALLOW_DELETE);
        }

        reviewRepository.delete(review);

        return "/delete/success";
    }
}
