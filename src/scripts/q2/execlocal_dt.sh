#!/usr/bin/env bash

spark-submit --class q2.SparkLoadDecisionTreeAIGNewsWithPCA --master local skdd_dt_pca.jar resources/kaggle-1000-train.csv resources/kaggle-1000-test.csv 123 800 NO_PCA 0 10 0

spark-submit --class q2.SparkLoadDecisionTreeAIGNewsWithPCA --master local skdd_dt_pca.jar resources/kaggle-1000-train.csv resources/kaggle-1000-test.csv 123 800 NO_PCA 0 10 1
