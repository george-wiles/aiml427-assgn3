#!/usr/bin/env bash

spark-submit --class q2.BigDataClusterEater1 --master local bdce1_lr.jar q2input/IMDB_Dataset.csv 20 123
#spark-submit --class q2.BigDataClusterEater1 --master local bdce1_lr.jar q2input/smallset.csv 20 123
