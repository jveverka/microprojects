#!/bin/sh

CONFIG_URL=${CS_BASE_URL}/application/default/uat/reverse-proxy/nginx.conf
CERT_BASE_URL=${CS_BASE_URL}/application/default/uat/reverse-proxy

rm /etc/nginx/http.d/default.conf
echo "Downloading NGINX config from: ${CONFIG_URL}"
curl -sS --user ${CS_USERNAME}:${CS_PASSWORD} ${CONFIG_URL} -o /etc/nginx/http.d/default.conf
echo "CURL: $?"

if [ ${USE_TLS} = "true" ]; then
  echo "Downloading server certs from: ${CERT_BASE_URL}"
  curl -sS --user ${CS_USERNAME}:${CS_PASSWORD} ${CERT_BASE_URL}/server.cer -o /etc/nginx/http.d/server.cer
  echo "CURL: $?"
  curl -sS --user ${CS_USERNAME}:${CS_PASSWORD} ${CERT_BASE_URL}/server.key -o /etc/nginx/http.d/server.key
  echo "CURL: $?"
else
   echo "USE_TLS = ${USE_TLS}"
fi

#/usr/sbin/nginx -g 'daemon off;'
/usr/sbin/nginx -g "daemon off;error_log /dev/stdout debug;"
#/usr/sbin/nginx-debug -g "daemon off;error_log /dev/stdout debug;"