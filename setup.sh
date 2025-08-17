#!/bin/bash

# Fermo il container, qualora l'avessi già lanciato:
docker stop my-mongo

# Rimuovo la rete apt-network, qualora l'avessi già creata
docker network rm apt-network

# Creo la rete apt-network: 
docker network create apt-network

# Lancio il container di MongoDB:
docker run -d --name my-mongo --network apt-network -p 27017:27017 --rm mongo

# Importo i dati nella collection 'subscriptions':
docker run -it --network apt-network -v "$PWD"/assets:/assets --rm mongo:latest mongoimport --host my-mongo --collection='orders' --headerline --file=assets/orders.csv --type=csv

# Lancio la shell di MongoDB:
docker run -it --network apt-network --rm mongo:latest mongosh --host my-mongo
