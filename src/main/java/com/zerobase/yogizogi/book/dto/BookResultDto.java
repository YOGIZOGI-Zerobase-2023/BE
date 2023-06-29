package com.zerobase.yogizogi.book.dto;

import com.zerobase.yogizogi.book.domain.entity.Book;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class BookResultDto {
    Long id;
    Long userId;
    Long accommodationId;
    String bookName;
    String accommodationName;
    String picUrl;
    LocalDate checkInDate;
    LocalDate checkOutDate;
    Integer price;
    Double rate;
    Boolean reviewRegistered;
    //정적 팩터리 메서드
    public static BookResultDto from(Book book){
        return BookResultDto.builder()
            .id(book.getId())
            .userId(book.getUser().getId())
            .accommodationName(book.getAccommodation().getName())
            .accommodationId(book.getAccommodation().getId())
            .bookName(book.getBookName())
            .price(book.getPayAmount())
            .rate(book.getAccommodation().getRate())
            .checkInDate(book.getCheckInDate())
            .checkOutDate(book.getCheckOutDate())
            .picUrl(book.getAccommodation().getPicUrl())
            .reviewRegistered(book.getReviewRegistered())
            .build();
    }
}
