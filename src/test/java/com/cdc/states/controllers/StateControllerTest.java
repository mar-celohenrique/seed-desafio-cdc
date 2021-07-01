package com.cdc.states.controllers;

import com.cdc.BaseRestControllerTest;
import com.cdc.RestControllerTest;
import com.cdc.countries.entities.Country;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.jqwik.api.ForAll;
import net.jqwik.api.Label;
import net.jqwik.api.Property;
import net.jqwik.api.constraints.AlphaChars;
import net.jqwik.api.constraints.StringLength;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assumptions.assumeTrue;

@RestControllerTest
class StateControllerTest extends BaseRestControllerTest {

    private final Set<String> names = new HashSet<>();

    @Property(tries = 10)
    @Label("should create a state")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    void create(@ForAll @AlphaChars @StringLength(min = 1, max = 255) String name) throws Exception {
        assumeTrue(this.names.add(name));

        // given
        Country country = new ObjectMapper().readValue(super.post("/countries", Map.of("name", "country"))
                .andReturn()
                .getResponse()
                .getContentAsString(), Country.class);

        Map<String, Object> content = Map.of("name", name, "countryId", country.getId());

        // then
        super.post("/states", content).andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        super.post("/states", content).andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }
}