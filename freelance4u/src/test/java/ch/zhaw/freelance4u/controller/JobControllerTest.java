package ch.zhaw.freelance4u.controller;

import ch.zhaw.freelance4u.repository.CompanyRepository;
import ch.zhaw.freelance4u.security.TestSecurityConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.model.Generation;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Import(TestSecurityConfig.class)
public class JobControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CompanyRepository companyRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OpenAiChatModel chatModel;

    private static String jobId;
    private static String companyId;

    private String getCompanyId() {
        return companyRepository.findAll().get(0).getId();
    }

    @Test
    @Order(1)
    public void testCreateJob() throws Exception {
        companyId = getCompanyId();

        // Mock AI response for title generation
        ChatResponse mockChatResponse = new ChatResponse(
                java.util.List.of(new Generation("Improved Test Job")));
        when(chatModel.call(any(Prompt.class))).thenReturn(mockChatResponse);

        String jobJson = """
                {
                    "title": "Test Job",
                    "description": "Test Description",
                    "jobType": "IMPLEMENT",
                    "earnings": 100.0,
                    "companyId": "%s"
                }
                """.formatted(companyId);

        MvcResult result = mockMvc.perform(post("/api/job")
                .header("Authorization", "Bearer admin")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jobJson))
                .andExpect(status().isCreated())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        jobId = jsonNode.get("id").asText();
    }

    @Test
    @Order(2)
    public void testGetJobById() throws Exception {
        mockMvc.perform(get("/api/job/" + jobId)
                .header("Authorization", "Bearer user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("Improved Test Job"))
                .andExpect(jsonPath("$.description").value("Test Description"))
                .andExpect(jsonPath("$.companyId").value(companyId));
    }

    @Test
    @Order(3)
    public void testDeleteJobById() throws Exception {
        mockMvc.perform(delete("/api/job/" + jobId)
                .header("Authorization", "Bearer admin"))
                .andExpect(status().isOk())
                .andExpect(content().string("DELETED"));
    }

    @Test
    @Order(4)
    public void testGetJobByIdAfterDeletion() throws Exception {
        mockMvc.perform(get("/api/job/" + jobId)
                .header("Authorization", "Bearer user"))
                .andExpect(status().isNotFound());
    }
}
