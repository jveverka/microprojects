package one.microproject.scheduler.service;

import one.microproject.scheduler.dto.JobId;
import one.microproject.scheduler.dto.JobResultInfo;
import one.microproject.scheduler.dto.JobStatus;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


public interface ResultService {

    Flux<JobResultInfo> getAll();

    Mono<JobId> save(JobResultInfo result);

    Mono<JobResultInfo> get(JobId jobId);

    Mono<JobId> delete(JobId jobId);

    Mono<JobResultInfo> statusUpdate(JobId jobId, JobStatus status);

}
