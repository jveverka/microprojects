ARG ARCH="amd64"
FROM ${ARCH}/adoptopenjdk:11-jre
COPY build/libs/event-logger-service-1.0.0-SNAPSHOT.jar /event-logger-service.jar
COPY event-logger-start.sh /event-logger-start.sh
RUN apt update
RUN apt install -y netcat

# install metricbeat
RUN curl -L -O https://artifacts.elastic.co/downloads/beats/metricbeat/metricbeat-7.14.0-$(dpkg --print-architecture).deb
RUN dpkg -i metricbeat-7.14.0-$(dpkg --print-architecture).deb
COPY metricbeat.yml /etc/metricbeat/metricbeat.yml
RUN chmod go-w /etc/metricbeat/metricbeat.yml

RUN chmod +x /event-logger-start.sh
RUN mkdir -p /opt/data
COPY src/main/resources/application-cloud.yml /opt/data/application-cloud.yml
ENV XMX 128m
ENV SERVER_PORT 8090
ENV APP_CONFIG_PATH false
ENTRYPOINT ["/event-logger-start.sh"]
