#!/usr/bin/env bash

spark-submit --class q1.SparkLoadDecisionTree --master local skdd_dt.jar resources/kdd.data 123
