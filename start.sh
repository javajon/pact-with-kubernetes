#!/bin/bash
set -ex

minikube start --insecure-registry '192.168.99.0/24'

# See https://github.com/kubernetes/minikube/tree/master/deploy/addons
minikube addons enable registry
minikube addons enable heapster

minikube version
minikube status

kubectl create namespace pact

{ set -; } 2>/dev/null
echo 'Minikube is now ready'
