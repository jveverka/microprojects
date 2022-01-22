[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java11](https://img.shields.io/badge/java-17-blue)](https://img.shields.io/badge/java-17-blue)
[![Gradle](https://img.shields.io/badge/gradle-v7-blue)](https://img.shields.io/badge/gradle-v7-blue)
![Build and Test](https://github.com/jveverka/microproject/workflows/Build%20and%20Test/badge.svg)

# Microproject(s)
Collection of various micro-projects.

* [iam-service OAuth2 server](https://github.com/jveverka/iam-service)
* [event-logger-service](event-logger-service)
* [scheduler-engine](scheduler-service)
* file-server - TBD

### Local Infrastructure
* [Mongo setup](docs/local-infrastructure/mongo-setup.md)
* [Local Docker Registry](docs/local-infrastructure/local-docker-registry-setup.md)

### Application Clusters
Example flow of microservice application development and deployments for managed kubernetes clusters.
* [Application development flow](docs/app-development)
* [Docker Swarm deployment](docs/cluster-docker-swarm)
* [Kubernetes deployment](docs/cluster-kubernetes)

### Build and Test
```
gradle clean build test
```