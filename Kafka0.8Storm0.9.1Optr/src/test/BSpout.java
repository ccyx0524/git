package test;

import java.util.Map;

import backtype.storm.spout.SpoutOutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.IRichSpout;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Values;

public class BSpout implements IRichSpout{

	private SpoutOutputCollector _collector;
	private String[] _users={"userA","userB","userC","userD","userE"};
	private String[] _pays={"100","200","300","400","500"};
	private int count=5;
	
	@Override
	public void open(Map conf, TopologyContext context, SpoutOutputCollector collector) {

		_collector=collector;
	}

	@Override
	public void close() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void activate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deactivate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void nextTuple() {
		for(int i=0;i<count;i++){
			try{
				Thread.sleep(1000);
				_collector.emit("business", new Values(System.currentTimeMillis(),_users[i],_pays[i]));
				
			}catch(InterruptedException e){
				e.printStackTrace();
			}
		}
	}

	@Override
	public void ack(Object msgId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void fail(Object msgId) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declareStream("business", new Fields("time","user","pay"));
	}

	@Override
	public Map<String, Object> getComponentConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

}
