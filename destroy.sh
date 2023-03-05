#!/bin/bash

source ${WORKSPACE}/deployment.config

echo "Destroying the stack '${infraStack}'"

echo "Cleaning ui bucket"
aws s3 rm s3://${uiBucket} --recursive

aws cloudformation delete-stack --stack-name ${infraStack} --region ${home_region}
aws cloudformation wait stack-delete-complete --stack-name ${infraStack} --region ${home_region}

echo "Successfully destroyed stack '${infraStack}'"