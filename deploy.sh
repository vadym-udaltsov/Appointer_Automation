#!/bin/bash

source ${WORKSPACE}/deployment.config

accountId="$1"
verifiedEmail="$2"
deploymentBucket="appointer-deployment-${accountId}"
uiBucket="appointer-ui-${accountId}"
localizationBucket="appointer-localization-${accountId}"

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

echo 'Uploading python lambdas artefact to s3...'
aws s3 cp pythonLambdasPackage.zip s3://${deploymentBucket}

echo 'Uploading lambda layer artefact to s3...'
aws s3 cp ${layerArtefact} s3://${deploymentBucket}

echo "Deploying stack: ${infraStack}"
aws cloudformation deploy --stack-name ${infraStack} --template-file ${infraTemplate} --no-fail-on-empty-changeset \
--capabilities CAPABILITY_NAMED_IAM --region ${home_region} \
--parameter-overrides \
AccountId=${accountId} \
Region=${home_region} \
VerifiedIdentityEmail=${verifiedEmail} \
ArtifactSuff=${artefactSuff} \
UiBucketName=${uiBucket} \
DeploymentBucket=${deploymentBucket}

creationStatus=$(aws cloudformation describe-stacks --stack-name ${infraStack} --query 'Stacks[0].StackStatus' --output text | tr -d '\n')
apiId=$(aws cloudformation describe-stacks --stack-name ${infraStack} --query 'Stacks[0].Outputs[?OutputKey==`BotApiId`].OutputValue' --output text | tr -d '\n')
poolId=$(aws cloudformation describe-stacks --stack-name ${infraStack} --query 'Stacks[0].Outputs[?OutputKey==`UserPoolClientId`].OutputValue' --output text | tr -d '\n')

echo "creationStatus: ${creationStatus}"

if [[ "CREATE_COMPLETE" == "${creationStatus}" ]]; then
    echo "Stack created."
elif [[ "UPDATE_COMPLETE" == "${creationStatus}" ]]; then
    echo "Stack updated."
else
    echo "Stack failed with status: ${creationStatus}";
    exit 1;
fi

echo "Replacing values in vars.js file..."
echo "apiId: ${apiId}"
echo "poolId: ${poolId}"

sed -i'' -e 's/botApiId/'${apiId}'/g' admin/src/main/webapp/js/vars.js
sed -i'' -e 's/poolClientId/'${poolId}'/g' admin/src/main/webapp/js/vars.js
sed -i'' -e 's/uiBucketName/'${uiBucket}'/g' admin/src/main/webapp/js/vars.js
sed -i'' -e 's/userPoolDomainName/'appointer-${accountId}'/g' admin/src/main/webapp/js/vars.js
sed -i'' -e 's/envId/'${accountId}'/g' admin/src/main/webapp/js/vars.js

echo "Uploading UI files for admin functionality..."
cd admin/src/main/webapp

for folder in *
do
  echo "Uploading files from folder ${folder}"
  aws s3 sync $folder s3://${uiBucket}/$folder/ --acl public-read
done

echo "Going out of admin..."
cd ../../../../

echo "Uploading localization files..."
cd bot/src/main/resources/localization

for folder in *
do
  echo "Uploading files from folder ${folder}"
  aws s3 sync $folder s3://${localizationBucket}/$folder/
done