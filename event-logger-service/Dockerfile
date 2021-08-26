ARG ARCH="amd64"
FROM ${ARCH}/adoptopenjdk:11-jre
COPY build/libs/event-logger-service-1.0.0-SNAPSHOT.jar /event-logger-service.jar
COPY event-logger-start.sh /event-logger-start.sh
RUN chmod +x /event-logger-start.sh
RUN mkdir -p /opt/data
COPY src/main/resources/application-cloud.yml /opt/data/application-cloud.yml
ENV XMX 128m
ENV SERVER_PORT 8090
ENV APP_CONFIG_PATH false
ENTRYPOINT ["/event-logger-start.sh"]
