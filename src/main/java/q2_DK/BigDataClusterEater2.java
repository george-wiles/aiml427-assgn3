package q2;

import org.apache.spark.ml.classification.BinaryLogisticRegressionTrainingSummary;
import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.classification.LogisticRegressionModel;
import org.apache.spark.ml.classification.DecisionTreeClassificationModel;
import org.apache.spark.ml.classification.DecisionTreeClassifier;
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
import org.apache.spark.ml.feature.StopWordsRemover;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import org.apache.spark.ml.feature.PCA;
import org.apache.spark.ml.feature.PCAModel;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ArrayList;

import static java.lang.System.exit;

public class BigDataClusterEater2 {

	public static void main(String[] args) {
		String appName = "q2.BigDataClusterEater2";

		if (args.length != 6) {
			System.out.println("args: input file, hashing features, use scaling, use pca, pcaK, random seed");
			exit(0);
		}

		System.out.println("InputFile: "+args[0]);
		System.out.println("Number of Hashing Features: "+args[1]);
		System.out.println("Use Scaling: "+args[2]);
		System.out.println("Use PCA: "+args[3]);
		System.out.println("pcaK: "+args[4]);
		System.out.println("Random Seed: "+args[5]);

		String inFile = args[0];
		int numFeatures = Integer.parseInt(args[1]);
		int useScaling = Integer.parseInt(args[2]);
		int usePCA = Integer.parseInt(args[3]);
		int pcaK = Integer.parseInt(args[4]);
		long randomSeed = Long.parseLong(args[5]);

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

		String pipeFeature = "review";

		// Index labels, adding metadata to the label column.
		// Fit on whole dataset to include all labels in index.
		StringIndexer labelIndexer = new StringIndexer()
				.setInputCol("sentiment")
				.setOutputCol("indexedSentiment")
				.setHandleInvalid("skip");

		Tokenizer tokenizer = new Tokenizer()
			.setInputCol(pipeFeature)
			.setOutputCol("words");

		StopWordsRemover stopwords = new StopWordsRemover()
			.setInputCol("words")
			.setOutputCol("filtered");

		pipeFeature = new String("filtered");

		HashingTF hashingTF = new HashingTF()
			.setInputCol(pipeFeature)
			.setOutputCol("rawFeatures")
			.setNumFeatures(numFeatures);		

		pipeFeature = new String("rawFeatures");

		PCA pca = new PCA()
			.setInputCol(pipeFeature)
			.setOutputCol("pcaFeatures")
			.setK(pcaK);
		if ( usePCA == 1 ) {
			pipeFeature = new String("pcaFeatures");
		}

		//Scale the features
		StandardScaler scaler = new StandardScaler()
				.setInputCol(pipeFeature)
				.setOutputCol("scaledFeatures")
				.setWithStd(true)
				.setWithMean(true);
		if ( useScaling == 1 ) {
			pipeFeature = new String("scaledFeatures");
		}

		//Define the Logistic Regression instance
		//We want vanila logistic regression here - not regularised.
		LogisticRegression lr = new LogisticRegression()
				.setMaxIter(10) //Set maximum iterations
				.setFeaturesCol(pipeFeature)
				.setLabelCol("indexedSentiment")
		;

		ArrayList<PipelineStage> pipelinestages = new ArrayList();
		pipelinestages.add(labelIndexer);
		pipelinestages.add(tokenizer);
		pipelinestages.add(stopwords);
		pipelinestages.add(hashingTF);
		if ( usePCA == 1 ) {
			pipelinestages.add(pca);
		}
		if ( useScaling == 1 ) {
			pipelinestages.add(scaler);
		}
		pipelinestages.add(lr);

		PipelineStage[] pls = pipelinestages.toArray(new PipelineStage[pipelinestages.size()]);

		Pipeline pipeline = new Pipeline()
			//.setStages(new PipelineStage[] {labelIndexer,tokenizer,hashingTF,vectorAssembler,scaler,lr});
			.setStages(pls);

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

