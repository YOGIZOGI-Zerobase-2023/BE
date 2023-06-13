package com.zerobase.yogizogi.book.domain.entity;


import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BookTest {
    @Test
    @DisplayName("예약 엔터티의 모든 값이 제대로 생성되는지 테스트")

    void createBook(){
        //given
        Book book = Book.builder().bookName("홍길동").reviewRegistered(false).userId(3L).roomId(2L)
            .startDate(LocalDate.now()).endDate(LocalDate.now().plusDays(1))
            .payAmount(1000).people(4).accommodationId(1L).build();
        //when
        // then
        assertEquals("홍길동", book.getBookName());
        assertEquals(false, book.isReviewRegistered());//intellij가 바꾸려고 함...
        assertEquals(3L, book.getUserId());
        assertEquals(2L, book.getRoomId());
        assertEquals(LocalDate.now(),book.getStartDate());
        assertEquals(LocalDate.now().plusDays(1),book.getEndDate());
        assertEquals(1000, book.getPayAmount());
        assertEquals(4, book.getPeople());
        assertEquals(1L, book.getAccommodationId());
    }
}