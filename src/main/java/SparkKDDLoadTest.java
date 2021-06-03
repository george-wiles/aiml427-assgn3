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
		Dataset<Row> df = spark.read().format("csv").load("inputkdd/kdd.data").toDF("duration","protocol_type","service","flag","src_bytes","dst_bytes","land","wrong_fragment","urgent","hot","num_failed_logins","logged_in","num_compromised","root_shell","su_attempted","num_root","num_file_creations","num_shells","num_access_files","num_outbound_cmds","is_host_login","is_guest_login","count","srv_count","serror_rate","srv_serror_rate","rerror_rate","srv_rerror_rate","same_srv_rate","diff_srv_rate","srv_diff_host_rate","dst_host_count","dst_host_srv_count","dst_host_same_srv_rate","dst_host_diff_srv_rate","dst_host_same_src_port_rate","dst_host_srv_diff_host_rate","dst_host_serror_rate","dst_host_srv_serror_rate","dst_host_rerror_rate","dst_host_srv_rerror_rate","class");
;

		

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

