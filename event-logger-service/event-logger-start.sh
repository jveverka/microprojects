#!/bin/bash

echo "starting event-logger ..."
echo "APP_CONFIG_PATH=${APP_CONFIG_PATH}"

echo "Waiting for MongoDB to launch on ${EL_MONGO_HOST}:${EL_MONGO_PORT} ..."
while ! nc -z ${EL_MONGO_HOST} ${EL_MONGO_PORT}; do
  sleep 0.5 # wait for 0.5s before check again
  echo -n "."
done
echo ""
echo "MongoDB launched !"

if [ -z ${ES_HOSTS+x} ]; then
  echo "ElasticSearch ES_HOSTS is not set, metricbeat start is skipped !"
else
  echo "ES_HOSTS='$ES_HOSTS', starting metricbeat !"
  metricbeat 2>&1 &
fi

if [ "${APP_CONFIG_PATH}" = "false" ]; then
  echo "using default configuration"
  echo "SERVER_PORT=${SERVER_PORT}"
  echo "XMX=${XMX}"
  java -Xms32m -Xmx${XMX} -jar /event-logger-service.jar
else
  echo "using custom configuration"
  echo "APP_CONFIG_PATH=${APP_CONFIG_PATH}"
  echo "XMX=${XMX}"
  java -Xms32m -Xmx${XMX} -jar /event-logger-service.jar \
     --spring.config.location=file:${APP_CONFIG_PATH}
fi
