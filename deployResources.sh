#!/bin/bash
#source deployment.config

#echo "Creating deployment bucket..."
#
#aws cloudformation create-stack --stack-name "appointment-stack" --template-body file://ApiGatewayLambda.yml \
#--capabilities CAPABILITY_NAMED_IAM

aws cloudformation deploy --stack-name appointment-stack --template-file ApiGatewayLambda.yml --no-fail-on-empty-changeset --capabilities CAPABILITY_NAMED_IAM --region eu-central-1 --parameter-overrides file://config.json

$SHELL