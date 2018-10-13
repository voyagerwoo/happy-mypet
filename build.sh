#!/usr/bin/env bash

./mvnw clean package && \
    docker build -f src/main/docker/Dockerfile -t happy-mypet .