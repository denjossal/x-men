#!/bin/sh
./gradlew clean build # <1>
docker build . -t mutant # <2>
sam local start-api -t sam.yaml -p 3000 # <3>
