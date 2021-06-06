#!/usr/bin/env bash

spark-submit --class q2.SparkLoadLinearRegressionWithPCA --master local skdd_lr_pca.jar resources/kaggle-news-train.csv resources/kaggle-news-test.csv 123 8000
