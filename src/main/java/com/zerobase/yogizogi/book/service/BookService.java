package com.zerobase.yogizogi.book.service;

import com.zerobase.yogizogi.accommodation.domain.entity.Room;
import com.zerobase.yogizogi.accommodation.repository.PriceRepository;
import com.zerobase.yogizogi.accommodation.repository.RoomRepository;
import com.zerobase.yogizogi.book.domain.entity.Book;
import com.zerobase.yogizogi.book.domain.model.BookForm;
import com.zerobase.yogizogi.book.dto.BookResultDto;
import com.zerobase.yogizogi.book.repository.BookRepository;
import com.zerobase.yogizogi.global.exception.CustomException;
import com.zerobase.yogizogi.global.exception.ErrorCode;
import com.zerobase.yogizogi.user.domain.entity.AppUser;
import com.zerobase.yogizogi.user.dto.UserDto;
import com.zerobase.yogizogi.user.repository.UserRepository;
import com.zerobase.yogizogi.user.token.JwtAuthenticationProvider;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookService {

    private final JwtAuthenticationProvider provider;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final PriceRepository priceRepository;

    //User의 BookList를 가지고 옵니다.
    public List<BookResultDto> myBookList(String token) {
        if (!provider.validateToken(token)) {
            throw new CustomException(ErrorCode.DO_NOT_ALLOW_TOKEN);
        }

        UserDto userDto = provider.getUserDto(token);
        AppUser user = userRepository.findById(userDto.getId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        List<Book> books = bookRepository.findAllByUser(user);
        return books.stream()
            .map(book -> BookResultDto.builder()
                .bookName(book.getBookName())
                .price(book.getPayAmount())
                .id(book.getId())
                .userId(book.getUser().getId())
                .checkInDate(book.getCheckOutDate())
                .checkOutDate(book.getCheckOutDate())
                .score(book.getAccommodation().getScore())
                .picUrl(book.getAccommodation().getPicUrl())
                .reviewRegistered(book.getReviewRegistered())
                .build())
            .collect(Collectors.toList());
    }

    //예약을 만듭니다.*현재는 숙소 등록에 관한 관련성이 없는 상태입니다.
    //현재는 등록 없이 사용을 위해 roomId를 가져오지 않지만 이후에는 roomId를 필수적으로 받을 것
    public String makeBook(String token, BookForm bookForm) {
        if (!provider.validateToken(token)) {
            throw new CustomException(ErrorCode.DO_NOT_ALLOW_TOKEN);
        }

        UserDto userDto = provider.getUserDto(token);
        AppUser user = userRepository.findById(userDto.getId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        //TO DO예약이 가능한지 여부 확인해서 불가능하면 예약을 더 이상 진행할 수 없는 요소**

        //예약 단계로 접어들며 한 번 더 예약 가능한지의 확인을 진행**
        // 해당 숙소가 해당 기간 동안에 예약이 가능한지로 검색할 것**
        //현재 하드 코딩으로 1만 넣은 상황으로 진행
        //room을 예약할 때, 숙소 정보를 리뷰를 위해 가지고 와 저장하기**
        int betweenDay = (int) ChronoUnit.DAYS.between(bookForm.getCheckInDate(),
            bookForm.getCheckOutDate());
        int payAmount = 0;
        Room room = roomRepository.findById(bookForm.getRoomId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ROOM));

        for (int i = 0; i < betweenDay; i++) {
            Integer priceCandidate = priceRepository.findAllByRoomAndDate(room,
                bookForm.getCheckInDate().plusDays(i)).getPrice();
            if (priceCandidate != null) {
                payAmount += priceCandidate;
            }
        }
        Book book = Book.builder().user(user)
            .checkInDate(bookForm.getCheckInDate())
            .checkOutDate(bookForm.getCheckOutDate())
            .people(bookForm.getPeople())
            .bookName(bookForm.getBookName()) //이 부분에 관한 처리 로직 고민.
            .payAmount(payAmount)
            .reviewRegistered(false).build();

        bookRepository.save(book);
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

        if (!Objects.equals(book.getUser().getId(), user.getId())) {
            throw new CustomException(ErrorCode.NOT_ALLOW_DELETE);
        }

        bookRepository.delete(book);
        return "delete/success";
    }
}
