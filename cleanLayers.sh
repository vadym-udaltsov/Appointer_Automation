#!/bin/bash

for i in {439..535}
do
  echo "Deleting layer version ${i}"
  aws lambda delete-layer-version --layer-name adminLayer --version-number ${i}
done

$SHELL