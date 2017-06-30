#!/bin/bash

broker=$(minikube service pact-broker-service --url)

set -x
curl -X DELETE $broker/pacticipants/denverConsumer
curl -X DELETE $broker/pacticipants/largestCitiesConsumer