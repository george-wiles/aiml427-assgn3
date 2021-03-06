package q1;

import org.apache.spark.ml.classification.DecisionTreeClassificationModel;
import org.apache.spark.ml.classification.DecisionTreeClassifier;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.feature.*;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static java.lang.System.exit;

public class SparkLoadDecisionTree {


	public static void main(String[] args) {
		String appName = "q1.SparkLoadDecisionTree";

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

		// Index labels, adding metadata to the label column.
		// Fit on whole dataset to include all labels in index.
		StringIndexerModel labelIndexer = new StringIndexer()
				.setInputCol("label")
				.setOutputCol("indexedLabel").fit(ds);

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

		Dataset<Row> scaledDs = scaler
				.fit(indexedDs)
				.transform(indexedDs);

		//Define the Logistic Regression instance
		DecisionTreeClassifier dtc = new DecisionTreeClassifier()
				.setFeaturesCol("features")
				.setLabelCol("indexedLabel");

		DecisionTreeClassificationModel dtModel = dtc.fit(training);

		Dataset<Row> predictions_training = dtModel.transform(training);

		MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
				.setLabelCol("indexedLabel")
				.setPredictionCol("prediction")
				.setMetricName("accuracy");

		double accuracy_training = evaluator.evaluate(predictions_training);
		System.out.println("Training Error = " + (1.0 - accuracy_training));

		Dataset<Row> predictions_test = dtModel.transform(test);
		double accuracy_test = evaluator.evaluate(predictions_test);
		System.out.println("Test Error = " + (1.0 - accuracy_test));

	}
}

