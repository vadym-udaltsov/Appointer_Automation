#!/bin/bash

aws cloudformation validate-template --template-body file://ApiGatewayLambda.yml

$SHELL