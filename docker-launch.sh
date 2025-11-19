#!/bin/bash

# Fermo il container, qualora l'avessi già lanciato:
docker stop subscriptions

# Rimuovo la rete apt-network, qualora l'avessi già creata
docker network rm apt-network

# Creo la rete apt-network: 
docker network create apt-network

# Lancio il container di MongoDB:
docker run -d --name subscriptions --network apt-network --publish 27017:27017 --rm mongo
