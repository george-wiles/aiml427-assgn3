#!/usr/bin/env bash

#for randomSeed in 1 2 3 4 5
for randomSeed in 123
do
        echo "Submitting bdce1 logistic regression to cluster with random seed $randomSeed"
        sleep 3
	#spark-submit --class q2.BigDataClusterEater1 --master yarn --deploy-mode cluster bdce1_lr.jar q2input2/IMDB_Dataset_fixed2.csv 20 $randomSeed

	spark-submit --class q2.BigDataClusterEater2 --master yarn --deploy-mode cluster bdce1_lr2.jar q2input2/IMDB_Dataset_fixed2.csv 1000 0 0 1 $randomSeed
	spark-submit --class q2.BigDataClusterEater2 --master yarn --deploy-mode cluster bdce1_lr2.jar q2input2/IMDB_Dataset_fixed2.csv 1000 0 1 5 $randomSeed
	spark-submit --class q2.BigDataClusterEater2 --master yarn --deploy-mode cluster bdce1_lr2.jar q2input2/IMDB_Dataset_fixed2.csv 1000 0 1 10 $randomSeed
	spark-submit --class q2.BigDataClusterEater2 --master yarn --deploy-mode cluster bdce1_lr2.jar q2input2/IMDB_Dataset_fixed2.csv 1000 0 1 50 $randomSeed
	spark-submit --class q2.BigDataClusterEater2 --master yarn --deploy-mode cluster bdce1_lr2.jar q2input2/IMDB_Dataset_fixed2.csv 1000 0 1 100 $randomSeed
	spark-submit --class q2.BigDataClusterEater2 --master yarn --deploy-mode cluster bdce1_lr2.jar q2input2/IMDB_Dataset_fixed2.csv 1000 1 1 100 $randomSeed

        echo "Completed kdd logistic regression on cluster with random seed $randomSeed"
done
echo "done."

