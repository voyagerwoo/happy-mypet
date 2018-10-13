#!/usr/bin/env bash

export IMAGE_NAME="happy-mypet"
export CLUSTER_NAME="happy-mypet"
export REGION=`aws configure get region`

export ECS_SG_NAME=happy-mypet-ecs-sg
export ALB_SG_NAME=happy-mypet-alb-sg
export ALB_NAME=happy-mypet-alb
export TARGET_NAME=happy-mypet-targets

ACCOUNT_ID=`aws sts get-caller-identity | jq -r ".Account"`
export DOCKER_REGISTRY_HOST="${ACCOUNT_ID}.dkr.ecr.${REGION}.amazonaws.com"

export VPC_ID=`aws ec2 describe-vpcs | jq -r '.Vpcs[0].VpcId'`
export SUBNET_ID_1=`aws ec2 describe-subnets | jq -r '.Subnets[0].SubnetId'`
export SUBNET_ID_2=`aws ec2 describe-subnets | jq -r '.Subnets[1].SubnetId'`

export AWS_ACCESS_KEY_ID=`aws configure get aws_access_key_id`
export AWS_SECRET_ACCESS_KEY=`aws configure get aws_secret_access_key`