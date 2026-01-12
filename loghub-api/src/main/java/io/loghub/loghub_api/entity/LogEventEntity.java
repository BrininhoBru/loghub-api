package io.loghub.loghub_api.entity;

import io.loghub.loghub_api.dto.LogLevel;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "log_events", indexes = {
        @Index(name = "idx_application", columnList = "application"),
        @Index(name = "idx_environment", columnList = "environment"),
        @Index(name = "idx_level", columnList = "level"),
        @Index(name = "idx_timestamp", columnList = "timestamp")
})
public class LogEventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String application;

    @Column(nullable = false)
    private String environment;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LogLevel level;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;

    @Column(nullable = false)
    private Instant timestamp;

    private String traceId;

    @Column(columnDefinition = "TEXT")
    private String metadata;

    private String sdkLanguage;

    private String sdkVersion;

    public LogEventEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApplication() {
        return application;
    }

    public void setApplication(String application) {
        this.application = application;
    }

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public LogLevel getLevel() {
        return level;
    }

    public void setLevel(LogLevel level) {
        this.level = level;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getMetadata() {
        return metadata;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public String getSdkLanguage() {
        return sdkLanguage;
    }

    public void setSdkLanguage(String sdkLanguage) {
        this.sdkLanguage = sdkLanguage;
    }

    public String getSdkVersion() {
        return sdkVersion;
    }

    public void setSdkVersion(String sdkVersion) {
        this.sdkVersion = sdkVersion;
    }
}

