#!/bin/bash

aws cloudformation validate-template --template-body file://deploymentResources-infra.yml

$SHELL