#!/usr/bin/env bash

#stack_name="mynavi-sonarqube-ecs-service"
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
stack_name="mynavi-sonarqube-vpc"

aws cloudformation delete-stack --stack-name ${stack_name}