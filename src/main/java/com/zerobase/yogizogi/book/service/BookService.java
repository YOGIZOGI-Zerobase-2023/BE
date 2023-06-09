package com.zerobase.yogizogi.book.service;

import com.zerobase.yogizogi.book.domain.entity.Book;
import com.zerobase.yogizogi.book.domain.model.BookForm;
import com.zerobase.yogizogi.book.repository.BookRepository;
import com.zerobase.yogizogi.global.exception.CustomException;
import com.zerobase.yogizogi.global.exception.ErrorCode;
import com.zerobase.yogizogi.user.common.UserRole;
import com.zerobase.yogizogi.user.domain.entity.AppUser;
import com.zerobase.yogizogi.user.dto.UserDto;
import com.zerobase.yogizogi.user.repository.UserRepository;
import com.zerobase.yogizogi.user.token.JwtAuthenticationProvider;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
        if (!provider.validateToken(token)) {
            throw new CustomException(ErrorCode.DO_NOT_ALLOW_TOKEN);
        }

        UserDto userDto = provider.getUserDto(token);
        AppUser user = userRepository.findById(userDto.getId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        //TO DO예약이 가능한지 여부 확인해서 불가능하면 예약을 더 이상 진행할 수 없는 요소**

        //USER 만 예약이 가능하게 방어.
        if (user.getUserRole() == UserRole.HOST) {
            throw new CustomException(ErrorCode.HOST_NOT_ALLOW_BOOK);
        }
        //예약 단계로 접어들며 한 번 더 예약 가능한지의 확인을 진행** 해당 숙소가 해당 기간 동안에 예약이 가능한지로 검색할 것**

        Book book = Book.builder().userId(user.getId())
            .startDate(bookForm.getStartDate())
            .endDate(bookForm.getEndDate())
            .people(bookForm.getPeople()).payAmount(bookForm.getPayAmount())
            .reviewRegistered(false).build();

        bookRepository.save(book);

        /**
         //book 값 명시적 저장 :)<-불필요 할 수도 있어서 우선 주석 처리
         List<Book> userBookList = user.getBooks();
         if (userBookList == null) {
         userBookList = new ArrayList<>();
         }
         userBookList.add(book);
         user.setBooks(userBookList);
         userRepository.save(user);
         */
        return "/success";
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

        if (!Objects.equals(book.getUserId(), user.getId())) {
            throw new CustomException(ErrorCode.NOT_ALLOW_DELETE);
        }

        bookRepository.delete(book);
        return "delete/success";
    }

    public Page<Book> myBookList(String token, Pageable pageable) {
        if (!provider.validateToken(token)) {
            throw new CustomException(ErrorCode.DO_NOT_ALLOW_TOKEN);
        }

        UserDto userDto = provider.getUserDto(token);
        AppUser user = userRepository.findById(userDto.getId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        return  bookRepository.findAllByUserId(user.getId(), pageable);
    }
}
