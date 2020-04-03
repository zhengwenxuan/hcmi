package com.hjw.webService.client.qufu.job.xdbean;

import java.util.ArrayList;
import java.util.List;
 
public class RetXdItemQF {

	private String chargingItem_num="";//项目编码

	private String chargingItem_ms="";//收费项目描述
	
	private String chargingItem_jl="";//收费项目结论
	
    private String sh_time="";//审核时间   //未知
    private String jc_time="";//检查时间  //未知
    
    private String base64_bg="";//报告  64编码以后的字符串
    private String imagetype="";//体检报告类型
    
    public String getImagetype() {
		return imagetype;
	}

	public void setImagetype(String imagetype) {
		this.imagetype = imagetype;
	}

	private List<RetXdPicQF> listRetPacsPic =new ArrayList<RetXdPicQF>();

	public String getChargingItem_num() {
		return chargingItem_num;
	}

	public void setChargingItem_num(String chargingItem_num) {
		this.chargingItem_num = chargingItem_num;
	}

	public String getChargingItem_ms() {
		return chargingItem_ms;
	}

	public void setChargingItem_ms(String chargingItem_ms) {
		this.chargingItem_ms = chargingItem_ms;
	}

	public String getChargingItem_jl() {
		return chargingItem_jl;
	}

	public void setChargingItem_jl(String chargingItem_jl) {
		this.chargingItem_jl = chargingItem_jl;
	}

	public String getSh_time() {
		return sh_time;
	}

	public void setSh_time(String sh_time) {
		this.sh_time = sh_time;
	}

	public String getJc_time() {
		return jc_time;
	}

	public void setJc_time(String jc_time) {
		this.jc_time = jc_time;
	}

	public String getBase64_bg() {
		return base64_bg;
	}

	public void setBase64_bg(String base64_bg) {
		this.base64_bg = base64_bg;
	}

	public List<RetXdPicQF> getListRetPacsPic() {
		return listRetPacsPic;
	}

	public void setListRetPacsPic(List<RetXdPicQF> listRetPacsPic) {
		this.listRetPacsPic = listRetPacsPic;
	}

}
