package q2;

import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.classification.DecisionTreeClassifier;
import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.feature.*;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static java.lang.System.exit;

public class SparkLoadDecisionTreeAIGNewsWithPCA {

	public static void main(String[] args) {
		String appName = "q2.George_SparkLoad_DecisionTree";
		String algorithm = "DecisionTree";
		LocalDateTime start = LocalDateTime.now();

		if (args.length != 8) {
			System.out.println("Usage: training test seed features PCA PCA-count Tree-depth");
			exit(0);
		}
		String trainingFile = args[0];
		String testFile = args[1];
		Long randomSeed = Long.parseLong(args[2]);
		int numFeatures = Integer.parseInt(args[3]);
		String pca = args[4];
		int numPCAFeatures = Integer.parseInt(args[5]);
		int treeMaxDepth = Integer.parseInt(args[6]);
		Boolean useScalar = Boolean.getBoolean(args[7]);

		System.out.println(
				String.format("\nappName=%s,\n algorithm=%s,\n train=%s,\n test=%s,\n seed=%d,\n features=%d,\n pca=%s,\n pcaFeatures=%d\n treeDepth=%d\n, useScalar=%s\n",
				appName,
				algorithm,
				trainingFile,
				testFile,
				randomSeed,
				numFeatures,
				pca,
				numPCAFeatures,
				treeMaxDepth,
				useScalar.toString()));

		SparkSession spark = SparkSession.builder()
				.appName(appName)
				.getOrCreate();

		StructType schema = new StructType(new StructField[]{
				new StructField("Class Index", DataTypes.StringType, false, Metadata.empty()),
				new StructField("Title", DataTypes.StringType, false, Metadata.empty()),
				new StructField("Description", DataTypes.StringType, false, Metadata.empty())
		});
		Dataset<Row> trainingSet = spark.read().option("header","true").schema(schema).format("csv").load(trainingFile);
		Dataset<Row> testSet = spark.read().option("header","true").schema(schema).format("csv").load(testFile);

		StringIndexer labelIndexer = new StringIndexer()
				.setInputCol("Class Index")
				.setOutputCol("indexedLabel")
				.setHandleInvalid("keep");

		RegexTokenizer sentenceRegexTokenizer = new RegexTokenizer()
				.setPattern("\\W+")
				.setToLowercase(true)
				.setMinTokenLength(1)
				.setInputCol("Description")
				.setOutputCol("sentence_words_clean");

		StopWordsRemover swStopRemover = new StopWordsRemover()
				.setInputCol("sentence_words_clean")
				.setOutputCol("sentence_stop_words");

		HashingTF sentenceHashing = new HashingTF()
				.setInputCol("sentence_stop_words")
				.setOutputCol("sw_rawFeatures")
				.setNumFeatures(numFeatures);

		IDF sentenceIdf = new IDF()
				.setInputCol("sw_rawFeatures")
				.setOutputCol("sw_idf_features");

		RegexTokenizer titleRegexTokenizer = new RegexTokenizer()
				.setPattern("\\W+")
				.setToLowercase(true)
				.setMinTokenLength(1)
				.setInputCol("Title")
				.setOutputCol("title_words_clean");

		StopWordsRemover twStopRemover = new StopWordsRemover()
				.setInputCol("title_words_clean")
				.setOutputCol("title_stop_words");

		HashingTF titleHashing = new HashingTF()
				.setInputCol("title_stop_words")
				.setOutputCol("tw_rawFeatures")
				.setNumFeatures(numFeatures);

		IDF titleIdf = new IDF()
				.setInputCol("tw_rawFeatures")
				.setOutputCol("tw_idf_features");

		VectorAssembler vectorAssembler = new VectorAssembler()
				.setInputCols(new String[] {"tw_idf_features", "sw_idf_features"})
				.setOutputCol("features");

		List<PipelineStage> stages = new LinkedList<>();
		stages.add(labelIndexer);
		// SENTENCE
		stages.add(sentenceRegexTokenizer);
		stages.add(swStopRemover);
		stages.add(sentenceHashing);
		stages.add(sentenceIdf);
		// TITLE
		stages.add(titleRegexTokenizer);
		stages.add(twStopRemover);
		stages.add(titleHashing);
		stages.add(titleIdf);
		// COMBINE
		if (pca.equals("PCA")) {
			PCA titlePca = new PCA()
					.setInputCol("tw_idf_features")
					.setOutputCol("tw_pca_features")
					.setK(numPCAFeatures);

			PCA sentencePca = new PCA()
					.setInputCol("sw_idf_features")
					.setOutputCol("sw_pca_features")
					.setK(numPCAFeatures);

			VectorAssembler vectorAssemblerPCA = new VectorAssembler()
					.setInputCols(new String[] {"tw_pca_features", "sw_pca_features"})
					.setOutputCol("features");

			stages.add(sentencePca);
			stages.add(titlePca);
			stages.add(vectorAssemblerPCA);
		} else {
			stages.add(vectorAssembler);
		}
		if (useScalar) {
			StandardScaler scaler = new StandardScaler()
					.setInputCol("features")
					.setOutputCol("scaledFeatures")
					.setWithStd(true)
					.setWithMean(true);

			DecisionTreeClassifier dt = new DecisionTreeClassifier()
					.setFeaturesCol("scaledFeatures")
					.setLabelCol("indexedLabel").setMaxDepth(treeMaxDepth);
			stages.add(scaler);
			stages.add(dt);
		} else {
			DecisionTreeClassifier dt = new DecisionTreeClassifier()
					.setFeaturesCol("features")
					.setLabelCol("indexedLabel").setMaxDepth(treeMaxDepth);
			stages.add(dt);
		}

		PipelineStage[] stageArray = stages.toArray(new PipelineStage[stages.size()]);
		System.err.println("stages=" + Arrays.toString(stageArray));

		Pipeline pipeline = new Pipeline().setStages(stageArray);
		PipelineModel pipelineModel = pipeline.fit(trainingSet);
		Dataset<Row> predictions_training = pipelineModel.transform(trainingSet);
		predictions_training.show();

		MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
				.setLabelCol("indexedLabel")
				.setPredictionCol("prediction")
				.setMetricName("accuracy");

		double accuracy_training = evaluator.evaluate(predictions_training);
		System.out.println("Training Error = " + (1.0 - accuracy_training));

		Dataset<Row> predictions_test = pipelineModel.transform(testSet);
		double accuracy_test = evaluator.evaluate(predictions_test);
		System.out.println("Test Error = " + (1.0 - accuracy_test));

		LocalDateTime end = LocalDateTime.now();
		System.out.println("Start Time = " + start);
		System.out.println("End Time = " + end);
		System.out.println("Elapsed Time = " + Duration.between(start, end));

	}
}

