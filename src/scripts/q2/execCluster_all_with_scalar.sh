#!/usr/bin/env bash

for randomSeed in 1 2 # 3 4 5 6 7 8 9 10 1
do
  echo "Submitting aig-news logistic regression and Decision Tree to cluster with random seed $randomSeed"
  sleep 3
  spark-submit --class q2.SparkLoadLinearRegressionAIGNewsWithPCA --master yarn --deploy-mode cluster skdd_lr_pca.jar resources/kaggle-news-train.csv resources/kaggle-news-test.csv $randomSeed 1000 PCA 12 1

	sleep 3
	spark-submit --class q2.SparkLoadDecisionTreeAIGNewsWithPCA --master yarn --deploy-mode cluster skdd_dt_pca.jar resources/kaggle-news-train.csv resources/kaggle-news-test.csv 123 1000 PCA 12 12 1

	sleep 3
	spark-submit --class q2.SparkLoadDecisionTreeAIGNewsWithPCA --master yarn --deploy-mode cluster skdd_dt_pca.jar resources/kaggle-news-train.csv resources/kaggle-news-test.csv 123 1000 NO_PCA 0 12 1

  sleep 3
  spark-submit --class q2.SparkLoadLinearRegressionAIGNewsWithPCA --master yarn --deploy-mode cluster skdd_lr_pca.jar resources/kaggle-news-train.csv resources/kaggle-news-test.csv $randomSeed 1000 NO_PCA 0 1

  echo "Completed aig-news logistic regression on cluster with random seed $randomSeed"
done
echo "done."


