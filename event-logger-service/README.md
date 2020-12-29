# Event Logger Service
This project is __WIP__ !

* Logs DataRecord(s) for DataSeries.
* Provides event history and statistics.
* Uses OAuth2 security model provided by [iam-service](https://github.com/jveverka/iam-service).

### Build, Test & Run 
```
gradle clean build test
java -jar build/libs/event-logger-service-0.0.1-SNAPSHOT.jar 
```

### Run Environment in docker
```
docker-compose up -d
docker-compose down -v --rmi all --remove-orphans
```