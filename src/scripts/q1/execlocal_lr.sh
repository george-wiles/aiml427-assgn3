#!/usr/bin/env bash

spark-submit --class q2.SparkLoadLinearRegression --master local skdd_lr.jar resources/kaggle-news-train.csv resources/kaggle-news-test.csv 123
