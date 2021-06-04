#!/usr/bin/env bash

spark-submit --class q1.SparkLoadDecisionTree q --master local skdd_dt.jar resources/kdd.data
