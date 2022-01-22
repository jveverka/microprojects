# Config-Server
Microservice serving springboot and other configurations in k8s, 
docker swarm and other cloud deployments. This project is based on [Spring Cloud Config](https://cloud.spring.io/spring-cloud-config/reference/html/).

## Run with docker
```
docker run -d --name config-service \
  --restart unless-stopped \
  -v "/mnt/data/config-server:/opt/config-server" \
  -e PORT=8080 \
  -e CS_DEFAULT_LABEL=main \
  -e CS_GIT_URL=file://opt/config-server \
  -e CS_PASSWORD=supersecret \
  -p 8085:8085 jurajveverka/config-service:1.0.1
```

### Volume Mappings

| * Volume Path        | CS_GIT_URL                 |
|----------------------|----------------------------|
| "/opt/config-server" | "file://opt/config-server" |

### File Mappings
| Repository Path         | URL                                                                     |
|-------------------------|-------------------------------------------------------------------------|
| nginx-config/nginx.conf | http://<server>:<port>/application/default/main/nginx-config/nginx.conf |

## Build
* Local build: ``gradle clean build test``
* [Dockerize Config-Server](docs/cs-dockerize.md).
