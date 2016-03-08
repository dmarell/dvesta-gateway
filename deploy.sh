#!/usr/bin/env bash
APP_ENV=$1
docker build -t dvesta-gateway .
docker rm -f dvesta-gateway-${APP_ENV} &> /dev/null || true
docker run \
    -e se.marell.dvestagateway.environment=APP_ENV \
    -d --name dvesta-gateway-${APP_ENV} \
    -p 10102:8080 \
    dvesta-gateway