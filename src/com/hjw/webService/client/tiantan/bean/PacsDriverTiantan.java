package com.hjw.webService.client.tiantan.bean;

public class PacsDriverTiantan {
	private boolean flags=false;  //0 表示成功
	private String drivertype_id="";//设备类型编码
	private String drivertype_name="";//设备类型名称
	private String device_id=""; //设备编码  分诊的科室
	
	
	
	public String getDevice_id() {
		return device_id;
	}
	public void setDevice_id(String device_id) {
		this.device_id = device_id;
	}
	public boolean isFlags() {
		return flags;
	}
	public void setFlags(boolean flags) {
		this.flags = flags;
	}
	public String getDrivertype_id() {
		return drivertype_id;
	}
	public void setDrivertype_id(String drivertype_id) {
		this.drivertype_id = drivertype_id;
	}
	public String getDrivertype_name() {
		return drivertype_name;
	}
	public void setDrivertype_name(String drivertype_name) {
		this.drivertype_name = drivertype_name;
	}
	
	
}
