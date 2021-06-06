package q2;

import org.apache.spark.ml.Pipeline;
import org.apache.spark.ml.PipelineModel;
import org.apache.spark.ml.PipelineStage;
import org.apache.spark.ml.classification.LogisticRegression;
import org.apache.spark.ml.evaluation.MulticlassClassificationEvaluator;
import org.apache.spark.ml.feature.HashingTF;
import org.apache.spark.ml.feature.IDF;
import org.apache.spark.ml.feature.Tokenizer;
import org.apache.spark.ml.feature.VectorAssembler;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;

import static java.lang.System.exit;

public class SparkLoadLinearRegression {

	public static void main(String[] args) {
		String appName = "q2.SparkLoadLinearRegression";

		if (args.length != 3) {
			System.out.println("Usage: training test seed");
			exit(0);
		}
		String trainingFile = args[0];
		String testFile = args[1];
		Long randomSeed = Long.parseLong(args[2]);

		System.out.println("Random Seed: "+args[2]);

		SparkSession spark = SparkSession.builder()
				.appName(appName)
				//.master("local")
				.getOrCreate();

		StructType schema = new StructType(new StructField[]{
				new StructField("label", DataTypes.DoubleType, false, Metadata.empty()),
				new StructField("title", DataTypes.StringType, false, Metadata.empty()),
				new StructField("sentence", DataTypes.StringType, false, Metadata.empty())
		});
		Dataset<Row> trainingSet = spark.read().schema(schema).format("csv").load(trainingFile);

		Tokenizer sentenceTokenizer = new
				Tokenizer()
				.setInputCol("sentence")
				.setOutputCol("sentence_words");

		Tokenizer titleTokenizer = new
				Tokenizer()
				.setInputCol("title")
				.setOutputCol("title_words");

		int numFeatures = 200;
		HashingTF sentenceHashing = new HashingTF()
				.setInputCol("sentence_words")
				.setOutputCol("sw_rawFeatures")
				.setNumFeatures(numFeatures);

		HashingTF titleHashing = new HashingTF()
				.setInputCol("title_words")
				.setOutputCol("tw_rawFeatures")
				.setNumFeatures(numFeatures);

		IDF sentenceIdf = new IDF()
				.setInputCol("sw_rawFeatures")
				.setOutputCol("sw_idf_features");

		IDF titleIdf = new IDF()
				.setInputCol("tw_rawFeatures")
				.setOutputCol("tw_idf_features");

		VectorAssembler vectorAssembler = new VectorAssembler()
				.setInputCols(new String[] {"sw_idf_features", "tw_idf_features"})
				.setOutputCol("features");

		LogisticRegression lr = new LogisticRegression()
				.setMaxIter(300) //Set maximum iterations
				.setFeaturesCol("features")
				.setLabelCol("label");

		Pipeline pipeline = new Pipeline()
				.setStages(new PipelineStage[] {
						sentenceTokenizer,
						titleTokenizer,
						sentenceHashing,
						titleHashing,
						sentenceIdf,
						titleIdf,
						vectorAssembler,
						lr});

		PipelineModel pipelineModel = pipeline.fit(trainingSet);
		Dataset<Row> predictions_training = pipelineModel.transform(trainingSet);
		predictions_training.show();

		MulticlassClassificationEvaluator evaluator = new MulticlassClassificationEvaluator()
				.setLabelCol("indexedLabel")
				.setPredictionCol("prediction")
				.setMetricName("accuracy");

		double accuracy_training = evaluator.evaluate(predictions_training);
		System.out.println("Training Error = " + (1.0 - accuracy_training));


	}
}

