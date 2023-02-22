#!/usr/bin/env groovy
pipeline {
    agent any

    parameters {
        choice(name: 'Action', choices: ['deploy', 'destroy'])
    }

    environment {
        STACK_NAME = "appointment-stack"
        ARTEFACT_PATH = "target/lambdaSpring-0.0.1-SNAPSHOT-jar-with-dependencies.jar"
        DEPLOYMENT_BUCKET = "appointment-deployment-bucket"
        TEMPLATE = "ApiGatewayLambda.yml"
        REGION = "eu-central-1"
        PARAM_FILE = "config.json"
    }

    stages {
        stage('Build') {
            when {
                expression { params.Action != 'destroy' }
            }
            steps {
                sh 'mvn clean package'
            }
        }
        stage('Deploy') {
            steps {
                script {
                    if (params.Action == 'destroy') {
                        echo 'Destroying the stack...'
                        sh 'aws cloudformation delete-stack --stack-name ' + "$env.STACK_NAME" + ' --region ' + "$env.REGION"
                    } else {
                        sh "chmod +x ./config.json"
                        sh 'ls -la'
                        echo 'Uploading artefact to s3...'
                        sh 'aws s3 cp ' + "$env.ARTEFACT_PATH" + ' s3://' + "$env.DEPLOYMENT_BUCKET"
                        if (params.Action == 'deploy') {
                            echo 'Deploying stack: ' + "$env.STACK_NAME"
                            sh 'aws cloudformation deploy --stack-name ' + "$env.STACK_NAME" + ' --template-file ' + "$env.TEMPLATE" + ' --no-fail-on-empty-changeset --capabilities CAPABILITY_NAMED_IAM --region '+"$env.REGION" + ' --parameter-overrides file://' + "$env.PARAM_FILE"
                        }
                    }
                }
            }
        }
    }
}