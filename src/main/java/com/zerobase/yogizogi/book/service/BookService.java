package com.zerobase.yogizogi.book.service;

import com.zerobase.yogizogi.accommodation.domain.entity.Accommodation;
import com.zerobase.yogizogi.accommodation.domain.entity.Room;
import com.zerobase.yogizogi.accommodation.repository.AccommodationRepository;
import com.zerobase.yogizogi.accommodation.repository.PriceRepository;
import com.zerobase.yogizogi.accommodation.repository.RoomRepository;
import com.zerobase.yogizogi.book.domain.entity.Book;
import com.zerobase.yogizogi.book.domain.model.BookForm;
import com.zerobase.yogizogi.book.repository.BookRepository;
import com.zerobase.yogizogi.global.exception.CustomException;
import com.zerobase.yogizogi.global.exception.ErrorCode;
import com.zerobase.yogizogi.user.domain.entity.AppUser;
import com.zerobase.yogizogi.user.dto.UserDto;
import com.zerobase.yogizogi.user.repository.UserRepository;
import com.zerobase.yogizogi.user.token.JwtAuthenticationProvider;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;
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
    private final AccommodationRepository accommodationRepository;

    //User의 BookList를 가지고 옵니다.
    public List<Book> myBookList(Long userId, String token) {

        if (provider.validateToken(token)) {
            throw new CustomException(ErrorCode.DO_NOT_ALLOW_TOKEN);
        }

        UserDto userDto = provider.getUserDto(token);
        AppUser user = userRepository.findById(userDto.getId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        if (!Objects.equals(user.getId(), userId)) {
            throw new CustomException(ErrorCode.NOT_ALLOW_ACCESS);
        }

        return bookRepository.findAllByUser_Id(userId);
    }

    //현재는 등록 없이 사용을 위해 roomId를 가져오지 않지만 이후에는 roomId를 필수적으로 받을 것
    public void makeBook(String token, BookForm bookForm) {
        if (provider.validateToken(token)) {
            throw new CustomException(ErrorCode.DO_NOT_ALLOW_TOKEN);
        }

        UserDto userDto = provider.getUserDto(token);
        AppUser user = userRepository.findById(userDto.getId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));
        Accommodation accommodation = accommodationRepository.findById(
                bookForm.getAccommodationId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ACCOMMODATION));
        //Done예약이 가능한지 여부 확인해서 불가능하면 예약을 더 이상 진행할 수 없는 요소**
        //예약 단계로 접어들며 한 번 더 예약 가능한지의 확인을 진행**

        int betweenDay = (int) ChronoUnit.DAYS.between(bookForm.getCheckInDate(),
            bookForm.getCheckOutDate());

        Room room = roomRepository.findById(bookForm.getRoomId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_ROOM));

        Book book = Book.builder().user(user)//외래키 저장하려면 명시적으로 넣어야 하는 부분의 수정 가능성.
            .accommodation(accommodation)
            .room(room)
            .checkInDate(bookForm.getCheckInDate())
            .checkOutDate(bookForm.getCheckOutDate())
            .people(bookForm.getPeople())
            .bookName(bookForm.getBookName()) //이 부분에 관한 처리 로직 고민.
            .payAmount(bookForm.getPayAmount())
            .reviewRegistered(false).build();
        IntStream.range(0, betweenDay)
            .mapToObj(i -> priceRepository.findAllByRoom_IdAndDate(room.getId(),
                bookForm.getCheckInDate().plusDays(i)))
            .forEach(price -> {
                if (price != null) {
                    if (price.getRoomCnt() == 0) {//해당 로직은 여러 번 동시 예약 상황 고려해 계속 확인
                        deleteBook(token, user.getId(), book.getId());//해당 예약건을 없애고, 처리를 실패로 넘겨야 함.
                        throw new CustomException(ErrorCode.ALREADY_BOOKED_ROOM);
                    }
                    price.setRoomCnt(price.getRoomCnt() - 1);
                    priceRepository.save(price);
                } else {
                    throw new CustomException(ErrorCode.NOT_ALLOW_ACCESS);
                }
            });
        bookRepository.save(book);
        //가격 테이블 변경해주기.

    }

    public void deleteBook(String token, Long userId, Long bookId) {
        if (provider.validateToken(token)) {
            throw new CustomException(ErrorCode.DO_NOT_ALLOW_TOKEN);
        }

        UserDto userDto = provider.getUserDto(token);
        AppUser user = userRepository.findById(userDto.getId())
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_USER));

        if (!Objects.equals(userDto.getId(), userId)) {
            throw new CustomException(ErrorCode.NOT_ALLOW_ACCESS);
        }

        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND_BOOK));

        // 예약일이 지난 예약은 삭제 불가능.
        LocalDate now = LocalDate.now();
        if (book.getCheckInDate().isBefore(now)) {
            throw new CustomException(ErrorCode.NOT_ALLOW_DELETE_BOOK);
        }

        if (!Objects.equals(book.getUser().getId(), user.getId())) {
            throw new CustomException(ErrorCode.NOT_ALLOW_ACCESS);
        }

        // 예약 삭제시, (정합하면, Price roomCnt 업데이트)
        int betweenDay = (int) ChronoUnit.DAYS.between(book.getCheckInDate(),
            book.getCheckOutDate());
        IntStream.range(0, betweenDay)
            .mapToObj(i -> priceRepository.findAllByRoom_IdAndDate(book.getRoom().getId(),
                book.getCheckInDate().plusDays(i)))
            .forEach(price -> {
                if (price != null) {
                    price.setRoomCnt(price.getRoomCnt() + 1);
                    priceRepository.save(price);
                }
            });

        bookRepository.delete(book);
    }
}
