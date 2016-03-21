package cn.itcast.hadoop.mr.habase;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.client.Mutation;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.mapreduce.TableReducer;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapreduce.Job;

public class HabaseMR {
	public static void main(String[] args) throws IOException {
		Configuration conf = HBaseConfiguration.create();
		Job job = new Job(conf, "maperduce on habase");
		job.setJarByClass(HabaseMR.class);
		///
	}

	public class Mapper extends TableMapper<Text, Text> {
		@Override
		protected void map(ImmutableBytesWritable key, Result value, Context context)
				throws IOException, InterruptedException {

			Text k = new Text(Bytes.toString(key.get()));
			Text v = new Text(Bytes.toString(value.getValue(Bytes.toBytes("basicInfo"), Bytes.toBytes("age"))));

			context.write(v, k);
		}
	}

	public class Reducer extends TableReducer<Text, Text, ImmutableBytesWritable> {
		@Override
		protected void reduce(Text key, Iterable<Text> values, Context context)
				throws IOException, InterruptedException {
			Put put = new Put(Bytes.toBytes(key.toString()));
			for (Text value : values) {
				put.add(Bytes.toBytes("f1"), Bytes.toBytes(value.toString()), Bytes.toBytes(value.toString()));
			}

			context.write(null, put);
		}
	}
}
