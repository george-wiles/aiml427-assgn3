#!/usr/bin/env bash

#input file, hashing features, use scaling, use pca, pcaK, random seed
spark-submit --class q2.BigDataClusterEater6 --master local bdce1_lr6.jar q2input2/smallset.csv 10 0 0 1 123
spark-submit --class q2.BigDataClusterEater6 --master local bdce1_lr6.jar q2input2/smallset.csv 30 0 0 1 123
spark-submit --class q2.BigDataClusterEater6 --master local bdce1_lr6.jar q2input2/smallset.csv 100 0 0 1 123
spark-submit --class q2.BigDataClusterEater6 --master local bdce1_lr6.jar q2input2/smallset.csv 300 0 0 1 123
spark-submit --class q2.BigDataClusterEater6 --master local bdce1_lr6.jar q2input2/smallset.csv 1000 0 0 1 123
spark-submit --class q2.BigDataClusterEater6 --master local bdce1_lr6.jar q2input2/smallset.csv 1000 1 0 1 123
spark-submit --class q2.BigDataClusterEater6 --master local bdce1_lr6.jar q2input2/smallset.csv 1000 0 1 5 123
spark-submit --class q2.BigDataClusterEater6 --master local bdce1_lr6.jar q2input2/smallset.csv 1000 0 1 10 123
spark-submit --class q2.BigDataClusterEater6 --master local bdce1_lr6.jar q2input2/smallset.csv 1000 0 1 50 123
spark-submit --class q2.BigDataClusterEater6 --master local bdce1_lr6.jar q2input2/smallset.csv 1000 0 1 100 123
spark-submit --class q2.BigDataClusterEater6 --master local bdce1_lr6.jar q2input2/smallset.csv 1000 1 1 100 123
