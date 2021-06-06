package q2;

import org.apache.spark.ml.classification.BinaryLogisticRegressionTrainingSummary;
import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.classification.LogisticRegressionModel;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.feature.*;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.functions;
import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.feature.HashingTF;
import org.apache.spark.ml.feature.Tokenizer;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static java.lang.System.exit;

public class BigDataClusterEater1 {

	public static void main(String[] args) {
		String appName = "q2.BigDataClusterEater1";

		if (args.length != 3) {
			System.out.println("args: input file, hashing features, random seed");
			exit(0);
		}

		System.out.println("InputFile: "+args[0]);
		System.out.println("Number of Hashing Features: "+args[1]);
		System.out.println("Random Seed: "+args[2]);

		String inFile = args[0];
		int numFeatures = Integer.parseInt(args[1]);
		long randomSeed = Long.parseLong(args[2]);

		SparkSession spark = SparkSession.builder()
				.appName(appName)
				.getOrCreate();

		StructType schema = new StructType(new StructField[]{
			new StructField("review", DataTypes.StringType, false, Metadata.empty()),
			new StructField("sentiment", DataTypes.StringType, false, Metadata.empty())
		});

                Dataset<Row> allData = spark.read().option("header","true").schema(schema).format("csv").load(inFile);

		//Create training and test set
		Dataset<Row>[] splits = allData.randomSplit (new double[]{0.7,0.3}, randomSeed);
		Dataset<Row> training = splits[0];
		Dataset<Row> test = splits[1];

		// Index labels, adding metadata to the label column.
		// Fit on whole dataset to include all labels in index.
		StringIndexer labelIndexer = new StringIndexer()
				.setInputCol("sentiment")
				.setOutputCol("indexedSentiment")
				.setHandleInvalid("skip");

		Tokenizer tokenizer = new Tokenizer()
			.setInputCol("review")
			.setOutputCol("words");

		HashingTF hashingTF = new HashingTF()
			.setInputCol("words")
			.setOutputCol("rawFeatures")
			.setNumFeatures(numFeatures);		

		// Get the features into a single column as in libSVM format
		VectorAssembler vectorAssembler = new VectorAssembler()
				.setInputCols(new String[] {"rawFeatures"})
				.setOutputCol("features");
		//Scale the features
		StandardScaler scaler = new StandardScaler()
				.setInputCol("features")
				.setOutputCol("scaledFeatures")
				.setWithStd(true)
				.setWithMean(true);

		//Define the Logistic Regression instance
		//We want vanila logistic regression here - not regularised.
		LogisticRegression lr = new LogisticRegression()
				.setMaxIter(10) //Set maximum iterations
				//.setRegParam(0.1) //Set Lambda
				.setFeaturesCol("scaledFeatures")
				//.setFeaturesCol("features")
				.setLabelCol("indexedSentiment")
				//.setElasticNetParam(0.0); //Set Alpha
		;

		System.out.println("LR Reg:"+String.valueOf(lr.getRegParam()));
		System.out.println("LR ElasticNet:"+String.valueOf(lr.getElasticNetParam()));

		Pipeline pipeline = new Pipeline()
			.setStages(new PipelineStage[] {labelIndexer,tokenizer,hashingTF,vectorAssembler,scaler,lr});

		//fit pipeline on training
		PipelineModel pipelineModel = pipeline.fit(training);

		//Get fitted classifier model
		//LogisticRegressionModel lrModel = pipelineModel.stages.last.asInstanceOf[LogisticRegressionModel];
		LogisticRegressionModel lrModel = (LogisticRegressionModel) (pipelineModel.stages()[pipelineModel.stages().length-1]);
		/*
                System.out.println("    logistic regression model coefficients:");
                System.out.println("Coefficients: "
                                + lrModel.coefficients() + " Intercept: " + lrModel.intercept());
		double[] coefs=lrModel.coefficients().toArray();
		*/
		/*
		for (int i=0;i<coefs.length;i++) {
                	System.out.println(featureCols3[i]+":"+String.valueOf(coefs[i]));
		}
		*/

		/*
			Make Predictions on our training set
		*/
		Dataset<Row> predictions_training = pipelineModel.transform(training);
		Dataset<Row> results = pipelineModel.transform(test);
		results.show();

		// Select (prediction, true label) and compute test error.
		MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
			.setLabelCol("indexedSentiment")
			.setPredictionCol("prediction")
			.setMetricName("accuracy");

		double accuracy_training = evaluator.evaluate(predictions_training);
		System.out.println("Training Error = " + (1.0 - accuracy_training));

		MulticlassClassificationEvaluator evaluator2 = new MulticlassClassificationEvaluator()
				.setLabelCol("indexedSentiment")
				.setPredictionCol("prediction")
				.setMetricName("accuracy");
		/*
			Make Predictions on our test set
		*/
		Dataset<Row> predictions_test = pipelineModel.transform(test);
		double accuracy_test = evaluator2.evaluate(predictions_test);
		System.out.println("Test Error = " + (1.0 - accuracy_test));

	}
}

