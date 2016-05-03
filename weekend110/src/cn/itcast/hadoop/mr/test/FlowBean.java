package cn.itcast.hadoop.mr.test;

public class FlowBean {
	
	private String phoneNB;
	private long up_flow;
	private long d_flow;
	private long s_flow;
	public String getPhoneNB() {
		return phoneNB;
	}
	public void setPhoneNB(String phoneNB) {
		this.phoneNB = phoneNB;
	}
	public long getUp_flow() {
		return up_flow;
	}
	public void setUp_flow(long up_flow) {
		this.up_flow = up_flow;
	}
	public long getD_flow() {
		return d_flow;
	}
	public void setD_flow(long d_flow) {
		this.d_flow = d_flow;
	}
	public long getS_flow() {
		return s_flow;
	}
	public void setS_flow(long s_flow) {
		this.s_flow = s_flow;
	}
	
	public FlowBean(){}
	
}
