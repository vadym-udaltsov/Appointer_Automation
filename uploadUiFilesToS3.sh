#!/bin/bash
source ${WORKSPACE}/deployment.config

cd admin/src/main/webapp

for folder in *
do
  echo "Uploading files from folder ${folder}"
  aws s3 sync $folder s3://${uiBucket}/$folder/ --acl public-read
done

