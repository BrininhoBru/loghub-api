package io.loghub.loghub_api.repository;

import io.loghub.loghub_api.entity.LogEventEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface LogEventRepository extends JpaRepository<LogEventEntity, Long>, JpaSpecificationExecutor<LogEventEntity> {
}
