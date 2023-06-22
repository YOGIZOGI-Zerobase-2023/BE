package com.zerobase.yogizogi.book.domain.model;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookForm {
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private Integer people;
    private Long RoomId;
    private String bookName;
}
