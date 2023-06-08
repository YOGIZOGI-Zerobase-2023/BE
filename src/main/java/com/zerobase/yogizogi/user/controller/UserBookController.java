package com.zerobase.yogizogi.user.controller;

import com.zerobase.yogizogi.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserBookController {
    private final String TOKEN = "X-AUTH-TOKEN";
    private final BookService bookService;
    @GetMapping("/myboooks")
    public ResponseEntity<?> myBook(@RequestHeader(name = TOKEN) String token,
        @PageableDefault(page = 0, size = 10, sort = "id", direction = Sort.Direction.DESC)
        Pageable pageable){
        return ResponseEntity.ok(bookService.myBookList(token, pageable));
    }
}
