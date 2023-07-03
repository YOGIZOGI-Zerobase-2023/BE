package com.zerobase.yogizogi.book.controller;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.yogizogi.book.domain.model.BookForm;
import com.zerobase.yogizogi.book.service.BookService;
import java.util.Collections;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Test
    @DisplayName("예약리스트 반환")
    public void testMyBook() throws Exception {
        // given
        long userId = 1L;
        String token = "testToken";
        given(this.bookService.myBookList(userId, token)).willReturn(Collections.emptyList());

        // when
        this.mockMvc.perform(get("/api/user/" + userId + "/mybook")
                .header("X-AUTH-TOKEN", token))
            // then
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("예약하기")
    public void testMakeBook() throws Exception {
        // given
        long accommodationId = 1L;
        String token = "testToken";
        BookForm bookForm = new BookForm();
        // when
        this.mockMvc.perform(post("/api/accommodation/" + accommodationId + "/book")
                .header("X-AUTH-TOKEN", token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(bookForm)))
            // then
            .andExpect(status().isOk());
    }

    @Test
    @DisplayName("예약 삭제")
    public void testDeleteBook() throws Exception {
        // given
        long userId = 1L;
        long bookId = 1L;
        String token = "testToken";
        // when
        this.mockMvc.perform(delete("/api/user/" + userId + "/mybook/" + bookId)
                .header("X-AUTH-TOKEN", token))
            // then
            .andExpect(status().isOk());
    }
}