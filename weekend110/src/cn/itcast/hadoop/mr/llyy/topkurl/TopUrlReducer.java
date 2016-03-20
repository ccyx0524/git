package cn.itcast.hadoop.mr.llyy.topkurl;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import cn.itcast.hadoop.mr.flowsum.FlowBean;

public class TopUrlReducer extends Reducer<Text, FlowBean, Text, LongWritable>{
	
	private TreeMap<FlowBean, Text> treeMap=new TreeMap<>();
	
	private double globalCount=0;
	
	@Override
	protected void reduce(Text key, Iterable<FlowBean> values, Context context)
			throws IOException, InterruptedException {
		// TODO Auto-generated method stub
		Text url = new Text(key.toString());
		long up_sum=0;
		long d_sum=0;
		for(FlowBean bean:values){
			up_sum+=bean.getUp_flow(); 
			d_sum+=bean.getD_flow();
		}
		FlowBean bean=new FlowBean("", up_sum, d_sum);
		
		treeMap.put(bean, url);
	}
	
	
	@Override
	protected void cleanup(Context context)
			throws IOException, InterruptedException {
		Set<Entry<FlowBean, Text>> entrySet=treeMap.entrySet();
		double tempCount=0;
		for(Entry<FlowBean, Text> ent:entrySet){
			if(tempCount/globalCount>0.8){
				context.write(ent.getValue(), new LongWritable(ent.getKey().getS_flow()));
				tempCount+=ent.getKey().getS_flow();
			}else{
				return;
			}
		}
		
	}
}
