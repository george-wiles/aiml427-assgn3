#!/usr/bin/env bash

for randomSeed in 1 2 # 3 4 5 6 7 8 9 10 1
do
  echo "Submitting aig-news logistic regression to cluster with random seed $randomSeed"
  sleep 3
  spark-submit --class q2.SparkLoadLinearRegression --master yarn --deploy-mode cluster skdd_lr.jar resources/kaggle-news-train.csv resources/kaggle-news-test.csv $randomSeed 500
  echo "Completed aig-news logistic regression on cluster with random seed $randomSeed"
done
echo "done."

