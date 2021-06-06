#!/usr/bin/env bash

spark-submit --class q1.pipeline_SparkLoadDecisionTree --master local skdd_dt.jar resources/kdd.data 123
