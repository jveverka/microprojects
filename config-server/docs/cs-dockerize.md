# How to dockerize Config-Server

```
export VERSION=1.0.1
gradle clean build test

# on x86 AMD64 device:
docker build -t jurajveverka/config-server:${VERSION}-amd64 --build-arg ARCH=amd64 --file Dockerfile .
docker push jurajveverka/config-server:${VERSION}-amd64

# on ARM64 v8 device:
docker build -t jurajveverka/config-server:${VERSION}-arm64v8 --build-arg ARCH=amd64 --file Dockerfile .
docker push jurajveverka/config-server:${VERSION}-arm64v8

# on x86 AMD64 device: 
docker manifest create \
jurajveverka/config-server:${VERSION} \
--amend jurajveverka/config-server:${VERSION}-amd64 \
--amend jurajveverka/config-server:${VERSION}-arm64v8

docker manifest push jurajveverka/config-server:${VERSION}
```