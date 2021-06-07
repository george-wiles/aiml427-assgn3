#!/usr/bin/env bash

#"args: input file, hashing features, use scaling, use pca, pcaK, random seed, alpha, lambda"
spark-submit --class q2.BigDataClusterEater4 --master local bdce1_lr4.jar q2input2/smallset.csv 1000 0 0 1 123 0.0 0.0
spark-submit --class q2.BigDataClusterEater4 --master local bdce1_lr4.jar q2input2/smallset.csv 1000 0 0 1 123 1.0 0.5
spark-submit --class q2.BigDataClusterEater4 --master local bdce1_lr4.jar q2input2/smallset.csv 1000 0 0 1 123 0.5 1.0
spark-submit --class q2.BigDataClusterEater4 --master local bdce1_lr4.jar q2input2/smallset.csv 1000 0 0 1 123 0.5 0.0
spark-submit --class q2.BigDataClusterEater4 --master local bdce1_lr4.jar q2input2/smallset.csv 1000 0 0 1 123 0.3 0.8
spark-submit --class q2.BigDataClusterEater4 --master local bdce1_lr4.jar q2input2/smallset.csv 1000 0 0 1 123 0.8 0.3
