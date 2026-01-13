package io.loghub.loghub_api.dto;

import jakarta.validation.constraints.NotBlank;

public record SdkInfo(
        @NotBlank(message = "SDK language is required")
        String language,

        @NotBlank(message = "SDK version is required")
        String version
) {
}

