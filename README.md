# Undertow - Docker Reference

A project reference for **Undertow** with **Docker** implementing a microservice.

## Requirements

  - Docker 18

## Building

### Integration tests

The development cycle should consist in the creation of **integration test cases**. Use the below command to execute them:

```bash
$ docker run \
    -u $(id -u ${USER}):$(id -g ${USER}) \
    --net=host \
    --rm \
    -w /tmp/project \
    -e DOCKER_HOST=unix://var/run/docker.sock \
    --privileged=true \
    -v /etc/passwd:/etc/passwd \
    -v ${PWD}:/tmp/project \
    -v ${HOME}/.m2:${HOME}/.m2 \
    -v /var/run/docker.sock:/var/run/docker.sock:rw \
    openjdk:11.0.4-jdk-slim \
    ./mvnw -Djansi.force=true -P local -U clean verify
```

### Local Execution

For execute the _microservice_ use:

```bash
$ docker run \
    -u $(id -u ${USER}):$(id -g ${USER}) \
    --net=host \
    --rm \
    -w /tmp/project \
    -v /etc/passwd:/etc/passwd \
    -v ${PWD}:/tmp/project \
    -v ${HOME}/.m2:${HOME}/.m2 \
    openjdk:11.0.4-jdk-slim \
    ./mvnw -Djansi.force=true -P local -U clean package docker:build docker:start
```

For stop the _microservice_ use:

```bash
$ docker run \
    -u $(id -u ${USER}):$(id -g ${USER}) \
    --net=host \
    --rm \
    -w /tmp/project \
    -v /etc/passwd:/etc/passwd \
    -v ${PWD}:/tmp/project \
    -v ${HOME}/.m2:${HOME}/.m2 \
    openjdk:11.0.4-jdk-slim \
    ./mvnw -Djansi.force=true -P local docker:stop
```

