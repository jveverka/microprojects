# Events Service

## Build docker
```
export VERSION=1.0.0
# on x86 AMD64 device:
docker build -t jurajveverka/events-service:${VERSION}-amd64 --build-arg ARCH=amd64 --file Dockerfile .
docker push jurajveverka/events-service:${VERSION}-amd64

# on ARM64 v8 device:
docker build -t jurajveverka/events-service:${VERSION}-arm64v8 --build-arg ARCH=amd64 --file Dockerfile .
docker push jurajveverka/events-service:${VERSION}-arm64v8

# on x86 AMD64 device: 
docker manifest create \
jurajveverka/events-service:${VERSION} \
--amend jurajveverka/events-service:${VERSION}-amd64 \
--amend jurajveverka/events-service:${VERSION}-arm64v8

docker manifest push jurajveverka/events-service:${VERSION}
```

## Run Docker
```
docker run -d --name events-service \
  --restart unless-stopped \
  -e PORT=8085 \
  -e ELASTIC_HOST=localhost \
  -e ELASTIC_PORT=9200 \
  -e ELASTIC_USER=elastic \
  -e ELASTIC_PASS=secret \
  -p 8085:8085 jurajveverka/events-service:1.0.0
```

### References
* [Spring Data Elasticsearch](https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/#reference)
