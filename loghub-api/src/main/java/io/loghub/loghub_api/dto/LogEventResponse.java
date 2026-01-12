package io.loghub.loghub_api.dto;

import java.time.Instant;
import java.util.Map;

public record LogEventResponse(
        Long id,
        String application,
        String environment,
        LogLevel level,
        String message,
        Instant timestamp,
        String traceId,
        Map<String, Object> metadata,
        SdkInfo sdk
) {
}
