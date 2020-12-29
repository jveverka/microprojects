# Compatible with Architecture: x86_64 systems (Intel, AMD)

# https://hub.docker.com/r/adoptopenjdk/openjdk11
FROM adoptopenjdk/openjdk11:alpine-jre
COPY build/libs/event-logger-service-0.0.1-SNAPSHOT.jar /event-logger-service-0.0.1-SNAPSHOT.jar
COPY event-logger-start.sh /event-logger-start.sh
RUN chmod +x /event-logger-start.sh
RUN mkdir -p /opt/data
ENV XMX 128m
ENV SERVER_PORT 8090
ENV APP_CONFIG_PATH false
ENTRYPOINT ["/event-logger-start.sh"]
