# Event Logger Service
This project is __WIP__ !

* Logs DataRecord(s) for DataSeries.
* Provides event history and statistics.
* Uses OAuth2 security model provided by [iam-service](https://github.com/jveverka/iam-service).

### Rest APIs
#### Data Series - APIs
* __GET__ ``/services/series``
* __POST__ ``/services/series``
* __DELETE__ ``/services/series/{groupId}/{name}``
* __GET__ ``/services/series/{groupId}/{name}`` 

#### Data Records - APIs
* __GET__ ``/services/records/{groupId}/{name}``
* __GET__ ``/services/records/{groupId}/{name}/{startTime}/{duration}``
* __POST__ ``/services/records``
* __PUT__ ``/services/records``
* __DELETE__ ``/services/records/{groupId}/{name}/{timeStamp}``

### Build, Test & Run 
```
gradle clean build test
java -jar build/libs/event-logger-service-0.0.1-SNAPSHOT.jar 
```

### Run infrastructure in docker
```
docker-compose -f docker-compose-infra.yml up -d
docker-compose -f docker-compose-infra.yml down -v --rmi all --remove-orphans
```

### Create docker image
```
docker build -t jurajveverka/event-logger:1.0.0-SNAPSHOT --file Dockerfile .
docker push jurajveverka/event-logger:1.0.0-SNAPSHOT

docker run --name event-logger:1.0.0-SNAPSHOT \
      --restart unless-stopped \
      -e APP_CONFIG_PATH=/opt/data/application-cloud.yml \
      -e XMX=128m \
      -v 'pwd':/opt/data \
      --network host \
      -d -p 8090:8090 jurajveverka/event-logger:1.0.0-SNAPSHOT

docker-compose up -d
docker-compose down -v --rmi all --remove-orphans
```
