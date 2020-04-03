package com.hjw.webService.client.tj180.Bean;

import java.util.ArrayList;
import java.util.List;

import com.hjw.webService.client.Bean.Doctor;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.Bean.Person;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.Bean.dbgj   
     * @Description: 消息体 
     * @author: yangm     
     * @date:   2016年10月7日 上午11:08:27   
     * @version V2.0.0.0
 */
public class GroupItemMessageBeanTJ180{

	private String unionProjectId="";//	体检项目编码
	private String price="";//	项目价格 免费项目也要传，价格为0
	public String getUnionProjectId() {
		return unionProjectId;
	}
	public void setUnionProjectId(String unionProjectId) {
		this.unionProjectId = unionProjectId;
	}
	public String getPrice() {
		return price;
	}
	public void setPrice(String price) {
		this.price = price;
	}
}
