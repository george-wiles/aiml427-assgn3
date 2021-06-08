#!/usr/bin/env bash

sleep 3
spark-submit --class q2.SparkLoadLinearRegressionAIGNewsWithPCA --master yarn --deploy-mode cluster skdd_lr_pca.jar resources/kaggle-news-train.csv resources/kaggle-news-test.csv $randomSeed 1000 PCA 12 0



