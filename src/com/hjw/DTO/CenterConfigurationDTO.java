package com.hjw.DTO;

public class CenterConfigurationDTO implements java.io.Serializable {
	 private static final long serialVersionUID = -97502163798576023L;

	 private String center_name;
	 
	 private String config_key;
	 
	 private String config_value;
	 
	 private String is_active;
	 
	 private String common;

	public String getCenter_name() {
		return center_name;
	}

	public void setCenter_name(String center_name) {
		this.center_name = center_name;
	}

	public String getConfig_key() {
		return config_key;
	}

	public void setConfig_key(String config_key) {
		this.config_key = config_key;
	}

	public String getConfig_value() {
		return config_value;
	}

	public void setConfig_value(String config_value) {
		this.config_value = config_value;
	}

	public String getIs_active() {
		return is_active;
	}

	public void setIs_active(String is_active) {
		this.is_active = is_active;
	}

	public String getCommon() {
		return common;
	}

	public void setCommon(String common) {
		this.common = common;
	}

	   	
	}