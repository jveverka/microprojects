package one.microproject.scheduler.model;

import one.microproject.scheduler.repository.ScheduledJob;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduledJobRepository extends ReactiveCrudRepository<ScheduledJob, String>  {
}
