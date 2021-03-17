FROM adoptopenjdk/openjdk11:jre-11.0.8_10-alpine
COPY build/libs/schedule-service-1.0.0-SNAPSHOT.jar /schedule-service-1.0.0-SNAPSHOT.jar
ENTRYPOINT ["java", "-Xms32m", "-Xms128M", "-jar","/schedule-service-1.0.0-SNAPSHOT.jar"]
