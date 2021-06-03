package SparkExample;

import java.util.Arrays;

import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;

import scala.Tuple2;

public class SparkKDDLoadTest {

	public static void main(String[] args) {
		String appName = "SparkKDDLoadTest";
		
		SparkSession spark = SparkSession.builder()
				.appName(appName)
				//.master("local")
				.getOrCreate();
		Dataset<Row> df = spark.read().format("csv").load("inputkdd/kdd.data");

		/*
		JavaRDD<String> lines = spark.read().textFile(args[0]).javaRDD();
		JavaPairRDD<String, Integer> records = 
				lines
				.flatMap(s -> Arrays.asList(s.split(" ")).iterator())
				.mapToPair(word -> new Tuple2<String, Integer>(word, 1))
				.reduceByKey((x,y) -> x+y);
		*/
		//records = records.sortByKey(true);
		//JavaRDD<String> formatted = records.map( new  Function<Tuple2<String,Integer>, String>() 
		//{
		//
		//	@Override
		//	public String call(Tuple2<String, Integer> theTuple) throws  Exception { 
		//		return  theTuple._1() +  " appears "  + theTuple._2();
		//	} 
		//});
		
		//records.repartition(1).saveAsTextFile(args[1]);
	}
}

