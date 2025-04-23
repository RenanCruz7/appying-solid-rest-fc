package task.manajer.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void getTaskStats_ShouldReturnStats() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/stats"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.todoCount").isNumber())
                .andExpect(jsonPath("$.doingCount").isNumber())
                .andExpect(jsonPath("$.doneCount").isNumber())
                .andExpect(jsonPath("$.averagePriority").isNumber());
    }

    @Test
    void searchTasksByName_ShouldReturnMatchingTasks() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/tasks/search")
                        .param("name", "Projeto")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.content").isArray());
    }
}