package io.loghub.loghub_api.service;

import io.loghub.loghub_api.dto.*;
import io.loghub.loghub_api.entity.LogEventEntity;
import io.loghub.loghub_api.mapper.LogEventMapper;
import io.loghub.loghub_api.repository.LogEventRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@Service
public class LogEventService {

    private final LogEventRepository repository;
    private final LogEventMapper mapper;

    public LogEventService(LogEventRepository repository, LogEventMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Transactional
    public LogEventResponse ingest(LogEvent logEvent) {
        LogEventEntity entity = mapper.toEntity(logEvent);
        LogEventEntity saved = repository.save(entity);
        return mapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public PageResponse<LogEventResponse> search(
            String application,
            String environment,
            LogLevel level,
            Instant from,
            Instant to,
            int page,
            int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<LogEventEntity> result = repository.findWithFilters(
                application,
                environment,
                level,
                from,
                to,
                pageable
        );

        return new PageResponse<>(
                result.getContent().stream()
                        .map(mapper::toResponse)
                        .toList(),
                result.getNumber(),
                result.getSize(),
                result.getTotalElements(),
                result.getTotalPages()
        );
    }
}

