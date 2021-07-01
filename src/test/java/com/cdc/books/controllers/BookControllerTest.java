package com.cdc.books.controllers;

import com.cdc.BaseRestControllerTest;
import com.cdc.RestControllerTest;
import com.cdc.authors.entities.Author;
import com.cdc.categories.entities.Category;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
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
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@RestControllerTest
class BookControllerTest extends BaseRestControllerTest {

    private final Set<String> values = new HashSet<>();

    private final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

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
        Author author = this.objectMapper.readValue(super.post("/authors", Map.of("name", "Name",
                "email",
                randomAlphabetic(10).concat("@domain.com"),
                "description",
                "Description")).andReturn().getResponse().getContentAsString(), Author.class);

        Category category = this.objectMapper.readValue(super.post("/categories", Map.of("name", randomAlphabetic(10)))
                .andReturn().getResponse().getContentAsString(), Category.class);

        Map<String, Object> content = Map.of(
                "title", title,
                "synopsis", synopsis,
                "summary", summary,
                "price", price,
                "pages", pages,
                "isbn", isbn,
                "publicationDate", publicationDate.format(DateTimeFormatter.ISO_DATE),
                "authorId", author.getId(),
                "categoryId", category.getId());

        // then
        super.post("/books", content).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        super.post("/books", content).andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Provide
    Arbitrary<LocalDate> futureDates() {
        return Dates.dates().atTheEarliest(LocalDate.now().plusDays(1));
    }

}