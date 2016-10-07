#!/bin/bash

ssh server2.dev 'rm -Rf ./sqlfsrv/*'
ssh server3.dev 'rm -Rf ./sqlfsrv/*'
ssh server4.dev 'rm -Rf ./sqlfsrv/*'
ssh server5.dev 'rm -Rf ./sqlfsrv/*'
