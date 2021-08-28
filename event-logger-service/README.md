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
java -jar build/libs/event-logger-service-1.0.0-SNAPSHOT.jar 
```

### Run infrastructure in docker-compose
```
docker-compose -f docker-compose-infra.yml up -d
docker-compose -f docker-compose-infra.yml down -v --rmi all --remove-orphans
```

### Create docker image
```
docker build -t jurajveverka/event-logger:1.0.0-SNAPSHOT --file Dockerfile .
docker push jurajveverka/event-logger:1.0.0-SNAPSHOT
```

### Run stack in docker-compose
```
docker-compose up -d
docker-compose down -v --rmi all --remove-orphans
```

### Setup iam-service (create project users)
1. Run project setup script ``tools/project-setup-create.sh``
2. Get project user access token.
   ```
   USER_ACCESS_TOKEN=`curl -s --request POST 'http://localhost:8080/auth/services/oauth2/backend-services/microprojects/token?grant_type=password&username=juraj&password=secret&scope=&client_id=mp-client&client_secret=mp-client-secret' \
   --header 'Content-Type: application/x-www-form-urlencoded' | jq -r ".access_token"`
   echo "USER_ACCESS_TOKEN=${USER_ACCESS_TOKEN}"
   ``` 
