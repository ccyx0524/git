package cn.itcast.hadoop.mr.habase;

import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableOutputFormat;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.Writable;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

public class ImportFromFile {
	
	public static final String NAME="ImportFromFile";
	public enum Counters{LINES}
	static class ImportMapper extends Mapper<LongWritable, Text, ImmutableBytesWritable, Writable>{
		private byte[] family=null;
		private byte[] qualifier=null;
		@Override
		protected void setup(Mapper<LongWritable, Text, ImmutableBytesWritable, Writable>.Context context)
				throws IOException, InterruptedException {
			String column=context.getConfiguration().get("conf.column");
			byte[][] colkey=KeyValue.parseColumn(Bytes.toBytes(column));
			family=colkey[1];
			if(colkey.length>1){
				qualifier=colkey[1];
			}
		}
		@Override
		protected void map(LongWritable offset, Text line,
				Mapper<LongWritable, Text, ImmutableBytesWritable, Writable>.Context context)
				throws IOException, InterruptedException {
			try{
				String lineString=line.toString();
				byte[] rowkey=DigestUtils.md5(lineString);
				Put put=new Put(rowkey);
				put.add(family, qualifier, Bytes.toBytes(lineString));
				//context.write(new ImmutableBytesWritable(rowkey),put);
				context.getCounter(Counters.LINES).increment(1);
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	private static CommandLine pareArgs(String[] args){
		CommandLine cmd=null;
		return cmd;
	}
	public static void main(String[] args) throws Exception{
		Configuration conf=HBaseConfiguration.create();
		String[] otherArgs=new GenericOptionsParser(conf, args).getRemainingArgs();
		CommandLine cmd=pareArgs(otherArgs);
		String table=cmd.getOptionValue("t");
		String input=cmd.getOptionValue("i");
		String column=cmd.getOptionValue("c");
		conf.set("conf.column",column);
		
		Job job=new Job(conf,"Import from file"+input+"into table"+table);
		job.setJarByClass(ImportFromFile.class);
		job.setMapperClass(ImportMapper.class);
		job.setOutputFormatClass(TableOutputFormat.class);
		job.getConfiguration().set(TableOutputFormat.OUTPUT_TABLE, table);
		
		job.setOutputKeyClass(ImmutableBytesWritable.class);
		job.setOutputValueClass(Writable.class);
		job.setNumReduceTasks(0);
		FileInputFormat.addInputPath(job, new Path(input));
		
		System.exit(job.waitForCompletion(true)?0:1);
	}
}
