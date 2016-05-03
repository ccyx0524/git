package cn.itcast.hadoop.mr.test;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;


public class WordCount {

	public class WCMapper extends Mapper<LongWritable, Text, Text, LongWritable>{
		@Override
		protected void map(LongWritable key, Text value,
				Mapper<LongWritable, Text, Text, LongWritable>.Context context)
				throws IOException, InterruptedException {
			String line=value.toString();
			
			String[] words=StringUtils.split(line, " ");
			
			for(String word:words){
				context.write(new Text(word), new LongWritable(1));
			}
		}
	}
	
	public class WCReducer extends Reducer<Text, LongWritable, Text, LongWritable>{
		@Override
		protected void reduce(Text key, Iterable<LongWritable> values,
				Reducer<Text, LongWritable, Text, LongWritable>.Context context) throws IOException, InterruptedException {
			long count=0;
			
			for(LongWritable value:values){
				count+=value.get();
			}
			
			context.write(key, new LongWritable(count));
		}
	}
	
	public static void main(String[] args) throws Exception {
		Configuration conf=new Configuration();
		
		Job wcjob=Job.getInstance(conf);
		
		wcjob.setJarByClass(WordCount.class);
		
		wcjob.setMapperClass(WCMapper.class);
		wcjob.setReducerClass(WCReducer.class);
		
		wcjob.setCombinerClass(WCReducer.class);
		
		wcjob.setOutputKeyClass(Text.class);
		wcjob.setOutputValueClass(LongWritable.class);
		
		wcjob.setMapOutputKeyClass(Text.class);
		wcjob.setMapOutputValueClass(LongWritable.class);
		
		FileInputFormat.setInputPaths(wcjob, new Path("hdfs://weekend110:9000/wc/input"));
		FileOutputFormat.setOutputPath(wcjob, new Path("hdfs://weekend110:9000/wc/output"));
		
		wcjob.waitForCompletion(true);
	}
}
