package com.zerobase.yogizogi.book.domain.entity;


import static org.junit.jupiter.api.Assertions.assertEquals;

import com.zerobase.yogizogi.room.domain.entity.Room;
import com.zerobase.yogizogi.user.domain.entity.AppUser;
import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BookTest {

    @Test
    @DisplayName("예약 엔터티의 모든 값이 제대로 생성되는지 테스트")
    void createBook() {
        AppUser appUser = new AppUser();
        appUser.setId(3L);
        Room room = new Room();
        room.setId(2L);
        //given
        Book book = Book.builder().bookName("홍길동").reviewRegistered(false).user(appUser).room(room)
            .startDate(LocalDate.now()).endDate(LocalDate.now().plusDays(1))
            .payAmount(1000).people(4).build();
        //when
        // then
        assertEquals("홍길동", book.getBookName());
        assertEquals(false, book.isReviewRegistered());
        assertEquals(3L, book.getUser().getId());
        assertEquals(2L, book.getRoom().getId());
        assertEquals(LocalDate.now(), book.getStartDate());
        assertEquals(LocalDate.now().plusDays(1), book.getEndDate());
        assertEquals(1000, book.getPayAmount());
        assertEquals(4, book.getPeople());
    }
}