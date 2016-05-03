package cn.itcast.hadoop.mr.llyy.enhance;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.RecordWriter;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class LogOutputFormat<K,V> extends FileOutputFormat<K, V>{

	@Override
	public RecordWriter<K, V> getRecordWriter(TaskAttemptContext arg0) throws IOException, InterruptedException {
		FileSystem fs=FileSystem.get(new Configuration());
		FSDataOutputStream enchancedOs=fs.create(new Path("/liulang/output/enchanced"));
		FSDataOutputStream tocreawlOS=fs.create(new Path("/liuliang/output/tocrawl"));
		
		return new LogEnchancdRecodWriter<K,V>(enchancedOs,tocreawlOS);
	}

	public static class LogEnchancdRecodWriter<K,V> extends RecordWriter<K, V>{
		private FSDataOutputStream enchancedOs=null;
		private FSDataOutputStream tocrawlOs=null;
		
		public LogEnchancdRecodWriter(FSDataOutputStream enchancedOs,FSDataOutputStream tocrawlOs) {
			this.enchancedOs=enchancedOs;
			this.tocrawlOs=tocrawlOs;
		}
		
		@Override
		public void close(TaskAttemptContext arg0) throws IOException, InterruptedException {

			
		}

		@Override
		public void write(K key, V value) throws IOException, InterruptedException {
			if(key.toString().endsWith("tocrawl")){
				tocrawlOs.write(key.toString().getBytes());
			}else{
				enchancedOs.write(key.toString().getBytes());
			}
		}
		
	}
}
