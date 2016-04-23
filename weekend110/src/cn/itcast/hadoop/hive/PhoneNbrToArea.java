package cn.itcast.hadoop.hive;

import java.util.HashMap;

import org.apache.hadoop.hive.ql.exec.UDF;


public class PhoneNbrToArea extends UDF{

	private static HashMap<String, String> areaMap = new HashMap<>();
	static {
		areaMap.put("1344", "beijing");
		areaMap.put("1357", "tianjin");
		areaMap.put("1379", "nanjing");
		areaMap.put("1877","qingdao");
		areaMap.put("1875", "jinan");
	}
	
	//一定要用public修饰才能被hive调用
	public String evaluate(String pnb) {
		
		String result  = areaMap.get(pnb.substring(0,4))==null? (pnb+"    huoxing"):(pnb+"  "+areaMap.get(pnb.substring(0,4)));		
		
		return result;
	}
	
}
