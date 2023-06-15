package com.zerobase.yogizogi.book.controller;

import com.zerobase.yogizogi.book.domain.model.BookForm;
import com.zerobase.yogizogi.book.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/accommodation")
public class BookController {
    private final String TOKEN ="X-AUTH-TOKEN";
    private final BookService bookService;

    @GetMapping()
    public ResponseEntity<?> myBook(@RequestHeader(name = TOKEN) String token,
        @RequestParam(name = "page", defaultValue = "0") int page,
        @RequestParam(name = "size", defaultValue = "2") int size,// 예외 처리가 추가로 필요할 것으로 보임. id만 들어가면 에러.
        @RequestParam(name = "sort", defaultValue = "id,desc") String sort){
        String[] sortProperties = sort.split(",");
        Sort.Direction direction = sortProperties[1].equalsIgnoreCase("desc")
            ? Sort.Direction.DESC : Sort.Direction.ASC;
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortProperties[0]));
        return ResponseEntity.ok(bookService.myBookList(token, pageable));
    }

    @PostMapping("/{accommodationId}/book")//TODO
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
