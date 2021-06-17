package com.cdc;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.Map;

public abstract class BaseRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public final ResultActions post(String path, Map<String, Object> content) throws Exception {
        return this.mockMvc.perform(MockMvcRequestBuilders.post(path)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(content))).andDo(MockMvcResultHandlers.print());
    }

    public final ResultActions get(String path, Object... uriVars) throws Exception {
        return this.mockMvc.perform(MockMvcRequestBuilders.get(path, uriVars)
                .accept(MediaType.APPLICATION_JSON)).andDo(MockMvcResultHandlers.print());
    }

}
