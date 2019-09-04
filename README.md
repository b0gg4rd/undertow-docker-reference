# Undertow - Project Reference

A project reference for **Undertow** with **Docker** implementing a microservice.

## Requirements

  - JDK 1.8
  - Docker 18

## Building

### Integration Tests

The development cycle should consist in the creation of **integration test cases**. Use the below command to execute them:

```bash
$ ./mvnw -P local -U clean verify
```

### Local Execution

For execute the _microservice_ use:

```bash
$ ./mvnw -P local -U clean package docker:build docker:start
```

For stop the _microservice_ use:

```bash
$ ./mvnw -P local docker:stop
```

