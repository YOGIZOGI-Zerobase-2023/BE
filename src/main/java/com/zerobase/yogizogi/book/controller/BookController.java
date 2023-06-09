package com.zerobase.yogizogi.book.controller;

import com.zerobase.yogizogi.book.domain.model.BookForm;
import com.zerobase.yogizogi.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
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
@RequestMapping("/books")
public class BookController {
    private final String TOKEN ="X-AUTH-TOKEN";
    private final BookService bookService;

    @GetMapping()
    public ResponseEntity<?> myBook(@RequestHeader(name = TOKEN) String token,
       @PageableDefault(page = 0, size = 2,sort = "id", direction = Sort.Direction.DESC)
        Pageable pageable){
        return ResponseEntity.ok(bookService.myBookList(token, pageable));
    }

    @PostMapping()
    public ResponseEntity<?> makeBook(@RequestHeader(name=TOKEN) String token,
        @RequestBody BookForm bookForm){
        return ResponseEntity.ok(bookService.makeBook(token, bookForm));
    }
    @DeleteMapping("/{bookId}")
    public ResponseEntity<?> deleteBook(@RequestHeader(name = TOKEN) String token,
        @PathVariable(name = "bookId") Long bookId) {
        return ResponseEntity.ok().body(bookService.deleteBook(token, bookId));
    }
}
