package cn.itcast.hadoop.mr.habase;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.MultiTableOutputFormat;
import org.apache.hadoop.hbase.mapreduce.TableInputFormat;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.util.GenericOptionsParser;

public class IndexBuilder {
	static class Map extends TableMapper<ImmutableBytesWritable, Put>{
		
		private java.util.Map<byte[], ImmutableBytesWritable> indexes=new HashMap<byte[], ImmutableBytesWritable>();
		private String familyName;
		@Override
		protected void setup(Mapper<ImmutableBytesWritable, Result, ImmutableBytesWritable, Put>.Context context)
				throws IOException, InterruptedException {
			Configuration conf=context.getConfiguration();
			
			String tableName=conf.get("tableName");
			String familyName=conf.get("familyName");
			
			String[] qualifiers=conf.getStrings("qualifiers");
			
			for(String q:qualifiers){
				indexes.put(Bytes.toBytes(q), new ImmutableBytesWritable(Bytes.toBytes(tableName+"-"+q)));
			}
			
		}
		@Override
		protected void map(ImmutableBytesWritable key, Result value,
				Mapper<ImmutableBytesWritable, Result, ImmutableBytesWritable, Put>.Context context)
				throws IOException, InterruptedException {
			Set<byte[]> keys=indexes.keySet();
			for(byte[] k:keys){
				ImmutableBytesWritable indextableName=indexes.get(k);
				byte[] val=value.getValue(Bytes.toBytes(familyName), k);
				
				if(val!=null){
					Put put=new Put(val);
					put.add(Bytes.toBytes("fl"), Bytes.toBytes("id"), key.get());
					
					context.write(indextableName, put);
					
				}
			}
		}
	}
	public static void main(String[] args) throws Exception{
		Configuration conf=HBaseConfiguration.create();
		String[] otherArgs=new GenericOptionsParser(conf, args).getRemainingArgs();
		
		if(otherArgs.length<3){
			System.exit(-1);
		}
		
		String tableName=otherArgs[0];
		String columnFamily=otherArgs[1];
		conf.set("tableName", tableName);
		conf.set("columnFamily", columnFamily);
		
		String[] qualifiers=new String[otherArgs.length-2];
		
		for(int i=0;i<qualifiers.length;i++){
			qualifiers[i]=otherArgs[i+2];
		}
		
		conf.setStrings("qualifiers", qualifiers);
		
		Job job=new Job(conf,tableName);
		
		job.setJarByClass(IndexBuilder.class);
		job.setMapperClass(Map.class);
		job.setNumReduceTasks(0);
		job.setInputFormatClass(TableInputFormat.class);
		job.setOutputFormatClass(MultiTableOutputFormat.class);
		
		Scan scan=new Scan();
		scan.setCaching(100);
		
		TableMapReduceUtil.initTableMapperJob(tableName, scan, Map.class, ImmutableBytesWritable.class, Put.class, job);
		
		job.waitForCompletion(true);
	}
}
