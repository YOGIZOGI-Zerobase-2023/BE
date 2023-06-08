package com.zerobase.yogizogi.book.dto;

import com.zerobase.yogizogi.book.domain.entity.Book;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class BookDto {
    private Long id;//bookId
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    private int people;
    private int payAmount;
    private boolean reviewRegistered;
    public static BookDto fromBook(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setStartDate(book.getStartDate());
        bookDto.setEndDate(book.getEndDate());
        bookDto.setPeople(book.getPeople());
        bookDto.setPayAmount(book.getPayAmount());
        bookDto.setReviewRegistered(book.isReviewRegistered());
        return bookDto;
    }
}
