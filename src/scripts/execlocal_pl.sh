#!/usr/bin/env bash

spark-submit --class q1.pipeline_SparkLoadLinearRegression --master local skdd_pl.jar resources/kdd.data 10
