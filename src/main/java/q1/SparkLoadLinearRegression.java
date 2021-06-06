package q1;

import org.apache.spark.ml.classification.BinaryLogisticRegressionTrainingSummary;
import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.classification.LogisticRegressionModel;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.feature.*;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static java.lang.System.exit;

public class SparkLoadLinearRegression {

	public static void main(String[] args) {
		String appName = "q1.SparkKDDLoadTest";

		if (args.length != 2) {
			System.out.println("provide input filename and random seed");
			exit(0);
		}
		String filename = args[0];
		Long randomSeed = Long.parseLong(args[1]);

		System.out.println("Random Seed: "+args[1]);

		SparkSession spark = SparkSession.builder()
				.appName(appName)
				//.master("local")
				.getOrCreate();
		Dataset<Row> df = spark.read().option("inferSchema", "true").format("csv").load(filename);

		String[] featureCols = {
				"duration","protocol_type","service","flag","src_bytes","dst_bytes",
				"land","wrong_fragment","urgent","hot","num_failed_logins","logged_in",
				"num_compromised","root_shell","su_attempted","num_root","num_file_creations",
				"num_shells","num_access_files","num_outbound_cmds","is_host_login","is_guest_login",
				"count","srv_count","serror_rate","srv_serror_rate","rerror_rate","srv_rerror_rate",
				"same_srv_rate","diff_srv_rate","srv_diff_host_rate","dst_host_count",
				"dst_host_srv_count","dst_host_same_srv_rate","dst_host_diff_srv_rate",
				"dst_host_same_src_port_rate","dst_host_srv_diff_host_rate","dst_host_serror_rate",
				"dst_host_srv_serror_rate","dst_host_rerror_rate","dst_host_srv_rerror_rate"
		};
		List cols = new LinkedList(Arrays.asList(featureCols));
		cols.add("label");

		Dataset<Row> ds = df.toDF((String[])cols.toArray(new String[cols.size()]));

		// Index labels, adding metadata to the label column.
		// Fit on whole dataset to include all labels in index.
		StringIndexerModel labelIndexer = new StringIndexer()
				.setInputCol("label")
				.setOutputCol("indexedLabel")
				.fit(ds);

		ds = labelIndexer.transform(ds);

		VectorAssembler vectorAssembler = new VectorAssembler()
				.setInputCols(featureCols)
				.setOutputCol("features");
		Dataset<Row> transformDs = vectorAssembler.transform(ds);

		VectorIndexerModel featureIndexer = new VectorIndexer()
				.setInputCol("features").setOutputCol("indexedFeatures")
				.setMaxCategories(12)
				.fit(transformDs);

		Dataset<Row> indexedDs = featureIndexer.transform(transformDs);

		//Scale the features
		StandardScaler scaler = new StandardScaler()
				.setInputCol("indexedFeatures")
				.setOutputCol("scaledFeatures")
				.setWithStd(true)
				.setWithMean(true);

		Dataset<Row> scaledDs = scaler.fit(indexedDs).transform(indexedDs);

		//Create training and test set
		Dataset<Row>[] splits = scaledDs.randomSplit (new double[]{0.7,0.3},randomSeed);
		Dataset<Row> training = splits[0];
		Dataset<Row> test = splits[1];

		//Define the Logistic Regression instance
		//We want vanila logistic regression here - not regularised.
		LogisticRegression lr = new LogisticRegression()
				.setMaxIter(50) //Set maximum iterations
				//.setRegParam(0.3) //Set Lambda
				.setFeaturesCol("features")
				.setLabelCol("indexedLabel")
				//.setElasticNetParam(0.8); //Set Alpha
		;

		// Fit the model
		LogisticRegressionModel lrModel = lr.fit(training);
		System.out.println("	logistic regression model coefficients:");
		System.out.println("Coefficients: "
				+ lrModel.coefficients() + " Intercept: " + lrModel.intercept());


		/*
			Model Stats
			codes from here: AIML427 Week11-12:24
		*/
		// Extract the summary from the returned model
		BinaryLogisticRegressionTrainingSummary trainingSummary = lrModel.binarySummary();
		// Obtain the loss per iteration.
		System.out.println("	Training LOSS per iteration");
		double[] objectiveHistory = trainingSummary.objectiveHistory();
		for (double lossPerIteration : objectiveHistory) {
			System.out.println(lossPerIteration);
		}

		// Obtain the ROC as a dataframe and areaUnderROC.
		Dataset<Row> roc = trainingSummary.roc();
		roc.show();
		roc.select("FPR").show();
		System.out.println("	Area under ROC:");
		System.out.println(trainingSummary.areaUnderROC());

		// Get the threshold corresponding to the maximum F-Measure
		Dataset<Row> fMeasure = trainingSummary.fMeasureByThreshold();
		//double maxFMeasure = fMeasure.select(functions.max("F-Measure")).head().getDouble(0);
		double maxFMeasure = fMeasure.select(functions.max("F-Measure")).head().getDouble(0);
		double bestThreshold = fMeasure.where(fMeasure.col("F-Measure").equalTo(maxFMeasure))
			.select("threshold")
			.head()
			.getDouble(0);
		//set this selected threshold for the model.
		lrModel.setThreshold(bestThreshold);

		String trainF1=String.valueOf(maxFMeasure);
		String f1Thresh=String.valueOf(bestThreshold);
		System.out.println("Best F1 Measure on Training:"+trainF1+" at threshold: "+f1Thresh);

		/*
			Make Predictions on our training set
		*/
		Dataset<Row> predictions_training = lrModel.transform(training);

		// Select (prediction, true label) and compute test error.
		MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
			.setLabelCol("indexedLabel")
			.setPredictionCol("prediction")
			.setMetricName("accuracy");

		double accuracy_training = evaluator.evaluate(predictions_training);
		System.out.println("Training Error = " + (1.0 - accuracy_training));

		MulticlassClassificationEvaluator evaluator2 = new MulticlassClassificationEvaluator()
				.setLabelCol("indexedLabel")
				.setPredictionCol("prediction")
				.setMetricName("accuracy");
		/*
			Make Predictions on our test set
		*/
		Dataset<Row> predictions_test = lrModel.transform(test);
		double accuracy_test = evaluator2.evaluate(predictions_test);
		System.out.println("Test Error = " + (1.0 - accuracy_test));

	}
}

