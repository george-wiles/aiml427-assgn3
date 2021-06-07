#!/usr/bin/env bash

#"args: input file, use scaling, use pca, pcaK, random seed vector size"
spark-submit --class q2.BigDataClusterEater5 --master local bdce1_lr5.jar q2input2/smallset.csv 0 0 1 123 20
spark-submit --class q2.BigDataClusterEater5 --master local bdce1_lr5.jar q2input2/smallset.csv 0 0 1 123 100
spark-submit --class q2.BigDataClusterEater5 --master local bdce1_lr5.jar q2input2/smallset.csv 0 0 1 123 200
spark-submit --class q2.BigDataClusterEater5 --master local bdce1_lr5.jar q2input2/smallset.csv 0 0 1 123 500
