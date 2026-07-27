package io.loghub.loghub_api.repository;

import io.loghub.loghub_api.dto.LogLevel;
import io.loghub.loghub_api.entity.LogEventEntity;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public final class LogEventSpecifications {

    private LogEventSpecifications() {
    }

    public static Specification<LogEventEntity> withFilters(
            String application,
            String environment,
            LogLevel level,
            Instant from,
            Instant to
    ) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (application != null) {
                predicates.add(cb.equal(root.get("application"), application));
            }
            if (environment != null) {
                predicates.add(cb.equal(root.get("environment"), environment));
            }
            if (level != null) {
                predicates.add(cb.equal(root.get("level"), level));
            }
            if (from != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("timestamp"), from));
            }
            if (to != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("timestamp"), to));
            }
            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
