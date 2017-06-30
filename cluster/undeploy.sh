#!/bin/sh
#
# Removal of deploymentsto the pact namespace.

: ${NAMESPACE:=pact}

# lowercase required
NAMESPACE=${NAMESPACE,,}

echo -n "Terminating namespace $NAMESPACE.."
kubectl delete namespace $NAMESPACE
