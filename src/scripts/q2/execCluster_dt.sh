#!/usr/bin/env bash

for randomSeed in 1 2 3 # 4 5 6 7 8 9 10 1
do
	echo "Submitting aig-news decision tree to cluster with random seed $randomSeed"
	sleep 3
	spark-submit --class q2.SparkLoadDecisionTreeAIGNewsWithPCA --master yarn --deploy-mode cluster skdd_dt_pca.jar resources/kaggle-news-train.csv resources/kaggle-news-test.csv 123 1000 NO_PCA 0 12
	echo "Completed aig-news decision tree on cluster with random seed $randomSeed"
done

echo "pca done."

