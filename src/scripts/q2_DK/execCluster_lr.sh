#!/usr/bin/env bash

for randomSeed in 1 2 3 4 5
#for randomSeed in 1
do
        echo "Submitting bdce1 logistic regression to cluster with random seed $randomSeed"
        sleep 3
	spark-submit --class q2.BigDataClusterEater1 --master yarn --deploy-mode cluster bdce1_lr.jar q2input/IMDB_Dataset.csv 20 $randomSeed
        echo "Completed kdd logistic regression on cluster with random seed $randomSeed"
done
echo "done."

