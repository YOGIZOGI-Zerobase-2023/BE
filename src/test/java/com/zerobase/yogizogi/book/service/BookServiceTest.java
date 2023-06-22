package com.zerobase.yogizogi.book.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import com.zerobase.yogizogi.accommodation.repository.PriceRepository;
import com.zerobase.yogizogi.accommodation.repository.RoomRepository;
import com.zerobase.yogizogi.book.domain.entity.Book;
import com.zerobase.yogizogi.book.domain.model.BookForm;
import com.zerobase.yogizogi.book.repository.BookRepository;
import com.zerobase.yogizogi.user.domain.entity.AppUser;
import com.zerobase.yogizogi.user.dto.UserDto;
import com.zerobase.yogizogi.user.repository.UserRepository;
import com.zerobase.yogizogi.user.token.JwtAuthenticationProvider;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public class BookServiceTest {

    @Mock
    private JwtAuthenticationProvider provider;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RoomRepository roomRepository;
    @Mock
    private PriceRepository priceRepository;
    private BookService bookService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        bookService = new BookService(provider, bookRepository, userRepository,roomRepository,priceRepository);
    }

    @Test
    @DisplayName("사용자의 예약 페이지 조회 테스트")
    public void testMyBookList() {
        // given
        UserDto userDto = new UserDto();
        userDto.setId(1L);
        String token = "valid_token";

        when(provider.validateToken(token)).thenReturn(true);
        when(provider.getUserDto(token)).thenReturn(userDto);

        AppUser user = new AppUser();
        user.setId(1L);
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(user));

        Pageable pageable = mock(Pageable.class);
        Page<Book> expectedPage = mock(Page.class);//<-이 부분이 주요*
        when(bookRepository.findAllByUserId(user.getId(), pageable)).thenReturn(expectedPage);

        // when
        Page<Book> result = bookService.myBookList(token, pageable);

        // then
        verify(provider).validateToken(token);
        verify(provider).getUserDto(token);
        verify(userRepository).findById(userDto.getId());
        verify(bookRepository).findAllByUserId(user.getId(), pageable);
        verifyNoMoreInteractions(provider, userRepository, bookRepository);

        assertEquals(expectedPage, result);
    }


    @Test
    @DisplayName("예약 생성 테스트")
    public void testMakeBook() {
        //given
        String token = "testToken";
        Long userId = 1L;
        BookForm bookForm = new BookForm();
        when(provider.validateToken(token)).thenReturn(true);
        when(provider.getUserDto(token)).thenReturn(new UserDto(userId, "test@test.com"));
        when(userRepository.findById(userId)).thenReturn(Optional.of(new AppUser()));

        //when //null발생 에러 가능성
        String result = bookService.makeBook(token, bookForm);

        //then
        assertEquals("/success", result);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    @DisplayName("예약 삭제 테스트")
    public void testDeleteBook() {
        //given
        String token = "testToken";
        Long userId = 1L;
        Long bookId = 1L;
        when(provider.validateToken(token)).thenReturn(true);
        when(provider.getUserDto(token)).thenReturn(new UserDto(userId, "test@test.com"));
        when(userRepository.findById(userId)).thenReturn(Optional.of(new AppUser()));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(new Book()));

        //when //null발생 에러 가능성
        String result = bookService.deleteBook(token, bookId);

        //then
        assertEquals("delete/success", result);
        verify(bookRepository, times(1)).delete(any(Book.class));
    }
}
