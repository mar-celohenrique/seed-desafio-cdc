package com.cdc.books.controllers;

import com.cdc.RestControllerTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestControllerTest
class BookDetailControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    @DisplayName("show book details")
    void book() throws Exception {
        // given
        this.mockMvc.perform(MockMvcRequestBuilders.post("/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(Map.of("name", "Name",
                        "email",
                        "email@domain.com",
                        "description",
                        "Description"))));

        this.mockMvc.perform(MockMvcRequestBuilders.post("/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(Map.of("name", "Category"))));

        String title = "title";
        String synopsis = "synopsis";
        String summary = "summary";
        BigDecimal price = BigDecimal.valueOf(100L);
        int pages = 100;
        String isbn = "isbn";
        int authorId = 1;
        int categoryId = 1;
        String publicationDate = LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_DATE);


        this.mockMvc.perform(MockMvcRequestBuilders.post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(Map.of(
                        "title", title,
                        "synopsis", synopsis,
                        "summary", summary,
                        "price", price,
                        "pages", pages,
                        "isbn", isbn,
                        "publicationDate", publicationDate,
                        "authorId", authorId,
                        "categoryId", categoryId))));

        // when
        ResultActions result = this.mockMvc.perform(MockMvcRequestBuilders.get("/books/{id}", 1)
                .accept(MediaType.APPLICATION_JSON));

        // then
        Map<String, Object> author = Map.of("name", "Name", "description", "Description");
        Map<String, Object> bookDetails = Map.of(
                "title", title,
                "isbn", isbn,
                "pages", pages,
                "price", price,
                "synopsis", synopsis,
                "summary", summary,
                "publicationDate", publicationDate,
                "author", author);

        String expectedResult = this.objectMapper.writeValueAsString(bookDetails);

        result.andExpect(MockMvcResultMatchers.content().json(expectedResult));
    }

}