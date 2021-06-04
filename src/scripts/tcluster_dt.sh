#!/usr/bin/env bash

spark-submit --class q1.SparkLoadDecisionTree --master yarn --deploy-mode cluster skdd_dt.jar resources/kdd.data
