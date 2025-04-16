#!/bin/bash
# Construction de l'image docker
docker build -t ghcr.io/lisazannettacci/jpandas:latest .

# Pusher l'image
docker push ghcr.io/lisazannettacci/jpandas:latest
