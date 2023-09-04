#!/bin/bash
source deployment.config

uiBucket="appointer-ui-773974733061"

cd admin/src/main/webapp

for folder in css html js mobile
do
  cd "$folder"

  for file in $(find . -type f)
  do
    if [ "$file" != './vars.js' ]
    then
       echo "$file"
       aws s3 cp "$file" "s3://${uiBucket}/${folder}/${file#./}"
    fi
  done
  cd ../
done

$SHELL