#!/usr/bin/env bash

spark-submit --class q1.pipeline_SparkLoadLinearRegression --master local skdd_pllr.jar resources/kdd.data 10