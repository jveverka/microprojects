package one.microproject.scheduler.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import one.microproject.scheduler.dto.JobId;
import one.microproject.scheduler.dto.JobResultInfo;
import one.microproject.scheduler.dto.JobStatus;
import one.microproject.scheduler.model.JobResult;
import one.microproject.scheduler.repository.JobResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;


@Service
@Transactional(readOnly = true)
public class ResultServiceImpl implements ResultService {

    private final JobResultRepository jobResultRepository;
    private final ObjectMapper mapper;

    @Autowired
    public ResultServiceImpl(JobResultRepository jobResultRepository) {
        this.jobResultRepository = jobResultRepository;
        this.mapper = new ObjectMapper();
    }

    @Override
    public Flux<JobResultInfo> getAll() {
        return jobResultRepository.findAll().transform(flux -> flux.map(this::transform));
    }

    @Override
    @Transactional
    public Mono<JobId> save(JobResultInfo result) {
        JobResult jobResult = transform(result);
        return jobResultRepository.save(jobResult).transform(mono -> mono.map( m -> result.getId()));
    }

    @Override
    public Mono<JobResultInfo> get(JobId jobId) {
        return jobResultRepository.findById(jobId.getId()).transform(mono -> mono.map(this::transform));
    }

    @Override
    @Transactional
    public Mono<JobId> delete(JobId jobId) {
        Mono<Void> deleted = jobResultRepository.deleteById(jobId.getId());
        return deleted.transform(mono -> mono.map( m -> jobId));
    }

    @Override
    @Transactional
    public Mono<JobResultInfo> statusUpdate(JobId jobId, JobStatus status) {
        jobResultRepository.findById(jobId.getId()).subscribe(r -> {
            r.setStatus(status);
            jobResultRepository.save(r).subscribe();
        });
        return jobResultRepository.findById(jobId.getId()).transform(mono -> mono.map(this::transform));
    }

    private JobResultInfo transform(JobResult jobResult) throws JsonFormatException {
        try {
            return new JobResultInfo(JobId.from(jobResult.getId()), jobResult.getTaskType(),
                    jobResult.getStartedTimeStamp(), jobResult.getDuration(), jobResult.getStatus(), jobResult.getCode(),
                    mapper.readTree(jobResult.getResult()));
        } catch (IOException e) {
            throw new JsonFormatException(e);
        }
    }

    private JobResult transform(JobResultInfo jobResultInfo) throws JsonFormatException {
        try {
            return new JobResult(jobResultInfo.getId().getId(), jobResultInfo.getTaskType(),
                    jobResultInfo.getStartedTimeStamp(), jobResultInfo.getDuration(), jobResultInfo.getStatus(), jobResultInfo.getCode(),
                    mapper.writeValueAsString(jobResultInfo.getResult()));
        } catch (IOException e) {
            throw new JsonFormatException(e);
        }
    }

}
