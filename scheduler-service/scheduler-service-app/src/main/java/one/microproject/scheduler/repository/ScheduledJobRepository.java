package one.microproject.scheduler.repository;

import one.microproject.scheduler.model.ScheduledJob;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduledJobRepository extends ReactiveCrudRepository<ScheduledJob, String>  {
}
