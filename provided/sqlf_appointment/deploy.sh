#!/bin/bash

echo "Shutting down..."
./shutdown.sh

echo "Cleaning..."
./clean.sh

echo "Building..."
mvn clean package

echo "Uploading..."
./upload.sh

echo "Starting..."
./start.sh

echo "Done."
