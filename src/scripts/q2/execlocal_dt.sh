#!/usr/bin/env bash

spark-submit --class q2.SparkLoadDecisionTree --master local skdd_dt.jar resources/kaggle-news-train.csv resources/kaggle-news-test.csv 123 800
