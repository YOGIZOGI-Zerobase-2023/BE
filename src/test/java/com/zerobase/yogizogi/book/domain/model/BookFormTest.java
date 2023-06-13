package com.zerobase.yogizogi.book.domain.model;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BookFormTest {
    @Test
    @DisplayName("예약 포맷 제작 테스트 코드")

    void makeBookForm(){
        //given
        BookForm bookForm = BookForm.builder().bookName("홍길동").startDate(LocalDate.now())
            .endDate(LocalDate.now().plusDays(1))
            .people(4)
            .payAmount(10000).build();

        //when
        //then
        assertEquals("홍길동",bookForm.getBookName());
        assertEquals(LocalDate.now(),bookForm.getStartDate());
        assertEquals(LocalDate.now().plusDays(1),bookForm.getEndDate());
        assertEquals(4,bookForm.getPeople());
        assertEquals(10000, bookForm.getPayAmount());

    }
}