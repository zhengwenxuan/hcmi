package com.hjw.webService.client.huojianwa.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "Item")  
@XmlType(propOrder = {})
public class Item_Apply {

	@XmlElement
	private int SeqNo;//序号
	@XmlElement
	private String ChargingId = "";//收费项目代码
	@XmlElement
	private String ItemName = "";//收费项目名称
	@XmlElement
	private String Price = "";//项目单价
	@XmlElement
	private int ItemCount = 1;//数量
	@XmlElement
	private String PacsItemCode = "";//PACS 检查项目代码
	
	public int getSeqNo() {
		return SeqNo;
	}
	public String getChargingId() {
		return ChargingId;
	}
	public String getItemName() {
		return ItemName;
	}
	public String getPrice() {
		return Price;
	}
	public int getItemCount() {
		return ItemCount;
	}
	public String getPacsItemCode() {
		return PacsItemCode;
	}
	public void setSeqNo(int seqNo) {
		SeqNo = seqNo;
	}
	public void setChargingId(String chargingId) {
		ChargingId = chargingId;
	}
	public void setItemName(String itemName) {
		ItemName = itemName;
	}
	public void setPrice(String price) {
		Price = price;
	}
	public void setItemCount(int itemCount) {
		ItemCount = itemCount;
	}
	public void setPacsItemCode(String pacsItemCode) {
		PacsItemCode = pacsItemCode;
	}
}
