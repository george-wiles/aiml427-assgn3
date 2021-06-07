#!/usr/bin/env bash

spark-submit --class q2.SparkLoadDecisionTreeAIGNewsWithPCA --master local skdd_dt_pca.jar resources/kaggle-1000-train.csv resources/kaggle-1000-test.csv 123 800 NO_PCA 0 10
spark-submit --class q2.SparkLoadDecisionTreeAIGNewsWithPCA --master local skdd_dt_pca.jar resources/kaggle-1000-train.csv resources/kaggle-1000-test.csv 123 800 PCA 10 10
spark-submit --class q2.SparkLoadDecisionTreeAIGNewsWithPCA --master local skdd_dt_pca.jar resources/kaggle-1000-train.csv resources/kaggle-1000-test.csv 123 800 PCA 100 10

spark-submit --class q2.SparkLoadDecisionTreeAIGNewsWithPCA --master local skdd_dt_pca.jar resources/kaggle-1000-train.csv resources/kaggle-1000-test.csv 123 800 NO_PCA 0 20
spark-submit --class q2.SparkLoadDecisionTreeAIGNewsWithPCA --master local skdd_dt_pca.jar resources/kaggle-1000-train.csv resources/kaggle-1000-test.csv 123 800 PCA 10 20
spark-submit --class q2.SparkLoadDecisionTreeAIGNewsWithPCA --master local skdd_dt_pca.jar resources/kaggle-1000-train.csv resources/kaggle-1000-test.csv 123 800 PCA 100 20
