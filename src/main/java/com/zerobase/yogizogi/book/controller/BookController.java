package com.zerobase.yogizogi.book.controller;

import com.zerobase.yogizogi.book.domain.model.BookForm;
import com.zerobase.yogizogi.book.dto.BookResultDto;
import com.zerobase.yogizogi.book.service.BookService;
import com.zerobase.yogizogi.global.ApiResponse;
import com.zerobase.yogizogi.global.ResponseCode;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BookController {

    private static final String TOKEN = "X-AUTH-TOKEN";//계속 쓰이는 동일한 값은 static으로
    private final BookService bookService;

    @GetMapping("/user/{userId}/mybook")
    public ResponseEntity<ApiResponse<Object>> myBook(
        @PathVariable(name = "userId") Long userId,
        @RequestHeader(name = TOKEN) String token) {
        List<BookResultDto> Dtos = bookService.myBookList(userId, token).stream()
            .map(BookResultDto::from)
            .collect(Collectors.toList());
        Collections.reverse(Dtos);// id 역순으로 가져오기 위한 방법
        return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(Dtos).toEntity();
    }

    @PostMapping("/accommodation/{accommodationId}/book")
    public ResponseEntity<ApiResponse<Object>> makeBook(@RequestHeader(name = TOKEN) String token,
        @PathVariable(name = "accommodationId") Long accommodationId,
        @RequestBody BookForm bookForm) {
        bookService.makeBook(token, bookForm);
        Map<String, String> msg = new HashMap<>();
        msg.put("msg", "예약이 성공적으로 이루어졌습니다.");
        return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(msg).toEntity();
    }

    @DeleteMapping("/user/{userId}/mybook/{bookId}")
    public ResponseEntity<ApiResponse<Object>> deleteBook(@RequestHeader(name = TOKEN) String token,
        @PathVariable(name = "userId") Long userId,
        @PathVariable(name = "bookId") Long bookId) {
        bookService.deleteBook(token, userId, bookId);
        Map<String, String> msg = new HashMap<>();
        msg.put("msg", "성공적으로 작업을 수행 했습니다.");
        return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(msg).toEntity();
    }
}
