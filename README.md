[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java11](https://img.shields.io/badge/java-11-blue)](https://img.shields.io/badge/java-11-blue)
[![Gradle](https://img.shields.io/badge/gradle-v6.5-blue)](https://img.shields.io/badge/gradle-v6.5-blue)
![Build and Test](https://github.com/jveverka/microproject/workflows/Build%20and%20Test/badge.svg)

# Microproject(s)
Collection of various micro-projects.

* [iam-service OAuth2 server](https://github.com/jveverka/iam-service)
* [event-logger-service](event-logger-service)
* [scheduler-engine](scheduler-service)
* file-server

### Infrastructure
* [Mongo setup](docs/mongo-setup.md)
* [Local Docker Registry](docs/local-docker-registry-setup.md)

### Build and Test
```
gradle clean install test publishToMavenLocal
```