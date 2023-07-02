package com.zerobase.yogizogi.book.service;

import com.zerobase.yogizogi.accommodation.domain.entity.Accommodation;
import com.zerobase.yogizogi.accommodation.domain.entity.Price;
import com.zerobase.yogizogi.accommodation.domain.entity.Room;
import com.zerobase.yogizogi.accommodation.repository.AccommodationRepository;
import com.zerobase.yogizogi.accommodation.repository.PriceRepository;
import com.zerobase.yogizogi.accommodation.repository.RoomRepository;
import com.zerobase.yogizogi.book.domain.entity.Book;
import com.zerobase.yogizogi.book.domain.model.BookForm;
import com.zerobase.yogizogi.book.repository.BookRepository;
import com.zerobase.yogizogi.global.exception.CustomException;
import com.zerobase.yogizogi.user.domain.entity.AppUser;
import com.zerobase.yogizogi.user.dto.UserDto;
import com.zerobase.yogizogi.user.repository.UserRepository;
import com.zerobase.yogizogi.user.token.JwtAuthenticationProvider;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class BookServiceTest {

    @InjectMocks
    private BookService bookService;

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

    @Mock
    private AccommodationRepository accommodationRepository;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testMyBookList() {

        String token = "token";
        when(provider.validateToken(token)).thenReturn(false);
        UserDto userDto = new UserDto(1L, "test@gmail.com");
        when(provider.getUserDto(token)).thenReturn(userDto);
        AppUser user = new AppUser();
        user.setId(userDto.getId());
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(user));
        bookService.myBookList(userDto.getId(), token);
        verify(bookRepository).findAllByUser_Id(userDto.getId());
    }

    @Test
    public void testMakeBook() {
        LocalDate now = LocalDate.now();
        String token = "token";
        BookForm bookForm = new BookForm();
        bookForm.setAccommodationId(1L);
        bookForm.setRoomId(1L);
        bookForm.setCheckInDate(now);
        bookForm.setCheckOutDate(now.plusDays(1));
        when(provider.validateToken(token)).thenReturn(false);
        UserDto userDto = new UserDto(1L, "test@gmail.com");
        when(provider.getUserDto(token)).thenReturn(userDto);
        AppUser user = new AppUser();
        user.setId(1L);
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(user));
        Accommodation accommodation = new Accommodation();
        accommodation.setId(bookForm.getAccommodationId());
        when(accommodationRepository.findById(bookForm.getAccommodationId())).thenReturn(
            Optional.of(accommodation));
        Room room = new Room();
        room.setId(bookForm.getRoomId());
        when(roomRepository.findById(bookForm.getRoomId())).thenReturn(Optional.of(room));
        assertThrows(CustomException.class, () -> bookService.makeBook(token, bookForm));
    }
    @Test
    public void testDeleteBook() {
        LocalDate now = LocalDate.now();
        String token = "token";
        Long userId = 1L;
        Long bookId = 1L;
        when(provider.validateToken(token)).thenReturn(false);
        UserDto userDto = new UserDto(userId, "test@gmail.com");
        when(provider.getUserDto(token)).thenReturn(userDto);
        AppUser user = new AppUser();
        user.setId(userId);
        when(userRepository.findById(userDto.getId())).thenReturn(Optional.of(user));
        Book book = new Book();
        book.setId(bookId);
        book.setUser(user);
        book.setCheckInDate(now);
        book.setCheckOutDate(now.plusDays(1));

        Room room = new Room();
        room.setId(1L);
        room.setName("Test Room");
        room.setCheckInTime("12:00");
        room.setCheekOutTime("11:00");
        room.setDefaultPeople(2);
        room.setMaxPeople(4);

        Accommodation accommodation = new Accommodation();
        accommodation.setId(1L);
        room.setAccommodation(accommodation);


        Set<Price> prices = new HashSet<>();
        room.setPrices(prices);

        book.setRoom(room);

        Price price = new Price();
        price.setRoomCnt(1);
        when(priceRepository.findAllByRoom_IdAndDate(anyLong(), any(LocalDate.class))).thenReturn(price);

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        assertThrows(CustomException.class, () -> bookService.deleteBook(token, userId, bookId));
    }


}