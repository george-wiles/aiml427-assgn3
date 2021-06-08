#!/usr/bin/env bash

for randomSeed in 1 2 3 4 5
#for randomSeed in 123
do
        echo "Submitting bdce1 logistic regression to cluster with random seed $randomSeed"
        sleep 3

	spark-submit --class q2.BigDataClusterEater5 --master yarn --deploy-mode cluster bdce1_lr5.jar q2input2/IMDB_Dataset_fixed2.csv 0 0 1 $randomSeed 100
	spark-submit --class q2.BigDataClusterEater5 --master yarn --deploy-mode cluster bdce1_lr5.jar q2input2/IMDB_Dataset_fixed2.csv 1 1 20 $randomSeed 100
	spark-submit --class q2.BigDataClusterEater5 --master yarn --deploy-mode cluster bdce1_lr5.jar q2input2/IMDB_Dataset_fixed2.csv 0 1 20 $randomSeed 100
	spark-submit --class q2.BigDataClusterEater5 --master yarn --deploy-mode cluster bdce1_lr5.jar q2input2/IMDB_Dataset_fixed2.csv 1 0 1 $randomSeed 100

        echo "Completed kdd logistic regression on cluster with random seed $randomSeed"
done
echo "done."

