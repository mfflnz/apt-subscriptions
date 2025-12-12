#!/bin/bash
set -e

# Creo la rete apt-network: 
docker network create apt-network

# Lancio il container di MongoDB:
docker run -d --name apt-mongo --network apt-network --publish 27017:27017 --rm mongo

# Importo i dati nella collection 'subscriptions':
docker run --network apt-network -v "$PWD"/src/main/resources:/resources --rm mongo:latest mongoimport --host apt-mongo --db='subscriptions' --collection='orders' --headerline --file=resources/sample-orders.csv --type=csv

# Lancio la shell di MongoDB:
# docker run -it --network apt-network --rm mongo:latest mongosh --host apt-mongo
