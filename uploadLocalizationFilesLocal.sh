#!/bin/bash
source deployment.config

localizationBucket="appointer-localization-773974733061"

cd bot/src/main/resources/localization

for folder in *
do
  echo "Uploading files from folder ${folder}"
  aws s3 sync $folder s3://${localizationBucket}/$folder/
done

$SHELL