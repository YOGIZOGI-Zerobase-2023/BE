package com.zerobase.yogizogi.book.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.yogizogi.book.domain.model.BookForm;
import com.zerobase.yogizogi.book.service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

class BookControllerTest {

    @InjectMocks
    private BookController bookController;
    @Mock
    private BookService bookService;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(bookController).build();
    }

    @Test
    @DisplayName("GET /books 예약 page 반환")
    void testMyBook() throws Exception {
        // given
        String token = "testToken";
        int page = 0;
        int size = 2;
        String sort = "id,desc";
        // when/then
        mockMvc.perform(get("/books")
                .header("X-AUTH-TOKEN", token)
                .param("page", String.valueOf(page))
                .param("size", String.valueOf(size))
                .param("sort", sort)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("POST /books 예약 만들기")
    void testMakeBook() throws Exception {
        // given
        String token = "testToken";
        BookForm bookForm = new BookForm();
        ObjectMapper objectMapper = new ObjectMapper();
        String bookFormJson = objectMapper.writeValueAsString(bookForm);
        // when/then
        mockMvc.perform(post("/books")
                .header("X-AUTH-TOKEN", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookFormJson))
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("DELETE /books/{bookId} 특정 예약 삭제")
    void testDeleteBook() throws Exception {
        // given
        String token = "testToken";
        long bookId = 1L;
        // when/then
        mockMvc.perform(delete("/books/{bookId}", bookId)
                .header("X-AUTH-TOKEN", token)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }
}
