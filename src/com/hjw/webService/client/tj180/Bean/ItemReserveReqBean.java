package com.hjw.webService.client.tj180.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.Bean.dbgj   
     * @Description: 消息体 
     * @author: yangm     
     * @date:   2016年10月7日 上午11:08:27   
     * @version V2.0.0.0
 */
public class ItemReserveReqBean {
	private String reserveId="";//	体检预约号
	private String itemsNum="0";//	体检项目数
	private List<ItemReserveBean> itemsInfo=new ArrayList<ItemReserveBean>();
	public String getReserveId() {
		return reserveId;
	}
	public void setReserveId(String reserveId) {
		this.reserveId = reserveId;
	}
	public String getItemsNum() {
		return itemsNum;
	}
	public void setItemsNum(String itemsNum) {
		this.itemsNum = itemsNum;
	}
	public List<ItemReserveBean> getItemsInfo() {
		return itemsInfo;
	}
	public void setItemsInfo(List<ItemReserveBean> itemsInfo) {
		this.itemsInfo = itemsInfo;
	}
	
	
}
