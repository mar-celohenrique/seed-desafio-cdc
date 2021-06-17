package com.cdc.books.controllers;

import com.cdc.BaseRestControllerTest;
import com.cdc.RestControllerTest;
import com.cdc.authors.entities.Author;
import com.cdc.books.entities.Book;
import com.cdc.categories.entities.Category;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@RestControllerTest
class BookDetailControllerTest extends BaseRestControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Test
    @DisplayName("show book details")
    void book() throws Exception {
        // given
        Author author = this.objectMapper.readValue(super.post("/authors", Map.of("name", "Name",
                "email",
                randomAlphabetic(10).concat("@domain.com"),
                "description",
                "Description")).andReturn().getResponse().getContentAsString(), Author.class);

        Category category = this.objectMapper.readValue(super.post("/categories", Map.of("name", randomAlphabetic(10)))
                .andReturn().getResponse().getContentAsString(), Category.class);

        String title = randomAlphabetic(10);
        String synopsis = "synopsis";
        String summary = "summary";
        BigDecimal price = BigDecimal.valueOf(100L);
        int pages = 100;
        String isbn = randomAlphabetic(10);
        Long authorId = author.getId();
        Long categoryId = category.getId();
        String publicationDate = LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_DATE);

        Book book = this.objectMapper.readValue(super.post("/books", Map.of(
                "title", title,
                "synopsis", synopsis,
                "summary", summary,
                "price", price,
                "pages", pages,
                "isbn", isbn,
                "publicationDate", publicationDate,
                "authorId", authorId,
                "categoryId", categoryId)).andReturn().getResponse().getContentAsString(), Book.class);

        // when
        ResultActions result = super.get("/books/{id}", book.getId());

        // then
        Map<String, Object> authorDetails = Map.of("name", "Name", "description", "Description");
        Map<String, Object> bookDetails = Map.of(
                "title", title,
                "isbn", isbn,
                "pages", pages,
                "price", price,
                "synopsis", synopsis,
                "summary", summary,
                "publicationDate", publicationDate,
                "author", authorDetails);

        String expectedResult = this.objectMapper.writeValueAsString(bookDetails);

        result.andExpect(MockMvcResultMatchers.content().json(expectedResult));
    }

}