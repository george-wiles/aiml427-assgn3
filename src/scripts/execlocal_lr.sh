#!/usr/bin/env bash

spark-submit --class q1.SparkLoadLinearRegression --master local skdd_lr.jar resources/kdd.data
