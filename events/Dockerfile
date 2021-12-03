ARG ARCH="amd64"
FROM ${ARCH}/amazoncorretto:17

COPY build/libs/events-*-SNAPSHOT.jar /events-service.jar
COPY src/main/resources/application-cloud.yml /application-cloud.yml

ENV XMX 128m
ENTRYPOINT ["/events-start.sh"]
