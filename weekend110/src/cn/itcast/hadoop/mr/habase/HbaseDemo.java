package cn.itcast.hadoop.mr.habase;

import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.KeyValue;

import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HTable;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;

public class HbaseDemo {

	private Configuration conf=null;
	
	public void init() {
		// TODO Auto-generated method stub
		conf=HBaseConfiguration.create();
		conf.set("hbase.zookerper.quorum", "weekend110:2181");
	}
	
	@Test
	public void testDrop() throws Exception{
		HBaseAdmin admin=new HBaseAdmin(conf);
		admin.disableTable("account");
		admin.deleteTable("account");
		admin.close();
	}
	
	@Test
	public void testPut() throws Exception{
		HTable table=new HTable(conf,"person_info");
		Put p=new Put(Bytes.toBytes("person_rk_joe_0001"));
		p.add("base_info".getBytes(), "name".getBytes(), "zhang".getBytes());
		table.put(p);
		table.close();
	}
	
	@Test
	public void testGet() throws Exception{
		HTable table=new HTable(conf, "person_info");
		Get get=new Get(Bytes.toBytes("person_rk_joe_0001"));
		get.setMaxVersions(5);
		Result result=table.get(get);
		//List<Cell> cells=result.list();
		
		for(KeyValue kv:result.list()){
			String family=new String(kv.getFamily());
			System.out.println(family);
			String qualifier=new String(kv.getQualifier());
			System.out.println(qualifier);
			System.out.println(kv.getValue());
		}
		
		table.close();
	}
	
	public void testScan(){
		
	}
}
