## Setup  local docker registry

1. Start local insecure docker registry (http) on ``{docker-registry-host}``
   ```
   docker run -d -p 5000:5000 --restart=always --name docker-registry registry:2
   ```

2. On all client machines: add insecure registry entry into ``/etc/docker/daemon.json`` 
   ```
   {
      "insecure-registries" : [ "{docker-registry-host}:5000" ]
   }
   ```
   Restart local docker daemon:
   ```
   sudo systemctl daemon-reload
   sudo systemctl restart docker
   ```

3. Create docker image and push to local registry
   ```
   docker build -t {docker-registry-host}:5000/{image-name}:{tag} --file Dockerfile .
   docker push {docker-registry-host}:5000/{image-name}:{tag}
   ```

4. Pull created image(s) from docker registry
   ```
   docker pull {docker-registry-host}:5000/{image-name}:{tag}
   ```
   On {docker-registry-host} you may use command
   ```
   docker pull localhost:5000/{image-name}:{tag}
   ```
5. Run docker container
   ```
   docker run -d ... {docker-registry-host}:5000/{image-name}:{tag}
   docker run -d ... localhost:5000/{image-name}:{tag}
   ```
   