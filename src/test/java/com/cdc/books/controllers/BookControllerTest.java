package com.cdc.books.controllers;

import com.cdc.RestControllerTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.jqwik.api.Arbitrary;
import net.jqwik.api.ForAll;
import net.jqwik.api.Label;
import net.jqwik.api.Property;
import net.jqwik.api.Provide;
import net.jqwik.api.constraints.AlphaChars;
import net.jqwik.api.constraints.BigRange;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.constraints.NumericChars;
import net.jqwik.api.constraints.StringLength;
import net.jqwik.time.api.Dates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

@RestControllerTest
class BookControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final Set<String> values = new HashSet<>();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Property(tries = 10)
    @Label("should create a book")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void create(@ForAll @AlphaChars @StringLength(min = 1, max = 255) String title,
                @ForAll @AlphaChars @StringLength(min = 1, max = 500) String synopsis,
                @ForAll @AlphaChars @StringLength(min = 1, max = 255) String summary,
                @ForAll @BigRange(min = "20", max = "100") BigDecimal price,
                @ForAll @IntRange(min = 100) Integer pages,
                @ForAll @NumericChars @StringLength(min = 10, max = 10) String isbn,
                @ForAll("futureDates") LocalDate publicationDate) throws Exception {

        assumeTrue(this.values.add(title));
        assumeTrue(this.values.add(isbn));

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

        String content = this.objectMapper.writeValueAsString(Map.of(
                "title", title,
                "synopsis", synopsis,
                "summary", summary,
                "price", price,
                "pages", pages,
                "isbn", isbn,
                "publicationDate", publicationDate.format(DateTimeFormatter.ISO_DATE),
                "authorId", 1,
                "categoryId", 1));

        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        // then
        this.mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        this.mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Provide
    Arbitrary<LocalDate> futureDates() {
        return Dates.dates().atTheEarliest(LocalDate.now().plusDays(1));
    }

}