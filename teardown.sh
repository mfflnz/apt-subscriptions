#!/bin/bash

docker stop my-mongo
docker network rm apt-network
docker container prune -f
docker volume prune -f
