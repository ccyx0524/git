package cn.itcast.hadoop.hdfs;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class HdfsUtilHA {

	
	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
//		conf.set(name, value);
		FileSystem fs = FileSystem.get(new URI("hdfs://ns1/"), conf, "hadoop");
		fs.copyFromLocalFile(new Path("c:/test.txt"), new Path("hdfs://ns1/"));
	}
	
}
