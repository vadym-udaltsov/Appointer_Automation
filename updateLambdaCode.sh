#!/bin/bash

source ${WORKSPACE}/deployment.config

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

  if [ "$1" = "all" ] || [ "$1" = "${lambda_name}" ]
  then
    echo "Updating $lambda_name function code..."
    aws lambda update-function-code --function-name $lambda_name --zip-file fileb://./"$lambda_artefact" --region $home_region
    aws lambda wait function-updated-v2 --function-name $lambda_name

    echo "Publishing new version..."
    lambda_version=$( aws lambda publish-version --function-name "${lambda_name}" --query Version --output text)
    aws lambda wait published-version-active --function-name $lambda_name

    echo "Updating lambda alias..."
    echo "lambda update-alias --function-name $lambda_name --name adminLambdaAlias"
    aws lambda update-alias --function-name $lambda_name --name $alias_name --function-version "${lambda_version}"
  fi
done