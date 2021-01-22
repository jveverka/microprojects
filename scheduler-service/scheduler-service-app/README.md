# Scheduler Service

This project is __WIP__ !

* Collects data from other services.
* Automatically schedules and re-schedules events. 
* Uses OAuth2 security model provided by [iam-service](https://github.com/jveverka/iam-service).

### Run Environment in docker
```
docker-compose up -d
docker-compose down -v --rmi all --remove-orphans
```

## Rest endpoints
* __GET__ ``/services/tasks/types``
* __POST__ ``/services/tasks/schedule``
* __GET__ ``/services/jobs``  
* __DELETE__ ``/services/jobs/cancel/{job-id}``

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
