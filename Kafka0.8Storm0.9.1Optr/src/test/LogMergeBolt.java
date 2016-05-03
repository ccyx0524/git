package test;

import java.util.HashMap;
import java.util.Map;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;

public class LogMergeBolt implements IRichBolt{
	
	private transient OutputCollector _collector;
	private HashMap<String, String> srcmap;

	@Override
	public void prepare(Map stormConf, TopologyContext context, OutputCollector collector) {

		_collector=collector;
		
		if(srcmap==null){
			srcmap=new HashMap<String,String>();
		}
	}

	@Override
	public void execute(Tuple input) {

		String streamID=input.getSourceStreamId();
		if(streamID.equals("visit")){
			String user=input.getStringByField("user");
			String srcid=input.getStringByField("srcid");
			srcmap.put(user, srcid);
		}else if(streamID.equals("business")){
			String user=input.getStringByField("user");
			String pay=input.getStringByField("pay");
			String srcid=srcmap.get(user);
			
			if(srcid!=null){
				_collector.emit(new Values(user,pay,srcid));
				srcmap.remove(user);
			}
			else{
				
			}
			
		}
		System.err.println(srcmap.toString()+"pay");
	}

	@Override
	public void cleanup() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		// TODO Auto-generated method stub
		declarer.declare(new Fields("user","srcid","pay"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
