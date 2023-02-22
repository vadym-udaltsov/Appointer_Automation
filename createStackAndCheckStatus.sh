#!/bin/bash

for ((i=1;i<=10;i++)); do
  echo "Getting stack info..."
  result="$( aws cloudformation describe-stacks --stack-name "appointment-stack" --region "eu-central-1" | grep StackStatus | awk {'print $2'} )"
  truncated=${result//,/}
  if [[ "${truncated}" == '"CREATE_COMPLETE"' ]]; then
    echo "Stack created, status:"
    echo "${truncated}"
    break
  fi
    echo "Stack creation in progress, status:"
    echo "${truncated}"
    sleep 10
done

#$SHELL