package com.hjw.webService.client.haijie.lisbean;

public class RetLisItemHJ {

	private String item_id;//检查项目编码

	private String values;//检查项目值
	
	private String value_fw;//参考范围
	private String value_zt;//结果状态，偏高\偏低\阳性
	
	private String values_dw;//值单位
	private String value_jd;//结果精度

	public String getItem_id() {
		return item_id;
	}
	public void setItem_id(String item_id) {
		this.item_id = item_id;
	}
	public String getValues() {
		return values;
	}
	public void setValues(String values) {
		this.values = values;
	}
	public String getValues_dw() {
		return values_dw;
	}
	public void setValues_dw(String values_dw) {
		this.values_dw = values_dw;
	}
	public String getValue_fw() {
		return value_fw;
	}
	public void setValue_fw(String value_fw) {
		this.value_fw = value_fw;
	}
	public String getValue_zt() {
		return value_zt;
	}
	public void setValue_zt(String value_zt) {
		this.value_zt = value_zt;
	}
	public String getValue_jd() {
		return value_jd;
	}
	public void setValue_jd(String value_jd) {
		this.value_jd = value_jd;
	}
	
}
