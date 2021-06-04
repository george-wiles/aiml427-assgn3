#!/usr/bin/env bash

spark-submit --class q1.SparkLoadLinearRegression q --master local skdd_lr.jar resources/kdd.data
