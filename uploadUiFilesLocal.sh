#!/bin/bash
source deployment.config

uiBucket="appointer-ui-773974733061"

cd admin/src/main/webapp

for folder in css html js mobile
do
  cd "$folder"

  aws s3 sync . "s3://${uiBucket}/${folder}/" --exclude 'vars.js'

  cd ../
done

$SHELL