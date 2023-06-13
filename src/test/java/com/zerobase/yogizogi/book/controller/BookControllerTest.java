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
    void testMyBook() throws Exception {
        String token = "testToken";
        mockMvc.perform(get("/books")
                .header("X-AUTH-TOKEN", token)
                .param("page", "0")
                .param("size", "2")
                .param("sort", "id,desc")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
    }
    @Test
    @DisplayName("BookMakeController 테스트")

    void testMakeBook() throws Exception {
        // Given
        String token = "testToken";
        BookForm bookForm = new BookForm();
        ObjectMapper objectMapper = new ObjectMapper();
        String bookFormJson = objectMapper.writeValueAsString(bookForm);
        // When
        mockMvc.perform(post("/books")
                .header("X-AUTH-TOKEN", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(bookFormJson))
            // Then
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("BookDeleteController 테스트")
    void testDeleteBook() throws Exception {
        // Given
        String token = "testToken";
        long bookId = 1L;
        // When
        mockMvc.perform(delete("/books/" + bookId)
                .header("X-AUTH-TOKEN", token)
                .contentType(MediaType.APPLICATION_JSON))
            // Then
            .andExpect(status().isOk());
    }
}