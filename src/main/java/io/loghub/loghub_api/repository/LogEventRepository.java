package io.loghub.loghub_api.repository;

import io.loghub.loghub_api.dto.LogLevel;
import io.loghub.loghub_api.entity.LogEventEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface LogEventRepository extends JpaRepository<LogEventEntity, Long> {

    @Query("""
            SELECT e FROM LogEventEntity e
            WHERE (:application IS NULL OR e.application = :application)
              AND (:environment IS NULL OR e.environment = :environment)
              AND (:level IS NULL OR e.level = :level)
              AND (:from IS NULL OR e.timestamp >= :from)
              AND (:to IS NULL OR e.timestamp <= :to)
            ORDER BY e.timestamp DESC
            """)
    Page<LogEventEntity> findWithFilters(
            @Param("application") String application,
            @Param("environment") String environment,
            @Param("level") LogLevel level,
            @Param("from") Instant from,
            @Param("to") Instant to,
            Pageable pageable
    );
}

