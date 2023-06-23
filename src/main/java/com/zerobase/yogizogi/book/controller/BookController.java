package com.zerobase.yogizogi.book.controller;

import com.zerobase.yogizogi.book.domain.model.BookForm;
import com.zerobase.yogizogi.book.dto.BookResultDto;
import com.zerobase.yogizogi.book.service.BookService;
import com.zerobase.yogizogi.global.ApiResponse;
import com.zerobase.yogizogi.global.ResponseCode;
import java.util.List;
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

    private final String TOKEN = "X-AUTH-TOKEN";
    private final BookService bookService;

    @GetMapping("/user/{userId}/mybook")
    public ResponseEntity<ApiResponse<List<BookResultDto>>> myBook(
        @PathVariable(name = "userId") Long userId,
        @RequestHeader(name = TOKEN) String token) {
        List<BookResultDto> Dtos = bookService.myBookList(userId, token).stream()
            .map(BookResultDto::from)
            .collect(Collectors.toList());

        return ApiResponse.builder().code(ResponseCode.RESPONSE_SUCCESS).data(Dtos).build().toEntity() ;
    }

    @PostMapping("/accommodation/{accommodationId}/book")
    public ResponseEntity<?> makeBook(@RequestHeader(name = TOKEN) String token,
        @PathVariable(name = "accommodationId") Long accommodationId,
        @RequestBody BookForm bookForm) {

        return ResponseEntity.ok(bookService.makeBook(token, bookForm));
    }

    @DeleteMapping("/user/{userId}/mybook/{bookId}")
    public ResponseEntity<?> deleteBook(@RequestHeader(name = TOKEN) String token,
        @PathVariable(name = "userId") Long userId,
        @PathVariable(name = "bookId") Long bookId) {
        return ResponseEntity.ok().body(bookService.deleteBook(token, userId, bookId));
    }
}
