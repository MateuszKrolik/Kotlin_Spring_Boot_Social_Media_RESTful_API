#! /bin/bash

docker run --detach \
  --env-file ./.env \
  --name mysql \
  --publish 3306:3306 \
  mysql:8-oracle