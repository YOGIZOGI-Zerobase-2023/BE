package com.zerobase.yogizogi.review.service;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.zerobase.yogizogi.accommodation.domain.entity.Accommodation;
import com.zerobase.yogizogi.accommodation.domain.entity.Room;
import com.zerobase.yogizogi.accommodation.repository.AccommodationRepository;
import com.zerobase.yogizogi.book.domain.entity.Book;
import com.zerobase.yogizogi.book.repository.BookRepository;
import com.zerobase.yogizogi.global.exception.CustomException;
import com.zerobase.yogizogi.review.domain.entity.Review;
import com.zerobase.yogizogi.review.domain.model.ReviewForm;
import com.zerobase.yogizogi.review.domain.model.ReviewUpdateForm;
import com.zerobase.yogizogi.review.repository.ReviewRepository;
import com.zerobase.yogizogi.user.domain.entity.AppUser;
import com.zerobase.yogizogi.user.dto.UserDto;
import com.zerobase.yogizogi.user.repository.UserRepository;
import com.zerobase.yogizogi.user.token.JwtAuthenticationProvider;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

class ReviewServiceTest {

    @Mock
    private JwtAuthenticationProvider provider;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private AccommodationRepository accommodationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private BookRepository bookRepository;

    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        reviewService = new ReviewService(provider, reviewRepository, accommodationRepository,
            userRepository, bookRepository);
    }

    @Test
    @DisplayName("리뷰리스트, 숙소가 존재하면, Page<Review> 반환")
    public void reviewListTest() {
        // given
        Long accommodationId = 1L;
        Pageable pageable = Pageable.unpaged();

        when(accommodationRepository.existsById(accommodationId)).thenReturn(true);
        when(reviewRepository.findAllByAccommodation_Id(accommodationId, pageable))
            .thenReturn(new PageImpl<>(new ArrayList<>()));

        // when
        Page<Review> result = reviewService.reviewList(accommodationId, pageable);

        // then
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        verify(accommodationRepository, times(1)).existsById(accommodationId);
        verify(reviewRepository, times(1)).findAllByAccommodation_Id(accommodationId, pageable);
    }

    @Test
    @DisplayName("리뷰리스트, 숙소 존재 하지 않으면 예외")
    public void reviewListTest_NotAccommodation_ThrowCustomException() {
        // given
        Long accommodationId = 1L;
        Pageable pageable = Pageable.unpaged();
        // when
        when(accommodationRepository.existsById(accommodationId)).thenReturn(false);

        // then
        assertThrows(CustomException.class,
            () -> reviewService.reviewList(accommodationId, pageable));
        verify(accommodationRepository, times(1)).existsById(accommodationId);
        verify(reviewRepository, never()).findAllByAccommodation_Id(anyLong(), any(Pageable.class));
    }

    @Test
    @DisplayName("리뷰 만들기")
    public void makeReviewTest() {
        // given
        Long accommodationId = 1L;
        String token = "validToken";
        ReviewForm reviewForm = new ReviewForm(1L, 5, "great");

        UserDto userDto = new UserDto(1L, "test");

        AppUser user = new AppUser();
        user.setId(1L);

        Book book = new Book();
        book.setCheckOutDate(LocalDate.now().minusDays(1));
        book.setReviewRegistered(false);

        Room room = new Room();
        Accommodation accommodation = new Accommodation();
        accommodation.setReviews(new HashSet<>());
        room.setAccommodation(accommodation);
        book.setRoom(room);

        when(provider.validateToken(token)).thenReturn(false);
        when(provider.getUserDto(token)).thenReturn(userDto);
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(user));
        when(bookRepository.findById(reviewForm.getBookId())).thenReturn(Optional.of(book));
        when(accommodationRepository.findById(accommodationId)).thenReturn(
            Optional.of(accommodation));
        when(reviewRepository.save(any(Review.class))).thenAnswer(invocation -> {
            Review savedReview = invocation.getArgument(0);
            accommodation.getReviews().add(savedReview);
            accommodation.updateScore();
            return savedReview;
        });

        // when
        reviewService.makeReview(accommodationId, token, reviewForm);

        // then
        verify(reviewRepository, times(1)).save(any());
        verify(bookRepository, times(1)).save(any());

        assertNotNull(accommodation.getReviews());
        assertFalse(accommodation.getReviews().isEmpty());
        assertEquals(1, accommodation.getReviews().size());
    }


    @Test
    @DisplayName("리뷰 업데이트")
    public void updateReviewTest() {
        //given
        Long accommodationId = 1L;
        Long reviewId = 1L;
        String token = "validToken";
        ReviewUpdateForm reviewForm = new ReviewUpdateForm(10, "good");

        AppUser user = new AppUser();
        user.setId(1L);

        Book book = new Book();
        book.setCheckOutDate(LocalDate.now().minusDays(1));
        book.setReviewRegistered(false);

        Room room = new Room();
        Accommodation accommodation = Accommodation.builder().id(accommodationId).build();
        accommodation.setReviews(new HashSet<>());
        room.setAccommodation(accommodation);
        book.setRoom(room);
        Review review = Review.builder().id(reviewId).user(user).rate(8).description("test")
            .accommodation(accommodation).build();

        when(provider.validateToken(token)).thenReturn(false);
        when(provider.getUserDto(token)).thenReturn(new UserDto(1L, "test@test.com"));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(accommodationRepository.findById(accommodationId)).thenReturn(Optional.of(accommodation));

        // when
        reviewService.updateReview(accommodationId, reviewId, token, reviewForm);

        // then
        verify(userRepository).findById(1L);
        verify(reviewRepository).findById(1L);
        verify(accommodationRepository).findById(1L);
        verify(reviewRepository).save(any(Review.class));
        verify(accommodationRepository).save(any(Accommodation.class));
    }

    @Test
    @DisplayName("리뷰 삭제")
    public void reviewDeleteTest() {
        // given
        Long accommodationId = 1L;
        Long reviewId = 1L;
        String token = "validToken";
        UserDto userDto = new UserDto(1L, "test");
        AppUser user = AppUser.builder().id(userDto.getId()).email(userDto.getEmail()).build();
        Accommodation accommodation = Accommodation.builder().id(accommodationId).build();
        Review review = Review.builder().id(reviewId).user(user).accommodation(accommodation).build();

        when(provider.validateToken(token)).thenReturn(false);
        when(provider.getUserDto(token)).thenReturn(userDto);
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(user));
        when(reviewRepository.findById(reviewId)).thenReturn(Optional.of(review));
        when(accommodationRepository.findById(accommodationId)).thenReturn(
            Optional.of(accommodation));

        // when
        assertDoesNotThrow(() -> reviewService.deleteReview(accommodationId, token, reviewId));

        // then
        verify(provider, times(1)).validateToken(token);
        verify(provider, times(1)).getUserDto(token);
        verify(userRepository, times(1)).findById(userDto.getId());
        verify(reviewRepository, times(1)).findById(reviewId);
        verify(accommodationRepository, times(1)).findById(accommodationId);
        verify(reviewRepository, times(1)).delete(review);
    }

    @Test
    @DisplayName("리뷰, 토큰 불완전하면 예외")
    void testReview_InvalidToken_ThrowCustomException() {
        // given
        Long accommodationId = 1L;
        String token = "invalidToken";
        ReviewForm reviewForm = new ReviewForm(1L, 8, "good");
        // when
        when(provider.validateToken(token)).thenReturn(true);

        // then
        assertThrows(CustomException.class,
            () -> reviewService.makeReview(accommodationId, token, reviewForm));
        verify(provider, times(1)).validateToken(token);
        verify(provider, never()).getUserDto(token);
        verify(userRepository, never()).findById(anyLong());
        verify(bookRepository, never()).findById(anyLong());
        verify(accommodationRepository, never()).findById(anyLong());
        verify(reviewRepository, never()).save(any(Review.class));
    }
}
