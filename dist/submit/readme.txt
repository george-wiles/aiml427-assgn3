#----------------------------------------------------------------------
# AIML 427 Big Data - Question Two Individual
#----------------------------------------------------------------------
# README
#
# Authors: George Wiles
# Purpose: Setup, Build and Run instructions for programs in Q2
# Date: June 8th, 2021
#----------------------------------------------------------------------

The following instructions assume you have the code and log archives supplied with the AIML427
Assignment 3 Q1 submission and have placed the archive on unix/linux storage accessible to the ecs
computers.

Dataset: https://www.kaggle.com/amananandrai/ag-news-classification-dataset
Test Dataset: https://www.kaggle.com/amananandrai/ag-news-classification-dataset?select=test.csv
Train Dataset: https://www.kaggle.com/amananandrai/ag-news-classification-dataset?select=train.csv

# Step 1. Download data from ECS submission website for Assignment 3
- kaggle-news-test.csv.zip
- kaggle-news-train.csv.zip
- submit-jars-logs.zip

# Step 2. Unzip files into local directory on ECS machine
$unzip submit-jars-logs.zip
-- submit
 |-- logs
 |-- skdd_dt_pca.jar
 |-- skdd_lr_pca.jar
 |-- execCluster_all_with_scalar.sh
 |-- execCluster_all_no_scalar.sh
$unzip kaggle-news-test.csv.zip
-- train.csv 29066993
$unzip kaggle-news-train.csv.zip
-- test.csv 1831789

# Step 3. Follow Assignment #3 instructions to copy the train.csv and test.csv onto a Spark hadoop dfs drive
Note if you are using the provided cluster scripts, they are parameterized to expect the following naming conventions,
however you can run the spark-submit files directly providing your own command line parameters

$hdfs dfs -moveToLocal train.csv ./resources/kaggle-news-train.csv
$hdfs dfs -moveToLocal test.csv ./resources/kaggle-news-test.csv

# Step 4. Execute LR and DT with scalar, with PCA and without PCA
$execCluster_all_with_scalar.sh

# Step 5. Execute LR and DT without scalar, with PCA and without PCA
$execCluster_all_with_scalar.sh

By default the script will run 2 iterations of the above, but can be varied up to 10 times, however
as unlike question 1 there is no random seed, but it is useful if you want to run a number of times to
ensure processing timings are consistent

# Running spark command without a script
$ spark-submit --class q2.SparkLoadDecisionTreeAIGNewsWithPCA --master local skdd_dt_pca.jar resources/kaggle-1000-train.csv resources/kaggle-1000-test.csv 123 800 NO_PCA 0 10 0
```
appName=q2.George_SparkLoad_DecisionTree,
 algorithm=DecisionTree,
 train=resources/kaggle-news-train.csv,
 test=resources/kaggle-news-test.csv,
 seed=123,
 features=800,
 pca=NO_PCA,
 pcaFeatures=0
 treeDepth=10
 useScalar=0
```

$ spark-submit --class q2.SparkLoadDecisionTreeAIGNewsWithPCA --master local skdd_dt_pca.jar resources/kaggle-1000-train.csv resources/kaggle-1000-test.csv 123 800 NO_PCA 0 10 0
```
appName=q2.George_SparkLoad_DecisionTree,
 algorithm=DecisionTree,
 train=resources/kaggle-news-train.csv,
 test=resources/kaggle-news-test.csv,
 seed=123,
 features=800,
 pca=NO_PCA,
 pcaFeatures=0
 treeDepth=10
 useScalar=0
```
$ spark-submit --class q2.SparkLoadLinearRegressionAIGNewsWithPCA --master local skdd_lr_pca.jar resources/kaggle-news-train.csv resources/kaggle-news-test.csv 1 1000 PCA 12 0
```
% spark-submit --class q2.SparkLoadLinearRegressionAIGNewsWithPCA --master local skdd_lr_pca.jar resources/kaggle-news-train.csv resources/kaggle-news-test.csv 1 1000 PCA 12 0
appName=q2.George_SparkLoadLinearRegression,
 algorithm=LinearRegression,
 train=resources/kaggle-news-train.csv,
 test=resources/kaggle-news-test.csv,
 seed=1,
 features=1000,
 pca=PCA,
 pcaFeature=12
 useScalar=0

21/06/08 23:45:20 INFO spark.SparkContext: Running Spark version 2.4.2
21/06/08 23:45:20 INFO spark.SparkContext: Submitted application: q2.George_SparkLoadLinearRegression
... etc...
 ```
