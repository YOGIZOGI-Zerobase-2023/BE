package com.zerobase.yogizogi.review.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.yogizogi.accommodation.domain.entity.Accommodation;
import com.zerobase.yogizogi.review.domain.entity.Review;
import com.zerobase.yogizogi.review.domain.model.ReviewForm;
import com.zerobase.yogizogi.review.domain.model.ReviewUpdateForm;
import com.zerobase.yogizogi.review.dto.ReviewDto;
import com.zerobase.yogizogi.review.service.ReviewService;
import com.zerobase.yogizogi.user.domain.entity.AppUser;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@SpringBootTest
@AutoConfigureMockMvc
public class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    private final String TOKEN = "X-AUTH-TOKEN";
    private final String token = "TestToken";
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("리뷰리스트 불러오기")
    public void testReviewsList() throws Exception {
        // given
        Review review = new Review();
        review.setId(1L);
        review.setUser(new AppUser());
        review.getUser().setNickName("testNickName");
        review.setAccommodation(new Accommodation());
        review.getAccommodation().setId(1L);
        review.setRate(10);
        review.setDescription("testDescription");

        Page<Review> page = new PageImpl<>(Collections.singletonList(review));
        when(reviewService.reviewList(any(Long.class), any())).thenReturn(page);

        Page<ReviewDto> result = page.map(ReviewDto::from);
        String expectedJson = objectMapper.writeValueAsString(result);

        // when
        MvcResult mvcResult = mockMvc.perform(get("/api/accommodation/1/review")
                .contentType(MediaType.APPLICATION_JSON))
            .andReturn();

        // then
        String actualResponseBody = mvcResult.getResponse().getContentAsString();
        JsonNode actualResponseJsonNode = objectMapper.readTree(actualResponseBody);
        JsonNode dataJsonNode = actualResponseJsonNode.get("data");
        String actualDataJson = objectMapper.writeValueAsString(dataJsonNode);
        assertEquals(expectedJson, actualDataJson);
    }


    @Test
    @DisplayName("리뷰 만들기")
    public void testMakeReview() throws Exception {
        ReviewForm reviewForm = new ReviewForm();
        Map<String, String> msg = new HashMap<>();
        msg.put("msg", "성공적으로 리뷰를 작성 했습니다.");

        ObjectMapper objectMapper = new ObjectMapper();
        String reviewFormJson = objectMapper.writeValueAsString(reviewForm);

        // given
        String expectedJsonResponse = "{\"code\":\"RESPONSE_SUCCESS\",\"status\":\"OK\",\"msg\":\"SUCCESS\",\"data\":{\"msg\":\"성공적으로 리뷰를 작성 했습니다.\"}}";

        // when
        mockMvc.perform(post("/api/accommodation/1/review")
                .header(TOKEN, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(reviewFormJson))
            .andExpect(status().isOk())
            // then
            .andExpect(content().json(expectedJsonResponse));
    }

    @Test
    @DisplayName("리뷰 업데이트")
    public void testUpdateReview() throws Exception {
        ReviewUpdateForm reviewForm = new ReviewUpdateForm();
        Map<String, String> msg = new HashMap<>();
        msg.put("msg", "성공적으로 작업을 수행 했습니다.");

        ObjectMapper objectMapper = new ObjectMapper();
        String reviewFormJson = objectMapper.writeValueAsString(reviewForm);

        // given
        String expectedJsonResponse = "{\"code\":\"RESPONSE_SUCCESS\",\"status\":\"OK\",\"msg\":\"SUCCESS\",\"data\":{\"msg\":\"성공적으로 작업을 수행 했습니다.\"}}";

        // when
        mockMvc.perform(patch("/api/accommodation/1/review/1")
                .header(TOKEN, token)
                .contentType(MediaType.APPLICATION_JSON)
                .content(reviewFormJson))
            .andExpect(status().isOk())
            // then
            .andExpect(content().json(expectedJsonResponse));
    }

    @Test
    @DisplayName("리뷰 삭제")
    public void testDeleteReview() throws Exception {
        // given
        String expectedJsonResponse = "{\"code\":\"RESPONSE_SUCCESS\",\"status\":\"OK\",\"msg\":\"SUCCESS\",\"data\":{\"msg\":\"성공적으로 작업을 수행 했습니다.\"}}";

        // when
        mockMvc.perform(delete("/api/accommodation/1/review/1")
                .header(TOKEN, token)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            // then
            .andExpect(content().json(expectedJsonResponse));
    }
}
