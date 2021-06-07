#!/usr/bin/env bash

for randomSeed in 1 2 3 4 5 6 7 8 9 10
#for randomSeed in 1
do
	echo "Submitting kdd decision tree to cluster with random seed $randomSeed"
	sleep 3
	spark-submit --class q1.pipeline_SparkLoadDecisionTree2 --master yarn --deploy-mode cluster skdd_dt2.jar resources/kdd.data $randomSeed
	echo "Completed kdd decision tree on cluster with random seed $randomSeed"
done
echo "done."
