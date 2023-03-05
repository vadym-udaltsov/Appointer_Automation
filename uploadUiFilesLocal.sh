#!/bin/bash

cd admin/src/main/webapp

for folder in *
do
  cd $folder

  for file in *
  do
    if [ $file != 'vars.js' ]
    then
       echo $file
       aws s3 cp "$file" s3://appointer-ui/$folder/
    fi
  done
  cd ../
done

$SHELL