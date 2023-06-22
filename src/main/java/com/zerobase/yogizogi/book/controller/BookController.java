package com.zerobase.yogizogi.book.controller;

import com.zerobase.yogizogi.book.domain.model.BookForm;
import com.zerobase.yogizogi.book.service.BookService;
import java.time.LocalDate;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
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
    public ResponseEntity<?> myBook(@PathVariable(name = "userId") Long userId,
        @RequestHeader(name = TOKEN) String token) {
        return ResponseEntity.ok(bookService.myBookList(userId, token));
    }

    @PostMapping("/accommodation/{accommodationId}/checkindate={checkindate}&checkoutdate={checkoutdate}&people={people}/{roomId}/book")
    public ResponseEntity<?> makeBook(@RequestHeader(name = TOKEN) String token,
        @PathVariable(name = "accommodationId") Long accommodationId,
        @PathVariable(name = "checkindate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkInDate,
        @PathVariable(name = "checkoutdate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate checkOutDate,
        @PathVariable(name = "people") Integer people,
        @PathVariable(name = "roomId") Long roomId,
        @RequestBody BookForm bookForm) {
        bookForm.setAccommodationId(accommodationId);
        bookForm.setCheckInDate(checkInDate);
        bookForm.setCheckOutDate(checkOutDate);
        bookForm.setPeople(people);
        bookForm.setRoomId(roomId);
        return ResponseEntity.ok(bookService.makeBook(token, bookForm));
    }

    @DeleteMapping("/{bookId}")
    public ResponseEntity<?> deleteBook(@RequestHeader(name = TOKEN) String token,
        @PathVariable(name = "bookId") Long bookId) {
        return ResponseEntity.ok().body(bookService.deleteBook(token, bookId));
    }
}
