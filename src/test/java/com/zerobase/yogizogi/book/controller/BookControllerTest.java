package com.zerobase.yogizogi.book.controller;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.yogizogi.book.domain.entity.Book;
import com.zerobase.yogizogi.book.domain.model.BookForm;
import com.zerobase.yogizogi.book.service.BookService;
import java.time.LocalDate;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(BookController.class)
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private BookService bookService;

    @Test
    @DisplayName("GET /books - Get my book list")
    void myBook() throws Exception {
        // given
        String token = "test-token";
        PageRequest pageable = PageRequest.of(0, 2);
        Page<Book> bookPage = new PageImpl<>(Collections.emptyList(), pageable, 0);
        when(bookService.myBookList(eq(token), eq(pageable))).thenReturn(bookPage);

        // when
        mockMvc.perform(get("/books")
                .header("X-AUTH-TOKEN", token)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        // then
        verify(bookService).myBookList(eq(token), eq(pageable));
    }

    @Test
    @DisplayName("POST /books - Make a new book")
    void makeBook() throws Exception {
        // given
        String token = "test-token";
        BookForm bookForm = new BookForm();
        bookForm.setStartDate(LocalDate.now());
        bookForm.setEndDate(LocalDate.now().plusDays(1));
        bookForm.setBookName("Test Book");
        bookForm.setPeople(2);
        bookForm.setPayAmount(100);
        String requestBody = objectMapper.writeValueAsString(bookForm);

        // when
        mockMvc.perform(post("/books")
                .header("X-AUTH-TOKEN", token)
                .content(requestBody)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        // then
        verify(bookService).makeBook(eq(token), eq(bookForm));
    }

    @Test
    @DisplayName("DELETE /books/{bookId} - Delete a book")
    void deleteBook() throws Exception {
        // given
        String token = "test-token";
        Long bookId = 1L;

        // when
        mockMvc.perform(delete("/books/{bookId}", bookId)
                .header("X-AUTH-TOKEN", token)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

        // then
        verify(bookService).deleteBook(eq(token), eq(bookId));
    }
}