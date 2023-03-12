#!/bin/bash

source ${WORKSPACE}/deployment.config

#Uploading needed lambda artefacts
for lambda_pair in $lambdas
do
  :
  IFS='||'
  read -ra words <<<"${lambda_pair}"
  declare -a names_array=()
  for i in "${words[@]}";
  do
    names_array+=($i)
  done
  lambda_artefact=${names_array[1]}/target/${names_array[1]}$artefactSuff

  echo "Uploading artefact ${lambda_artefact} to s3..."
  aws s3 cp ${lambda_artefact} s3://${deploymentBucket}
done

echo 'Uploading lambda layer artefact to s3...'
aws s3 cp ${layerArtefact} s3://${deploymentBucket}

echo "Deploying stack: ${infraStack}"
aws cloudformation deploy --stack-name ${infraStack} --template-file ${infraTemplate} --no-fail-on-empty-changeset \
--capabilities CAPABILITY_NAMED_IAM --region ${home_region} \
--parameter-overrides \
AccountId=${accountId} \
Region=${home_region} \
VerifiedIdentityEmail=${verifiedEmail} \
ArtifactSuff=${artefactSuff}


apiId=$(aws cloudformation describe-stacks --stack-name ${infraStack} --query 'Stacks[0].Outputs[?OutputKey==`BotApiId`].OutputValue' --output text | tr -d '\n')
poolId=$(aws cloudformation describe-stacks --stack-name ${infraStack} --query 'Stacks[0].Outputs[?OutputKey==`UserPoolClientId`].OutputValue' --output text | tr -d '\n')

echo "Replacing values in vars.js file..."
echo "apiId: ${apiId}"
echo "poolId: ${poolId}"

sed -i 's/botApiId/'${apiId}'/' admin/src/main/webapp/js/vars.js
sed -i 's/poolClientId/'${poolId}'/' admin/src/main/webapp/js/vars.js
sed -i 's/uiBucketName/'${uiBucket}'/' admin/src/main/webapp/js/vars.js
sed -i 's/userPoolDomainName/'appointer-${accountId}'/' admin/src/main/webapp/js/vars.js

echo "Uploading UI files for admin functionality..."
cd admin/src/main/webapp

for folder in *
do
  echo "Uploading files from folder ${folder}"
  aws s3 sync $folder s3://${uiBucket}/$folder/ --acl public-read
done