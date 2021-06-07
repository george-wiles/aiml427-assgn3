#!/usr/bin/env bash

#spark-submit --class q2.BigDataClusterEater1 --master local bdce1_lr.jar q2input/IMDB_Dataset_fixed.csv 20 123
#spark-submit --class q2.BigDataClusterEater1 --master local bdce1_lr.jar q2input2/smallset.csv 20 123
#input file, hashing features, use scaling, use pca, pcaK, random seed
#spark-submit --class q2.BigDataClusterEater2 --master local bdce1_lr2.jar q2input2/smallset.csv 10 0 0 1 123
#spark-submit --class q2.BigDataClusterEater2 --master local bdce1_lr2.jar q2input2/smallset.csv 30 0 0 1 123
#spark-submit --class q2.BigDataClusterEater2 --master local bdce1_lr2.jar q2input2/smallset.csv 100 0 0 1 123
#spark-submit --class q2.BigDataClusterEater2 --master local bdce1_lr2.jar q2input2/smallset.csv 300 0 0 1 123
spark-submit --class q2.BigDataClusterEater2 --master local bdce1_lr2.jar q2input2/smallset.csv 1000 0 0 1 123
spark-submit --class q2.BigDataClusterEater2 --master local bdce1_lr2.jar q2input2/smallset.csv 1000 0 1 5 123
spark-submit --class q2.BigDataClusterEater2 --master local bdce1_lr2.jar q2input2/smallset.csv 1000 0 1 10 123
spark-submit --class q2.BigDataClusterEater2 --master local bdce1_lr2.jar q2input2/smallset.csv 1000 0 1 50 123
spark-submit --class q2.BigDataClusterEater2 --master local bdce1_lr2.jar q2input2/smallset.csv 1000 0 1 100 123
spark-submit --class q2.BigDataClusterEater2 --master local bdce1_lr2.jar q2input2/smallset.csv 1000 1 1 100 123
