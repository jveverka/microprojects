# Scheduler Service

This project is __WIP__ !

## Implemented features
* Schedule periodic repetitive jobs.
* Schedule one-time jobs.
  
## Planned features
* Uses OAuth2 security model provided by [iam-service](https://github.com/jveverka/iam-service).

## Components
* [scheduler-common](scheduler-common)
* [scheduler-service-app](scheduler-service-app)
* [scheduler-task-example](scheduler-task-example)

### Run Environment in docker
```
docker-compose up -d
docker-compose down -v --rmi all --remove-orphans
```

## Rest endpoints
* __GET__ ``/services/tasks/types`` - get available task types.
* __POST__ ``/services/tasks/schedule`` - schedule job instance of task type.
* __GET__ ``/services/jobs`` - get scheduled/running jobs.
* __DELETE__ ``/services/jobs/{job-id}`` - cancel scheduled/running job.
* __GET__ ``/services/jobs/results`` - get all job results.
* __GET__ ``/services/jobs/results/{job-id}`` - get job result.
* __DELETE__ ``/services/jobs/results/{job-id}`` - delete job result.

## Adding external task implementation
The __scheduler-service__ is able to load an implementation of
``JobProvider`` from external jar file.
1. Start scheduler-service with external implementation of ``JobProvider``
```
#!/bin/bash
java -cp scheduler-service-app-1.0.0-SNAPSHOT.jar \
     -Dloader.path=libs/<external-tak>.jar \
     -Dloader.main=one.microproject.scheduler.SchedulerApp org.springframework.boot.loader.PropertiesLauncher \
     --spring.config.location=file:custom-application.yml
```
2. Add classpath to external implementation of ``JobProvider`` to custom-application.yml
```
app:
  providers:
    - classpath.to.implementation.CustomJobProvider
```
