#!/bin/bash

for i in {536..596}
do
  echo "Deleting layer version ${i}"
  aws lambda delete-layer-version --layer-name adminLayer --version-number ${i}
done

$SHELL