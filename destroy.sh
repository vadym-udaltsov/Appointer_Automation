#!/bin/bash

source ${WORKSPACE}/deployment.config

accountId="$1"
uiBucket="appointer-ui-${accountId}"
localizationBucket="appointer-localization-${accountId}"

echo "Destroying the stack '${infraStack}'"

echo "Cleaning ui bucket"
aws s3 rm s3://${uiBucket} --recursive

echo "Cleaning localization bucket"
aws s3 rm s3://${localizationBucket} --recursive

aws cloudformation delete-stack --stack-name ${infraStack} --region ${home_region}
aws cloudformation wait stack-delete-complete --stack-name ${infraStack} --region ${home_region}

echo "Successfully destroyed stack '${infraStack}'"