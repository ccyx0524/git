package test;

import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.topology.IRichBolt;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;

public class BvTopo {
	
	public static void main(String[] args) {
		TopologyBuilder builder=new TopologyBuilder();
		
		builder.setSpout("log-vspout", new VSpout(),1);
		builder.setSpout("log-bspout", new BSpout(), 1);
		
		builder.setBolt("log-merge",new LogMergeBolt(), 2)
		.fieldsGrouping("log-vspout", "visit",new Fields("user"))
		.fieldsGrouping("log-bspout", "business", new Fields("user"));
		
		builder.setBolt("log-stat", new LogStatBolt(), 2)
		.fieldsGrouping("log-merge", new Fields("srcid"));
		
		Config conf=new Config();
		conf.setNumAckers(0);
		
		conf.setNumWorkers(7);
		if (args.length > 0) {
			try {
				StormSubmitter.submitTopology(args[0], conf, builder.createTopology());
			} catch (AlreadyAliveException e) {
				e.printStackTrace();
			} catch (InvalidTopologyException e) {
				e.printStackTrace();
			}
		}else {
			LocalCluster localCluster = new LocalCluster();
			localCluster.submitTopology("mytopology", conf, builder.createTopology());
		}
	}

}
