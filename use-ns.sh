#!/bin/sh

if [ $# -eq 0 ] 
then
   echo 'Provide a namespace to switch to. (e.g. use-ns default)'
else
   kubectl config set-context minikube --namespace=$1 && kubectl config use-context minikube
   echo "namespace is now $1"
fi
