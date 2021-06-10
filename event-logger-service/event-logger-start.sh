#!/bin/sh

echo "starting event-logger ..."
echo "APP_CONFIG_PATH=${APP_CONFIG_PATH}"

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
