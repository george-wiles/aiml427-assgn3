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
appName=q2.George_SparkLoadLinearRegression,
 algorithm=LinearRegression,
 train=resources/kaggle-news-train.csv,
 test=resources/kaggle-news-test.csv,
 seed=1,
 features=1000,
 pca=PCA,
 pcaFeature=12
 useScalar=0
 ```














Build Instructions:
1) unpack the supplied archive that includes a copy of this readme file and verify the directory
structure and contents:
src/ == All code, scripts and logs are under this
scripts/ == bash shell scripts and logs
q1/ == bash shell scripts for building and execution
logs/ == log files
main/ == source code lives under here
java/ == java source code lives under here
q1/ == source code for q1
docs/ == results spreadsheet
lib/ == empty dir
jars/ == create this and populate with the hadoop and spark *.jar
2) copy the kdd.data file to the hdfs location relative to the users directory:
resources/kdd.data
3) unzip the hadoop_2.8.0_jars_files.zip archive and place all *jar files under lib/jars/
4) ssh to a cluster machine such as co246a-1
5) Navigate to src/scripts and run the following command to set your environment (assumes your
SHELL = zsh)
> source SetupSparkClasspath.zsh
6) Navigate to src/scripts/q1 and edit the build.sh file
7) verify the root_dir and lib_dir locations in the build.sh file
8) run the build.sh file to build the programs
> ./build.sh
Execution Instructions:
9) To run the logistic regression model without one-hot-encoding on the cluster run this
command:
> nohup ./execCluster_lr.sh > ../logs/output.cluster_lr.log 2>&1 &
10) Monitor progress of the process using:
> tail -f ../logs/output.cluster_lr.log
11) you will see the last line in the log "done." when the process is complete.
12) to obtain the output of the program navigate the the hadoop cluster management web page. A
url will be in the output log.
13) in the hadoop cluster page, find the completed job, open up the logs and view the standard
output log.
14) examples of these standard output logs can be found here: src/scripts/logs/
15) accuracy stats and other outputs from the program are found in these logs.
Notes on running the 4 programs:
Logistic Regression without One-Hot-Encoding: as described in 9-15 above
Logistic Regression with One-Hot-Encoding: use the ./execCluster_lr2.sh script as above
Decision Tree with MaxDepth=5: use the ./execCluster_dt.sh script as above
Decision Tree with MaxDepth=10: use the ./execCluster_dt2.sh script as above
Note on random seeds: the execution scripts loop through random seeds 1->10