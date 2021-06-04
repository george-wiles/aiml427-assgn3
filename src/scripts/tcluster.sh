#!/usr/bin/env bash

spark-submit --class q1.SparkLoadLinearRegression --master yarn --deploy-mode cluster skddlr2.jar resources/kdd.data
