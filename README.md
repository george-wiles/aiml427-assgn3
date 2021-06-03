# aiml427-assgn3

## references
* https://spark.apache.org/docs/latest/sql-data-sources-load-save-functions.html

## compile and run notes
```
	source SetupSparkClasspath.zsh
	javac -cp "jars/*" -d swc_classes SparkWordCount.java
	jar cvf swc.jar -C swc_classes/ .

	spark-submit --class SparkExample.SparkWordCount --master yarn --deploy-mode cluster swc.jar input2 output2
```

## experiments:

* Load the kdd.data and do nothing with it but run the thing on the cluster
	* see src/main/java/SparkKDDLoadTest.java
	* SUCCEEDED ... didn't do anything mind you other than load the data
* Load the kdd.data
	* assign colnames
	* separate X (features) and y (response)
	* make y numeric
