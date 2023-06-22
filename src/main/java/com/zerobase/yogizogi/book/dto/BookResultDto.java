package com.zerobase.yogizogi.book.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BookResultDto {
    Long id;
    Long userId;
    Long accommodationId;
    String bookName;
    String picUrl;
    LocalDate checkInDate;
    LocalDate checkOutDate;
    Integer price;
    Double score;
    Boolean reviewRegistered;
}
