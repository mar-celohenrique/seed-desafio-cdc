package com.cdc.authors.controllers;

import com.cdc.RestControllerTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.jqwik.api.ForAll;
import net.jqwik.api.Label;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.AlphaChars;
import net.jqwik.api.constraints.StringLength;
import org.junit.jupiter.api.Assumptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

@RestControllerTest
class AuthorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final Set<String> emails = new HashSet<>();

    @Property(tries = 10)
    @Label("should create an author")
    void create(@ForAll @AlphaChars @StringLength(min = 1, max = 255) String name,
                @ForAll @AlphaChars @StringLength(min = 1, max = 50) String email,
                @ForAll @AlphaChars @StringLength(min = 1, max = 255) String description) throws Exception {

        assumeTrue(this.emails.add(email));

        // given
        String content = new ObjectMapper().writeValueAsString(Map.of("name",
                name,
                "email",
                email + "@domain.com",
                "description",
                description));

        // then
        MockHttpServletRequestBuilder builder = MockMvcRequestBuilders.post("/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(content);

        this.mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        this.mockMvc.perform(builder).andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

}