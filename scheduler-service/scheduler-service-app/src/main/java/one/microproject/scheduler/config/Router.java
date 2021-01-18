package one.microproject.scheduler.config;

import one.microproject.scheduler.dto.JobId;
import one.microproject.scheduler.dto.ScheduleJobRequest;
import one.microproject.scheduler.dto.ScheduledJobInfo;
import one.microproject.scheduler.dto.TaskInfo;
import one.microproject.scheduler.service.PeriodicSchedulerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;

@Configuration
public class Router {

    @Bean
    public RouterFunction<ServerResponse> route(PeriodicSchedulerService periodicSchedulerService) {
        return RouterFunctions
                .route(GET("/services/tasks/types").and(accept(APPLICATION_JSON)), request -> {
                    Flux<TaskInfo> types = periodicSchedulerService.getTypes();
                    return ServerResponse.ok().body(types, TaskInfo.class);
                })
                .andRoute(POST("/services/tasks/schedule").and(accept(APPLICATION_JSON)), request -> {
                    Mono<ScheduleJobRequest> monoBody = request.bodyToMono(ScheduleJobRequest.class);
                    Mono<JobId> genericResponseMono = monoBody.flatMap(periodicSchedulerService::schedule);
                    return ServerResponse.ok().body(genericResponseMono, JobId.class);
                })
                .andRoute(GET("/services/jobs").and(accept(APPLICATION_JSON)), request -> {
                    Flux<ScheduledJobInfo> dataSeriesInfoMono = periodicSchedulerService.getScheduledJobs();
                    return ServerResponse.ok().body(dataSeriesInfoMono, ScheduledJobInfo.class);
                })
                .andRoute(PUT("/services/jobs/cancel/{job-id}").and(accept(APPLICATION_JSON)), request -> {
                    JobId jobId = JobId.from(request.pathVariable("job-id"));
                    Mono<JobId> mono = periodicSchedulerService.cancel(jobId);
                    return ServerResponse.ok().body(mono, JobId.class);
                });
    }

}
