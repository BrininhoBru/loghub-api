package io.loghub.loghub_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.loghub.loghub_api.dto.LogEvent;
import io.loghub.loghub_api.dto.LogLevel;
import io.loghub.loghub_api.dto.SdkInfo;
import io.loghub.loghub_api.repository.LogEventRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class LogControllerTest {

    private static final String API_KEY_HEADER = "X-API-KEY";
    private static final String VALID_API_KEY = "test-api-key";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LogEventRepository repository;

    @BeforeEach
    void setUp() {
        repository.deleteAll();
    }

    @Test
    @DisplayName("POST /api/logs - Should return 401 when API key is missing")
    void shouldReturn401WhenApiKeyMissing() throws Exception {
        LogEvent logEvent = createValidLogEvent();

        mockMvc.perform(post("/api/logs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logEvent)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Missing API Key"));
    }

    @Test
    @DisplayName("POST /api/logs - Should return 401 when API key is invalid")
    void shouldReturn401WhenApiKeyInvalid() throws Exception {
        LogEvent logEvent = createValidLogEvent();

        mockMvc.perform(post("/api/logs")
                        .header(API_KEY_HEADER, "invalid-key")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logEvent)))
                .andExpect(status().isUnauthorized())
                .andExpect(jsonPath("$.error").value("Invalid API Key"));
    }

    @Test
    @DisplayName("POST /api/logs - Should create log event successfully")
    void shouldCreateLogEventSuccessfully() throws Exception {
        LogEvent logEvent = createValidLogEvent();

        mockMvc.perform(post("/api/logs")
                        .header(API_KEY_HEADER, VALID_API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(logEvent)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.application").value("test-app"))
                .andExpect(jsonPath("$.environment").value("dev"))
                .andExpect(jsonPath("$.level").value("ERROR"))
                .andExpect(jsonPath("$.message").value("Test error message"));
    }

    @Test
    @DisplayName("POST /api/logs - Should return 400 when required fields are missing")
    void shouldReturn400WhenRequiredFieldsMissing() throws Exception {
        String invalidPayload = """
                {
                    "application": "test-app"
                }
                """;

        mockMvc.perform(post("/api/logs")
                        .header(API_KEY_HEADER, VALID_API_KEY)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidPayload))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").value("Validation Failed"));
    }

    @Test
    @DisplayName("GET /api/logs - Should return paginated logs")
    void shouldReturnPaginatedLogs() throws Exception {
        // Create some logs first
        for (int i = 0; i < 5; i++) {
            LogEvent logEvent = createValidLogEvent();
            mockMvc.perform(post("/api/logs")
                    .header(API_KEY_HEADER, VALID_API_KEY)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(logEvent)));
        }

        mockMvc.perform(get("/api/logs")
                        .header(API_KEY_HEADER, VALID_API_KEY)
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content", hasSize(5)))
                .andExpect(jsonPath("$.page").value(0))
                .andExpect(jsonPath("$.totalElements").value(5));
    }

    @Test
    @DisplayName("GET /api/logs - Should filter by application")
    void shouldFilterByApplication() throws Exception {
        // Create log with specific application
        LogEvent logEvent = new LogEvent(
                "specific-app",
                "prod",
                LogLevel.WARN,
                "Warning message",
                Instant.now(),
                null,
                null,
                null
        );

        mockMvc.perform(post("/api/logs")
                .header(API_KEY_HEADER, VALID_API_KEY)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(logEvent)));

        mockMvc.perform(get("/api/logs")
                        .header(API_KEY_HEADER, VALID_API_KEY)
                        .param("application", "specific-app"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].application").value("specific-app"));
    }

    @Test
    @DisplayName("GET /api/logs - Should filter by level")
    void shouldFilterByLevel() throws Exception {
        // Create ERROR log
        LogEvent errorLog = new LogEvent(
                "test-app",
                "dev",
                LogLevel.ERROR,
                "Error message",
                Instant.now(),
                null,
                null,
                null
        );

        // Create INFO log
        LogEvent infoLog = new LogEvent(
                "test-app",
                "dev",
                LogLevel.INFO,
                "Info message",
                Instant.now(),
                null,
                null,
                null
        );

        mockMvc.perform(post("/api/logs")
                .header(API_KEY_HEADER, VALID_API_KEY)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(errorLog)));

        mockMvc.perform(post("/api/logs")
                .header(API_KEY_HEADER, VALID_API_KEY)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(infoLog)));

        mockMvc.perform(get("/api/logs")
                        .header(API_KEY_HEADER, VALID_API_KEY)
                        .param("level", "ERROR"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].level").value("ERROR"));
    }

    @Test
    @DisplayName("GET /health - Should return health status without API key")
    void shouldReturnHealthWithoutApiKey() throws Exception {
        mockMvc.perform(get("/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"));
    }

    private LogEvent createValidLogEvent() {
        return new LogEvent(
                "test-app",
                "dev",
                LogLevel.ERROR,
                "Test error message",
                Instant.now(),
                "trace-123",
                Map.of("userId", "user-456", "action", "login"),
                new SdkInfo("java", "1.0.0")
        );
    }
}

