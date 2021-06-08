#!/usr/bin/env bash

spark-submit --class q2.SparkLoadLinearRegressionAIGNewsWithPCA --master local skdd_lr_pca.jar resources/kaggle-1000-train.csv resources/kaggle-1000-test.csv 123 800 NO_PCA 0 1
