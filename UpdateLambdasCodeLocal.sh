#!/bin/bash

source deployment.config

lambdasLocal="appointerBotLambda||bot||true"
#lambdas="adminLambda||admin||true appointerBotLambda||bot||true"
#lambdasLocal="postSignUpLambda||registration||false"
update_layer="n"

if [ "y" = ${update_layer} ];
then
    echo "Copying 3rd party dependency layer jar to s3..."
    aws s3 cp ${layerArtefact} s3://${deploymentBucket}

    echo "Publishing new version of layer..."
    layer_version=$(aws lambda publish-layer-version \
        --layer-name "$adminLayerName" \
        --content S3Bucket="$deploymentBucket",S3Key=bot-3d-layer.jar \
        --compatible-runtimes "$runtime" --query LayerVersionArn --output text --region $home_region)
fi

  # shellcheck disable=SC2154
for lambda_pair in $lambdasLocal
do
  :
  IFS='||'
  read -ra words <<<"${lambda_pair}"
  declare -a names_array=()
  for i in "${words[@]}";
  do
    names_array+=($i)
  done

  lambda_name=${names_array[0]}
  lambda_artefact=${names_array[1]}/target/${names_array[1]}$artefactSuff
  alias_name=${names_array[0]}Alias
  snap_start_enabled=${names_array[2]}

  echo "Updating $lambda_name function code..."
  aws lambda update-function-code --function-name $lambda_name --zip-file fileb://./"$lambda_artefact" --region $home_region
  aws lambda wait function-updated --function-name $lambda_name

  if [ "true" = ${snap_start_enabled} ]
  then
    echo "Publishing new version..."
    lambda_version=$( aws lambda publish-version --function-name "${lambda_name}" --query Version --output text)
    echo "${lambda_version}"
    aws lambda wait function-updated --function-name $lambda_name

    echo "Updating lambda alias..."
    aws lambda update-alias --function-name $lambda_name --name $alias_name --function-version "${lambda_version}"
  fi
  if [ "y" = ${update_layer} ];
  then
     echo "Attaching new version of layer to $lambda_name function..."
       aws lambda update-function-configuration --function-name $lambda_name --layers $layer_version --region $home_region
  fi
done

$SHELL