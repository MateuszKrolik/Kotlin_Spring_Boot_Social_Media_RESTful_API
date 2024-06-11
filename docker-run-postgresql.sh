#! /bin/bash

source .env

docker run -d \
  --name some-postgres \
  -e POSTGRES_PASSWORD=$POSTGRES_PASSWORD \
  -e PGDATA=/var/lib/postgresql/data/pgdata \
	-v /Users/mateuszkrolik/Developer/restful-web-services/postgresMount:/var/lib/postgresql/data \
  -p 5432:5432 \
  postgres:16-alpine

# psql -U postgres -h localhost -p 5432
