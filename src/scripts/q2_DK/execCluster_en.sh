#!/usr/bin/env bash

#for randomSeed in 1 2 3 4 5
for randomSeed in 123
do
        echo "Submitting bdce1 logistic regression to cluster with random seed $randomSeed"
        sleep 3
	#spark-submit --class q2.BigDataClusterEater1 --master yarn --deploy-mode cluster bdce1_lr.jar q2input2/IMDB_Dataset_fixed2.csv 20 $randomSeed

	spark-submit --class q2.BigDataClusterEater4 --master yarn --deploy-mode cluster bdce1_lr4.jar q2input2/IMDB_Dataset_fixed2.csv 1000 0 0 1 123 0.0 0.0
	spark-submit --class q2.BigDataClusterEater4 --master yarn --deploy-mode cluster bdce1_lr4.jar q2input2/IMDB_Dataset_fixed2.csv 1000 0 0 1 123 1.0 0.5
	spark-submit --class q2.BigDataClusterEater4 --master yarn --deploy-mode cluster bdce1_lr4.jar q2input2/IMDB_Dataset_fixed2.csv 1000 0 0 1 123 0.5 1.0
	spark-submit --class q2.BigDataClusterEater4 --master yarn --deploy-mode cluster bdce1_lr4.jar q2input2/IMDB_Dataset_fixed2.csv 1000 0 0 1 123 0.5 0.0
	spark-submit --class q2.BigDataClusterEater4 --master yarn --deploy-mode cluster bdce1_lr4.jar q2input2/IMDB_Dataset_fixed2.csv 1000 0 0 1 123 0.3 0.8
	spark-submit --class q2.BigDataClusterEater4 --master yarn --deploy-mode cluster bdce1_lr4.jar q2input2/IMDB_Dataset_fixed2.csv 1000 0 0 1 123 0.8 0.3


        echo "Completed kdd logistic regression on cluster with random seed $randomSeed"
done
echo "done."

