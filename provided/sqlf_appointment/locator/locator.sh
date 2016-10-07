#!/bin/bash

ARGS="-dir=data -client-bind-address=127.0.0.1 -peer-discovery-address=127.0.0.1"
ARGS="$ARGS -jmx-manager=true -jmx-manager-start=true"

sqlf locator start $ARGS
