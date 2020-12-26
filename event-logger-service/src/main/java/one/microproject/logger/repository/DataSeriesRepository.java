package one.microproject.logger.repository;

import one.microproject.logger.model.DataSeries;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DataSeriesRepository extends ReactiveCrudRepository<DataSeries, String> {
}
