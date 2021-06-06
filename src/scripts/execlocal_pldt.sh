#!/usr/bin/env bash

spark-submit --class q1.pipeline_SparkLoadDecisionTree --master local skdd_pldt.jar resources/kdd.data 10
