package com.zerobase.yogizogi.book.domain.model;
import com.zerobase.yogizogi.user.domain.entity.AppUser;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private LocalDate startDate;
    private LocalDate endDate;
    private int people;
    private int payAmount;
    private String bookName;
}