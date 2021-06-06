#!/usr/bin/env bash

for randomSeed in 1 2 3 4 5 6 7 8 9 10 1
do
        echo "Submitting kdd logistic regression (with pl&ohe) to cluster with random seed $randomSeed"
        sleep 3
	spark-submit --class q1.pipeline_SparkLoadLinearRegression --master yarn --deploy-mode cluster skdd_pllr.jar resources/kdd.data $randomSeed
        echo "Completed kdd logistic regression on cluster with random seed $randomSeed"
done
echo "done."

