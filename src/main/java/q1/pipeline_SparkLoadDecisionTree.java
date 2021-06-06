package q1;

import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.classification.DecisionTreeClassificationModel;
import org.apache.spark.ml.classification.DecisionTreeClassifier;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.feature.OneHotEncoderEstimator;
import org.apache.spark.ml.feature.StandardScaler;
import org.apache.spark.ml.feature.StringIndexer;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static java.lang.System.exit;

public class pipeline_SparkLoadDecisionTree {

	public static void main(String[] args) {
		String appName = "q1.pipeline_SparkLoadDecisionTree";

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

		//Create training and test set
		Dataset<Row>[] splits = ds.randomSplit (new double[]{0.7,0.3},randomSeed);
		Dataset<Row> training = splits[0];
		Dataset<Row> test = splits[1];

  		String[] catCols = {"protocol_type", "flag", "wrong_fragment", "urgent", "su_attempted"};
  		String[] catVecCols = {"protocol_type_vec", "flag_vec", "wrong_fragment_vec", "urgent_vec", "su_attempted_vec"};

		List featureCols2 = new LinkedList(Arrays.asList(featureCols));
		for (int j=0; j < catCols.length; j++) {
			featureCols2.add(catVecCols[j]);
		}
		String[] featureCols3 = (String[]) featureCols2.toArray(new String[featureCols2.size()]);

		for (int i=0; i < featureCols.length; i++) {
			for (int j=0; j < catCols.length; j++) {
				if ( featureCols[i] == catCols[j] ) {
					featureCols[i] = catVecCols[j];
				}
			}
			System.out.println(String.valueOf(i)+":"+featureCols[i]);
		}

		// Index labels, adding metadata to the label column.
		// Fit on whole dataset to include all labels in index.
		StringIndexer labelIndexer = new StringIndexer()
				.setInputCol("label")
				.setOutputCol("indexedLabel")
				.setHandleInvalid("keep");

		//convert categoricals
		//OneHotEncoder encoder = new OneHotEncoder()
		OneHotEncoderEstimator encoder = new OneHotEncoderEstimator()
  			.setInputCols(catCols)
  			.setOutputCols(catVecCols).setHandleInvalid("keep");

  			//.setInputCol("protocol_type")
  			//.setOutputCol("protocol_type_vec");
		// Get the features into a single column as in libSVM format
		VectorAssembler vectorAssembler = new VectorAssembler()
				.setInputCols(featureCols)
				.setOutputCol("features");
		//Scale the features
		StandardScaler scaler = new StandardScaler()
				.setInputCol("features")
				.setOutputCol("scaledFeatures")
				.setWithStd(true)
				.setWithMean(true);

		//Define the Logistic Regression instance
		//We want vanila logistic regression here - not regularised.
		DecisionTreeClassifier dt = new DecisionTreeClassifier()
				.setFeaturesCol("scaledFeatures")
				.setLabelCol("indexedLabel");

		Pipeline pipeline = new Pipeline()
			.setStages(new PipelineStage[] {labelIndexer,encoder,vectorAssembler,scaler,dt});

		//fit pipeline on training
		PipelineModel pipelineModel = pipeline.fit(training);

		//Get fitted classifier model
		//LogisticRegressionModel lrModel = pipelineModel.stages.last.asInstanceOf[LogisticRegressionModel];
		DecisionTreeClassificationModel dtModel = (DecisionTreeClassificationModel) (pipelineModel.stages()[pipelineModel.stages().length-1]);

		Dataset<Row> predictions_training = pipelineModel.transform(training);
		Dataset<Row> results = pipelineModel.transform(test);
		results.show();

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
		Dataset<Row> predictions_test = pipelineModel.transform(test);
		double accuracy_test = evaluator2.evaluate(predictions_test);
		System.out.println("Test Error = " + (1.0 - accuracy_test));

	}
}

