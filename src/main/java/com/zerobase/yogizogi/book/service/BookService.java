package com.zerobase.yogizogi.book.service;

import com.zerobase.yogizogi.book.domain.entity.Book;
import com.zerobase.yogizogi.book.domain.model.BookForm;
import com.zerobase.yogizogi.book.repository.BookRepository;
import com.zerobase.yogizogi.global.exception.CustomException;
import com.zerobase.yogizogi.global.exception.ErrorCode;
import com.zerobase.yogizogi.user.domain.entity.AppUser;
import com.zerobase.yogizogi.user.dto.UserDto;
import com.zerobase.yogizogi.user.repository.UserRepository;
import com.zerobase.yogizogi.user.token.JwtAuthenticationProvider;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {
    private final JwtAuthenticationProvider provider;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    //private final RoomRepository roomRepository;

    //현재는 등록 없이 사용을 위해 roomId를 가져오지 않지만 이후에는 roomId를 필수적으로 받을 것
    public String makeBook(String token, BookForm bookForm) {
        if(!provider.validateToken(token)){
            throw new CustomException(ErrorCode.DO_NOT_ALLOW_TOKEN);
        }
        AppUser user = userRepository.findById(bookForm.getUserId())
                .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        //예약 단계로 접어들며 한 번 더 예약 가능한지의 확인을 진행** 해당 숙소가 해당 기간 동안에 예약이 가능한지로 검색할 것**
        //날짜 포맷 프론트와 함께 양식을 맞춰서 만들 것**;

        bookRepository.save(Book.builder().user(user)
            .startDate(bookForm.getStartDate())
            .endDate(bookForm.getEndDate())
            .people(bookForm.getPeople()).payAmount(bookForm.getPayAmount())
            .build());

        return "book/success";
    }

    public String deleteBook(String token, Long bookId) {
        if (!provider.validateToken(token)) {
            throw new CustomException(ErrorCode.DO_NOT_ALLOW_TOKEN);
        }

        UserDto userDto = provider.getUserDto(token);
        AppUser user = userRepository.findById(userDto.getId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_BOOK));

        if (!Objects.equals(book.getUser().getId(), user.getId())) {
            throw new CustomException(ErrorCode.NOT_ALLOW_DELETE);
        }

        bookRepository.delete(book);
        return "delete/success";
    }

}
