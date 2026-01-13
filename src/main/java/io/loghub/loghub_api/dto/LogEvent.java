package io.loghub.loghub_api.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.Map;

public record LogEvent(
        @NotBlank(message = "Application is required")
        String application,

        @NotBlank(message = "Environment is required")
        String environment,

        @NotNull(message = "Level is required")
        LogLevel level,

        @NotBlank(message = "Message is required")
        String message,

        @NotNull(message = "Timestamp is required")
        Instant timestamp,

        String traceId,

        Map<String, Object> metadata,

        @Valid
        SdkInfo sdk
) {
}

