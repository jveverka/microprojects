# reverse-proxy
NGINX reverse proxy which downloads it's configuration and certificates from [config-server](../config-server). 
So the configuration file is not embedded in docker file, nor has to be mounted in startup.

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

    listen 443 ssl default_server;
    ssl_certificate /etc/nginx/http.d/server.cer;
    ssl_certificate_key /etc/nginx/http.d/server.key;

    server_name <server-name>;

    location /path/ {
      ...
    }
  }
  ```

## Build and test
```
docker build -t jurajveverka/reverse-proxy:1.0.0 .
docker run \
  -p 80:80 \
  -e USE_TLS=false \
  -e CS_USERNAME=user \
  -e CS_PASSWORD=eCkPfhx9ZjVpusZv \
  -e CS_BASE_URL=http://config-server:8080 \
  -it jurajveverka/reverse-proxy:1.0.0
docker push jurajveverka/reverse-proxy:1.0.0
```
