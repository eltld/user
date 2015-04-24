package net.ememed.user2.entity;

public class AppConfig {

	private String youjiangyaoqing;
	private String openalipay;
	private String module_ehr;
	private String module_emr;
	
	
	public String getModule_ehr() {
		return module_ehr;
	}
	public void setModule_ehr(String module_ehr) {
		this.module_ehr = module_ehr;
	}
	public String getModule_emr() {
		return module_emr;
	}
	public void setModule_emr(String module_emr) {
		this.module_emr = module_emr;
	}
	public void setyoujiangyaoqing(String Youjiangyaoqing) {
		youjiangyaoqing = Youjiangyaoqing;
	}
	public String getyoujiangyaoqing() {
		return youjiangyaoqing;
	}
	
	public void setOpenalipay(String Openalipay) {
		openalipay = Openalipay;
	}
	public String getOpenalipay() {
		return openalipay;
	}
}
