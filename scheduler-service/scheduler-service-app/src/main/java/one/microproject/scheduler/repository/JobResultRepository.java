package one.microproject.scheduler.repository;

import one.microproject.scheduler.model.JobResult;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobResultRepository extends ReactiveCrudRepository<JobResult, String>  {
}
