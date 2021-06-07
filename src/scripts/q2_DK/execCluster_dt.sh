#!/usr/bin/env bash

#for randomSeed in 1 2 3 4 5
for randomSeed in 123
do
        echo "Submitting bdce1 logistic regression to cluster with random seed $randomSeed"
        sleep 3
	#spark-submit --class q2.BigDataClusterEater1 --master yarn --deploy-mode cluster bdce1_lr.jar q2input2/IMDB_Dataset_fixed2.csv 20 $randomSeed

	spark-submit --class q2.BigDataClusterEater3 --master yarn --deploy-mode cluster bdce1_lr3.jar q2input2/IMDB_Dataset_fixed2.csv 1000 0 0 1 8 $randomSeed
	spark-submit --class q2.BigDataClusterEater3 --master yarn --deploy-mode cluster bdce1_lr3.jar q2input2/IMDB_Dataset_fixed2.csv 1000 0 0 1 10 $randomSeed
	spark-submit --class q2.BigDataClusterEater3 --master yarn --deploy-mode cluster bdce1_lr3.jar q2input2/IMDB_Dataset_fixed2.csv 1000 1 0 1 10 $randomSeed
	spark-submit --class q2.BigDataClusterEater3 --master yarn --deploy-mode cluster bdce1_lr3.jar q2input2/IMDB_Dataset_fixed2.csv 1000 0 1 5 10 $randomSeed
	spark-submit --class q2.BigDataClusterEater3 --master yarn --deploy-mode cluster bdce1_lr3.jar q2input2/IMDB_Dataset_fixed2.csv 1000 0 1 10 10 $randomSeed
	spark-submit --class q2.BigDataClusterEater3 --master yarn --deploy-mode cluster bdce1_lr3.jar q2input2/IMDB_Dataset_fixed2.csv 1000 0 1 100 10 $randomSeed
	spark-submit --class q2.BigDataClusterEater3 --master yarn --deploy-mode cluster bdce1_lr3.jar q2input2/IMDB_Dataset_fixed2.csv 1000 0 1 200 10 $randomSeed

        echo "Completed kdd logistic regression on cluster with random seed $randomSeed"
done
echo "done."

