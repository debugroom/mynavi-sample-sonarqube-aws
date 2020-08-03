#!/usr/bin/env bash

stack_name="mynavi-sonarqube-ecs-service"
#stack_name="mynavi-sonarqube-tg"
#stack_name="mynavi-sonarqube-ecs-task"
#stack_name="mynavi-sonarqube-lambda-trigger"
#stack_name="mynavi-sonarqube-lambda"
#stack_name="mynavi-sonarqube-rds"
#stack_name="mynavi-sonarqube-ecs-cluster"
#stack_name="mynavi-sonarqube-alb"
#stack_name="mynavi-sonarqube-natgw"
#stack_name="mynavi-sonarqube-sg"
#stack_name="mynavi-sonarqube-s3-lambda"
#stack_name="mynavi-sonarqube-vpc"
template_path="sonarqube-server/5-ecs-service-cfn.yml"
#template_path="sonarqube-server/4-tg-cfn.yml"
#template_path="sonarqube-server/4-ecs-task-cfn.yml"
#template_path="rds/4-lambda-trigger-cfn.yml"
#template_path="rds/3-lambda-cfn.yml"
#template_path="rds/3-rds-postgres-cfn.yml"
#template_path="base/3-ecs-cluster-cfn.yml"
#template_path="base/3-alb-cfn.yml"
#template_path="rds/2-natgw-cfn.yml"
#template_path="base/2-sg-cfn.yml"
#template_path="rds/1-s3-lambda-deploy-cfn.yml"
#template_path="base/1-vpc-cfn.yml"
parameters="EnvType=Dev"

#aws cloudformation create-stack --stack-name ${stack_name} --template-body file://${template_path} --capabilities CAPABILITY_IAM
# It is better cloudformation deploy option because command can execute even if stack existing(no need to delete existing stack).

if [ "$parameters" == "" ]; then
    aws cloudformation deploy --stack-name ${stack_name} --template-file ${template_path} --capabilities CAPABILITY_IAM
else
    aws cloudformation deploy --stack-name ${stack_name} --template-file ${template_path} --parameter-overrides ${parameters} --capabilities CAPABILITY_IAM
fi