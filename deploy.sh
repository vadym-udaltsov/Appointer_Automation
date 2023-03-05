#!/bin/bash

source ${WORKSPACE}/deployment.config

echo 'Uploading artefact to s3...'
aws s3 cp ${adminLambdaArtefact} s3://${deploymentBucket}
echo "Deploying stack: ${infraStack}"
aws cloudformation deploy --stack-name ${infraStack} --template-file ${infraTemplate} --no-fail-on-empty-changeset --capabilities CAPABILITY_NAMED_IAM --region ${home_region} --parameter-overrides file://${infraParams}

apiId=$(aws cloudformation describe-stacks --stack-name ${infraStack} --query 'Stacks[0].Outputs[?OutputKey==`BotApiId`].OutputValue' --output text | tr -d '\n')
poolId=$(aws cloudformation describe-stacks --stack-name ${infraStack} --query 'Stacks[0].Outputs[?OutputKey==`UserPoolClientId`].OutputValue' --output text | tr -d '\n')

echo "Replacing vars in js file..."
echo "apiid: ${apiId}"
echo "poolId: ${poolId}"

sed -i 's/botApiId/'${apiId}'/' admin/src/main/webapp/js/vars.js
sed -i 's/poolClientId/'${poolId}'/' admin/src/main/webapp/js/vars.js

echo "Uploading UI files for admin functionality..."
cd admin/src/main/webapp

for folder in *
do
  echo "Uploading files from folder ${folder}"
  aws s3 sync $folder s3://appointer-ui/$folder/ --acl public-read
done