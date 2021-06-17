package com.cdc.authors.controllers;

import com.cdc.BaseRestControllerTest;
import com.cdc.RestControllerTest;
import net.jqwik.api.ForAll;
import net.jqwik.api.Label;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.AlphaChars;
import net.jqwik.api.constraints.StringLength;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

@RestControllerTest
class AuthorControllerTest extends BaseRestControllerTest {

    private final Set<String> emails = new HashSet<>();

    @Property(tries = 10)
    @Label("should create an author")
    void create(@ForAll @AlphaChars @StringLength(min = 1, max = 255) String name,
                @ForAll @AlphaChars @StringLength(min = 1, max = 50) String email,
                @ForAll @AlphaChars @StringLength(min = 1, max = 255) String description) throws Exception {

        assumeTrue(this.emails.add(email));

        // given
        Map<String, Object> content = Map.of("name",
                name,
                "email",
                email + "@domain.com",
                "description",
                description);

        // then
        super.post("/authors", content).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        super.post("/authors", content).andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

}