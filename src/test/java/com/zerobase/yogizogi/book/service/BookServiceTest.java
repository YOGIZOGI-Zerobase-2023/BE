package com.zerobase.yogizogi.book.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.zerobase.yogizogi.book.domain.entity.Book;
import com.zerobase.yogizogi.book.domain.model.BookForm;
import com.zerobase.yogizogi.book.repository.BookRepository;
import com.zerobase.yogizogi.user.domain.entity.AppUser;
import com.zerobase.yogizogi.user.dto.UserDto;
import com.zerobase.yogizogi.user.repository.UserRepository;
import com.zerobase.yogizogi.user.token.JwtAuthenticationProvider;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;

public class BookServiceTest {

    @Mock
    private JwtAuthenticationProvider provider;
    @Mock
    private BookRepository bookRepository;
    @Mock
    private UserRepository userRepository;
    private BookService bookService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        bookService = new BookService(provider, bookRepository, userRepository);
    }

    @Test
    @DisplayName("사용자의 책 목록 조회 테스트")
    public void testMyBookList() {
        //given
        String token = "testToken";
        Long userId = 1L;
        Pageable pageable = PageRequest.of(0, 3, Sort.by(Direction.DESC, "id"));

        // Create mock reservations
        Book book1 = Mockito.mock(Book.class);
        Book book2 = Mockito.mock(Book.class);
        Book book3 = Mockito.mock(Book.class);

        // Create a list of mock reservations
        List<Book> mockBooks = new ArrayList<>();
        mockBooks.add(book1);
        mockBooks.add(book2);
        mockBooks.add(book3);

        Page<Book> expectedBooks = new PageImpl<>(mockBooks);

        when(provider.validateToken(token)).thenReturn(true);
        when(provider.getUserDto(token)).thenReturn(new UserDto(userId, "test@test.com"));
        when(userRepository.findById(userId)).thenReturn(Optional.of(new AppUser()));
        when(bookRepository.findAllByUserId(userId, pageable)).thenReturn(expectedBooks);

        //when
        Page<Book> actualBooks = bookService.myBookList(token, pageable);

        //then
        assertEquals(expectedBooks, actualBooks);
    }

    @Test
    @DisplayName("책 예약 생성 테스트")
    public void testMakeBook() {
        //given
        String token = "testToken";
        Long userId = 1L;
        BookForm bookForm = new BookForm();
        when(provider.validateToken(token)).thenReturn(true);
        when(provider.getUserDto(token)).thenReturn(new UserDto(userId, "test@test.com"));
        when(userRepository.findById(userId)).thenReturn(Optional.of(new AppUser()));

        //when
        String result = bookService.makeBook(token, bookForm);

        //then
        assertEquals("/success", result);
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    @DisplayName("책 예약 삭제 테스트")
    public void testDeleteBook() {
        //given
        String token = "testToken";
        Long userId = 1L;
        Long bookId = 1L;
        when(provider.validateToken(token)).thenReturn(true);
        when(provider.getUserDto(token)).thenReturn(new UserDto(userId, "test@test.com"));
        when(userRepository.findById(userId)).thenReturn(Optional.of(new AppUser()));
        when(bookRepository.findById(bookId)).thenReturn(Optional.of(new Book()));

        //when
        String result = bookService.deleteBook(token, bookId);

        //then
        assertEquals("delete/success", result);
        verify(bookRepository, times(1)).delete(any(Book.class));
    }
}
