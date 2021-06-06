#!/usr/bin/env bash

spark-submit --class q1.pipeline_SparkLoadDecisionTree2 --master local skdd_dt2.jar resources/kdd.data 123
