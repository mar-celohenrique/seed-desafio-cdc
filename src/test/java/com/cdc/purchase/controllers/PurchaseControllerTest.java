package com.cdc.purchase.controllers;

import com.cdc.BaseRestControllerTest;
import com.cdc.RestControllerTest;
import com.cdc.authors.entities.Author;
import com.cdc.books.entities.Book;
import com.cdc.categories.entities.Category;
import com.cdc.countries.entities.Country;
import com.cdc.states.entities.State;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import net.jqwik.api.ForAll;
import net.jqwik.api.Label;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.AlphaChars;
import net.jqwik.api.constraints.IntRange;
import net.jqwik.api.constraints.StringLength;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;

@RestControllerTest
class PurchaseControllerTest extends BaseRestControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper()
            .findAndRegisterModules()
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    @Property(tries = 10)
    @Label("should create a purchase")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void create(@ForAll @AlphaChars @StringLength(min = 1, max = 50) String email,
                @ForAll @AlphaChars @StringLength(min = 1, max = 255) String name,
                @ForAll @AlphaChars @StringLength(min = 1, max = 255) String lastName,
                @ForAll @AlphaChars @StringLength(min = 1, max = 255) String address,
                @ForAll @AlphaChars @StringLength(min = 1, max = 255) String complement,
                @ForAll @AlphaChars @StringLength(min = 1, max = 255) String city,
                @ForAll @AlphaChars @StringLength(min = 1, max = 20) String phone,
                @ForAll @AlphaChars @StringLength(min = 1, max = 8) String zipCode,
                @ForAll @IntRange(min = 1, max = 50) int quantity) throws Exception {

        Author author = this.objectMapper.readValue(super.post("/authors", Map.of("name", "Name",
                "email",
                randomAlphabetic(10).concat("@domain.com"),
                "description",
                "Description")).andReturn().getResponse().getContentAsString(), Author.class);

        Category category = this.objectMapper.readValue(super.post("/categories", Map.of("name", randomAlphabetic(10)))
                .andReturn().getResponse().getContentAsString(), Category.class);

        Book book = this.objectMapper.readValue(super.post("/books", Map.of(
                "title", randomAlphabetic(10),
                "synopsis", "synopsis",
                "summary", "summary",
                "price", BigDecimal.valueOf(100L),
                "pages", 100,
                "isbn", randomAlphabetic(10),
                "publicationDate", LocalDate.now().plusDays(1).format(DateTimeFormatter.ISO_DATE),
                "authorId", author.getId(),
                "categoryId", category.getId())).andReturn().getResponse().getContentAsString(), Book.class);

        Country country = new ObjectMapper().readValue(super.post("/countries", Map.of("name", "Country Name"))
                .andReturn()
                .getResponse()
                .getContentAsString(), Country.class);

        State state = new ObjectMapper().readValue(super.post("/states", Map.of("name", "State Name", "countryId", country.getId()))
                .andReturn()
                .getResponse()
                .getContentAsString(), State.class);

        // given
        List<Map<String, Object>> itemsRequest = List.of(Map.of("bookId", book.getId(), "quantity", quantity));

        Map<String, Object> purchaseOrderDetail = Map.of("total", book.getPrice().multiply(new BigDecimal(quantity)),
                "itemsRequest", itemsRequest);

        Map<String, Object> content = new HashMap<>();
        content.put("email", email.toLowerCase().concat("@domain.com"));
        content.put("name", name);
        content.put("lastName", lastName);
        content.put("document", "91739279026");
        content.put("address", address);
        content.put("complement", complement);
        content.put("city", city);
        content.put("countryId", country.getId());
        content.put("stateId", state.getId());
        content.put("phone", phone);
        content.put("zipCode", zipCode);
        content.put("purchaseOrderDetail", purchaseOrderDetail);

        // then
        super.post("/po", content).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
    }
}