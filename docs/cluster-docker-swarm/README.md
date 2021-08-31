# Docker Swarm deployments

![reference architecture](docker-swarm-reference-architecture.svg)

## Swarm container monitoring
[portainer.io](https://www.portainer.io/) 

## Log Collection 
* dockerized microservices are writing logs into stdout
* containers -> /var/lib/docker/containers/*/*.log -> filebeat -> elasticsearch
* log rotation is setup as local docker daemon property 
  * Navigate to the /etc/docker/daemon.json path.
  * Set the parameter log-driver with the name of logging driver. By default, log-driver is set to json-file.
    For example, "log-driver": "json-file"
  * To configure maximum size of logs and number of log files use the key log-opts in daemon.json:
    "log-driver": "json-file", "log-opts": { "max-size": "10m", "max-file": "3" }

## Metrics Collection

### References
* [Metricbeat OS  Data into ES](https://www.baeldung.com/ops/os-data-into-elastic-stack)
* [Beats](https://www.elastic.co/beats/)
* [Structured Logging](https://www.innoq.com/en/blog/structured-logging/)
* [Monitor Docker Swarm and Other Logs](https://dzone.com/articles/centralize-logging-with-docker-swarm-logstash-and)

