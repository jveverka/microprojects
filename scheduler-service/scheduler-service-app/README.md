# Scheduler Service

This project is __WIP__ !

* Collects data from other services.
* Automatically schedules and re-schedules events. 
* Uses OAuth2 security model provided by [iam-service](https://github.com/jveverka/iam-service).

## Rest endpoints
* __GET__ ``/services/tasks/types``
* __POST__ ``/services/tasks/schedule``
* __GET__ ``/services/jobs``  
* __PUT__ ``/services/jobs/cancel/{job-id}``

