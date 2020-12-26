package one.microproject.logger.repository;

import one.microproject.logger.model.DataRecord;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataRecordRepository extends ReactiveCrudRepository<DataRecord, String> {
}
