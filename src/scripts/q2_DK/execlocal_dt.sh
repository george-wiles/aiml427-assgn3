#!/usr/bin/env bash

#input file, hashing features, use scaling, use pca, pcaK, dtMaxDepth, random seed
spark-submit --class q2.BigDataClusterEater3 --master local bdce1_lr3.jar q2input2/smallset.csv 1000 0 0 1 5 123
spark-submit --class q2.BigDataClusterEater3 --master local bdce1_lr3.jar q2input2/smallset.csv 1000 0 0 1 10 123
spark-submit --class q2.BigDataClusterEater3 --master local bdce1_lr3.jar q2input2/smallset.csv 1000 0 0 1 15 123
spark-submit --class q2.BigDataClusterEater3 --master local bdce1_lr3.jar q2input2/smallset.csv 1000 0 0 1 20 123
spark-submit --class q2.BigDataClusterEater3 --master local bdce1_lr3.jar q2input2/smallset.csv 1000 1 0 1 15 123
spark-submit --class q2.BigDataClusterEater3 --master local bdce1_lr3.jar q2input2/smallset.csv 1000 0 1 5 15 123
spark-submit --class q2.BigDataClusterEater3 --master local bdce1_lr3.jar q2input2/smallset.csv 1000 0 1 10 15 123
spark-submit --class q2.BigDataClusterEater3 --master local bdce1_lr3.jar q2input2/smallset.csv 1000 0 1 50 15 123
spark-submit --class q2.BigDataClusterEater3 --master local bdce1_lr3.jar q2input2/smallset.csv 1000 0 1 100 15 123
spark-submit --class q2.BigDataClusterEater3 --master local bdce1_lr3.jar q2input2/smallset.csv 1000 0 1 200 15 123
spark-submit --class q2.BigDataClusterEater3 --master local bdce1_lr3.jar q2input2/smallset.csv 1000 1 1 200 15 123
