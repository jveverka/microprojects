# Docker Swarm deployments

![reference architecture](docker-swarm-reference-architecture.svg)

## Swarm container monitoring
[portainer.io](https://www.portainer.io/) 

## Log Collection 
containers -> /var/lib/docker/containers/*/*.log -> filebeat -> elasticsearch

## Metrics Collection
