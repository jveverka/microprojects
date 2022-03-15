# reverse-proxy
NGINX reverse proxy which downloads it's configuration and certificates from [config-server](../config-server). 
So the configuration file is not embedded in docker file, 
nor has to be mounted on container startup. See [start-nginx.sh](start-nginx.sh).

### Git configuration
* Expected directory structure (cer and key file is used only if USE_TLS=true):
  ```
  reverse-proxy/nginx.conf
  reverse-proxy/server.cer
  reverse-proxy/server.key
  ```
* Expected ``nginx.conf`` content:
  ```
  server {

    #listen 80 default_server; # if USE_TLS=false
    listen 443 ssl default_server;
    ssl_certificate /etc/nginx/http.d/server.cer;
    ssl_certificate_key /etc/nginx/http.d/server.key;

    server_name <server-name>;

    location /path/ {
      ...
    }
  }
  ```

## Build 
```
docker build -t jurajveverka/reverse-proxy:1.0.0 .
docker push jurajveverka/reverse-proxy:1.0.0
```

## Test - with TLS
```
docker run \
  -p 443:443 \
  -e USE_TLS=true \
  -e CS_SERVICE_NAME=reverse-proxy \
  -e CS_LABEL=main \
  -e CS_USERNAME=user \
  -e CS_PASSWORD=<config-server-password> \
  -e CS_BASE_URL=http://config-server:8888 \
  -it jurajveverka/reverse-proxy:1.0.0
```

## Test - without TLS
```
docker run \
  -p 80:80 \
  -e USE_TLS=false \
  -e CS_SERVICE_NAME=reverse-proxy \
  -e CS_LABEL=main \
  -e CS_USERNAME=user \
  -e CS_PASSWORD=<config-server-password> \
  -e CS_BASE_URL=http://config-server.iee.intern:8888 \
  -it jurajveverka/reverse-proxy:1.0.0
```
