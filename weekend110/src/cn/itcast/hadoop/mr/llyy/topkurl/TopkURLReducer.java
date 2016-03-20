package cn.itcast.hadoop.mr.llyy.topkurl;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import org.apache.hadoop.hdfs.server.namenode.HostFileManager.EntrySet;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import cn.itcast.hadoop.mr.flowsum.FlowBean;

public class TopkURLReducer extends Reducer<Text, FlowBean, Text, LongWritable>{
	private TreeMap<FlowBean,Text> treeMap = new TreeMap<>();
	private double globalCount = 0;
	
	
	// <url,{bean,bean,bean,.......}>
	@Override
	protected void reduce(Text key, Iterable<FlowBean> values,Context context)
			throws IOException, InterruptedException {
		Text url = new Text(key.toString());
		long up_sum = 0;
		long d_sum = 0;
		for(FlowBean bean : values){
			
			up_sum += bean.getUp_flow();
			d_sum += bean.getD_flow();
		}
		
		FlowBean bean = new FlowBean("", up_sum, d_sum);
		//ÿ���һ��url�������������ۼӵ�ȫ�������������У������еļ�¼������ɺ�globalCount�е�ֵ����ȫ�ֵ������ܺ�
		globalCount += bean.getS_flow();
		treeMap.put(bean,url);

	}
	
	
	//cleanup��������reduer���񼴽��˳�ʱ������һ��
	@Override
	protected void cleanup(Context context)
			throws IOException, InterruptedException {

		Set<Entry<FlowBean, Text>> entrySet = treeMap.entrySet();
		double tempCount = 0;
		
		for(Entry<FlowBean, Text> ent: entrySet){
			
			if(tempCount / globalCount < 0.8){
				
				context.write(ent.getValue(), new LongWritable(ent.getKey().getS_flow()));
				tempCount += ent.getKey().getS_flow();
				
			}else{
				return;
			}
			
			
		}
		
		
		
	}
	
}
