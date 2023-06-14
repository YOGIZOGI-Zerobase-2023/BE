package com.zerobase.yogizogi.book.dto;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class BookDtoTest {

    @Test
    @DisplayName("예약Dto의 모든 데이터의 값이 잘 들어가는지 테스트")
    void getAllBookDto() {

        //given
        BookDto bookDto = BookDto.builder().startDate(LocalDate.now())
            .endDate(LocalDate.now().plusDays(1))
            .people(4).payAmount(10000).build();
        //when

        //then<- format 관련 논의 필요.
        assertAll("예약Dto 검증",
            () -> assertEquals(LocalDate.now(), bookDto.getStartDate()),
            () -> assertEquals(LocalDate.now().plusDays(1), bookDto.getEndDate()),
            () -> assertEquals(4, bookDto.getPeople()),
            () -> assertEquals(10000, bookDto.getPayAmount())
        );
    }
}