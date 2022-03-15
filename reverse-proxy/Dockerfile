FROM alpine:3.15
LABEL org.opencontainers.image.authors="gergej123@gmail.com"

USER root
RUN apk update
RUN apk add curl
RUN apk add nginx
RUN apk add nginx-debug

COPY start-nginx.sh /start-nginx.sh
RUN mkdir -p /etc/nginx/conf.d
RUN chmod 755 /start-nginx.sh

ENV USE_TLS=false
ENV CS_SERVICE_NAME=reverse-proxy
ENV CS_APPLICATION=application
ENV CS_PROFILE=default
ENV CS_LABEL=uat

ENTRYPOINT ["/start-nginx.sh"]
