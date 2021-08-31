## App Development
* Springboot / Java 11 or later, as base framework for microservices.
* Testing - all tests must be executable on developer's PC.
  * Junit 5 / TestNG for unit testing.
  * testcontainers for junit / integration testing.
* Docker as application binary delivery.
  * Injectable external configuration, env. variables, mountable  application.yml
* Docker-compose to run simple multi-service deployments.
* Monitoring
  * logs: ELK stack
  * metrics: Prometheus and grafana