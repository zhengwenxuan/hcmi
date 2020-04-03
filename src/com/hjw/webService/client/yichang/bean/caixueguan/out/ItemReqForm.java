package com.hjw.webService.client.yichang.bean.caixueguan.out;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.interfaces.util.JaxbUtil;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "job")  
@XmlType(propOrder = {})
public class ItemReqForm {

	@XmlElement
	private String tubeconvert = "";//试管颜色
	@XmlElement
	private String itemname = "";//项目所属组别
	@XmlElement
	private String notelabel = "";//采血量
	@XmlElement
	private String mnemotests = "";//项目名称
	@XmlElement
	private String itemtime = "";//项目申请时间
	@XmlElement
	private String ward = "";//科室名称
	@XmlElement
	private String barcodelabel = "";//条码号
	@XmlElement
	private String destdesc = "";//采血管高度 1代表100mm
	@XmlElement
	private String itemtype = "";//报告类型
	@XmlElement
	private String reporttime = "";//报告时间
	@XmlElement
	private String remark = "";//备注信息
	
	public static void main(String[] args) throws Exception {
		ItemReqForm jobUserOut = new ItemReqForm();
		jobUserOut.setTubeconvert("黄");
		String xml = JaxbUtil.convertToXml(jobUserOut, true);
		System.out.println(xml);
	}
	
	public String getTubeconvert() {
		return tubeconvert;
	}
	public String getItemname() {
		return itemname;
	}
	public String getNotelabel() {
		return notelabel;
	}
	public String getMnemotests() {
		return mnemotests;
	}
	public String getItemtime() {
		return itemtime;
	}
	public String getWard() {
		return ward;
	}
	public String getBarcodelabel() {
		return barcodelabel;
	}
	public String getDestdesc() {
		return destdesc;
	}
	public String getItemtype() {
		return itemtype;
	}
	public String getReporttime() {
		return reporttime;
	}
	public String getRemark() {
		return remark;
	}
	public void setTubeconvert(String tubeconvert) {
		this.tubeconvert = tubeconvert;
	}
	public void setItemname(String itemname) {
		this.itemname = itemname;
	}
	public void setNotelabel(String notelabel) {
		this.notelabel = notelabel;
	}
	public void setMnemotests(String mnemotests) {
		this.mnemotests = mnemotests;
	}
	public void setItemtime(String itemtime) {
		this.itemtime = itemtime;
	}
	public void setWard(String ward) {
		this.ward = ward;
	}
	public void setBarcodelabel(String barcodelabel) {
		this.barcodelabel = barcodelabel;
	}
	public void setDestdesc(String destdesc) {
		this.destdesc = destdesc;
	}
	public void setItemtype(String itemtype) {
		this.itemtype = itemtype;
	}
	public void setReporttime(String reporttime) {
		this.reporttime = reporttime;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
}
