#!/bin/bash

echo "starting event-logger ..."
echo "XMX=${XMX}"
java -Xms32m -Xmx${XMX} -jar /events-service.jar --spring.config.location=file:/application-cloud.yml
