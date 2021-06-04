#!/usr/bin/env bash

spark-submit --class q1.SparkLoadLinearRegression --master yarn --deploy-mode cluster skdd_lr.jar resources/kdd.data
