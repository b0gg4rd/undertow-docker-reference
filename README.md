# Undertow - Docker Reference

A project reference for **Undertow** with **Docker** implementing a microservice.

## Requirements

  - Docker 18

## Building

### Integration tests

The development cycle should consist in the creation of **integration test cases**. Use the below command to execute them:

```bash
$ docker run \
    -u $(id -u):$(grep docker /etc/group | awk -F\: '{print $3}') \
    --net=host \
    --rm \
    -w $(pwd) \
    -v /etc/group:/etc/group:ro \
    -v /etc/passwd:/etc/passwd:ro \
    -v $(pwd):$(pwd) \
    -v ${HOME}/.m2:${HOME}/.m2 \
    -v /var/run/docker.sock:/var/run/docker.sock \
    openjdk:11.0.4-jdk-slim \
    ./mvnw -Djansi.force=true -P local -U clean verify
```

### Local Execution

For execute the _microservice_ use:

```bash
$ docker run \
    -u $(id -u):$(grep docker /etc/group | awk -F\: '{print $3}') \
    --net=host \
    --rm \
    -w $(pwd) \
    -v /etc/group:/etc/group:ro \
    -v /etc/passwd:/etc/passwd:ro \
    -v $(pwd):$(pwd) \
    -v ${HOME}/.m2:${HOME}/.m2 \
    -v /var/run/docker.sock:/var/run/docker.sock \
    openjdk:11.0.4-jdk-slim \
    ./mvnw -Djansi.force=true -P local -U clean package docker:build docker:start
```

