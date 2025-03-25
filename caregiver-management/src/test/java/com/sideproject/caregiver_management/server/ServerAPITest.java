package com.sideproject.caregiver_management.server;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ServerAPITest {
    @Autowired
    private MockMvc mockMvc;

    @Test
    void testStatusAPI() throws Exception {
        ResultActions resultActions = mockMvc.perform(get("/api/v1/server/status"));

        resultActions.andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ok"));
    }
}