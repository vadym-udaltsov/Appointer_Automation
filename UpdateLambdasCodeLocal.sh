#!/bin/bash

source deployment.config

  # shellcheck disable=SC2154
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
  lambda_name=${names_array[0]}
  lambda_artefact=${names_array[1]}
  alias_name=${names_array[2]}

  echo "Copying to bucket"
  aws s3 cp ./"$lambda_artefact" s3://appointment-deployment-bucket/"$lambda_name".jar

  echo "Updating $lambda_name function code..."
  aws lambda update-function-code --function-name $lambda_name --s3-bucket appointment-deployment-bucket --s3-key "$lambda_name".jar --region $home_region

#  aws lambda update-function-code --function-name $lambda_name --zip-file fileb://./"$lambda_artefact" --region $home_region
  aws lambda wait function-updated --function-name $lambda_name

  echo "Publishing new version..."
  lambda_version=$( aws lambda publish-version --function-name "${lambda_name}" --query Version --output text)
  echo "${lambda_version}"
  aws lambda wait function-updated --function-name $lambda_name

  echo "Updating lambda alias..."
  echo "lambda update-alias --function-name $lambda_name --name adminLambdaAlias"
  aws lambda update-alias --function-name $lambda_name --name $alias_name --function-version "${lambda_version}"
done

$SHELL