package test;

import java.util.HashMap;
import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.state.StateSpoutOutputCollector;
import backtype.storm.state.SynchronizeOutputCollector;
import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;


import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Tuple;

public class LogStatBolt implements IRichBolt{

	private transient OutputCollector _collector;
	private HashMap<String, Long> srcpay;
	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {

		_collector=collector;
		
		if(srcpay==null){
			srcpay=new HashMap<String,Long>();
		}
	}
	@Override
	public void execute(Tuple input) {

		String pay=input.getStringByField("pay");
		String srcid=input.getStringByField("srcid");
		if(srcpay.containsKey(srcid)){
			srcpay.put(srcid, Long.parseLong(pay)+srcpay.get(srcid));
		}else{
			srcpay.put(srcid, Long.parseLong(pay));
			
		}
		
		System.err.println(srcpay.toString());
	}
	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}
