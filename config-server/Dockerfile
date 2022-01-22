ARG ARCH="amd64"
FROM ${ARCH}/amazoncorretto:17

LABEL org.opencontainers.image.authors="gergej123@gmail.com"

COPY build/libs/config-*.jar /config-server.jar
ENV CS_DEFAULT_LABEL main
ENV CS_GIT_URI file://opt/config-server

ENTRYPOINT ["java", "-jar", "/config-server.jar"]
