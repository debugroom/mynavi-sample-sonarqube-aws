#!/usr/bin/env bash

bucket_name=debugroom-mynavi-sonarqube-cfn-lambda-bucket
stack_name="mynavi-sonarqube-s3-lambda"
template_path="rds/1-s3-lambda-deploy-cfn.yml"
s3_objectkey="mynavi-sample-sonarqube-initdb-0.0.1-SNAPSHOT-aws.jar"

if [ "" == "`aws s3 ls | grep $bucket_name`" ]; then
    aws cloudformation deploy --stack-name ${stack_name} --template-file ${template_path} --capabilities CAPABILITY_IAM
fi

cd rds/lambda-sonarqube-dbinit
./mvnw package
aws s3 cp target/${s3_objectkey} s3://${bucket_name}/