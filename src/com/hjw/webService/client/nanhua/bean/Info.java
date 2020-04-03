package com.hjw.webService.client.nanhua.bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "Info")  
@XmlType(propOrder = {})
public class Info{
	
	@XmlElement
	private String tjno;//体检号
	
	@XmlElement
	private String name;//姓名
	
	@XmlElement
	private String sex;//性别
	
	@XmlElement
	private long age;//年龄
	
	@XmlElement
	private String hisno;//挂号序号
	
	@XmlElement
	private String hisfph;//发票号
	
	@XmlElement
	private String zje;//总金额
	
	@XmlElement
	private int hissfzt;//收费状态：1已收费，0未收费
	
	@XmlElement
	private String tfsqrq;//收费状态：1已收费，0未收费
	
	@XmlElement
	private String MxItemcode;//明细code
	@XmlElement
	private String MxItemdj;//明细单价
	@XmlElement
	private String MxItemdw;//明细单位
	@XmlElement
	private String MxItemId;//明细id
	@XmlElement
	private String MxItemName;//明细项目名
	@XmlElement
	private double Num;//数量
	@XmlElement
	private String ZhItemcode;//组合code
	@XmlElement
	private String ZhItemdj;//组合单价
	@XmlElement
	private String ZhItemdw;//组合单位
	@XmlElement
	private String ZhItemId;//组合id
	@XmlElement
	private String ZhItemName;//组合项目名
	@XmlElement
	private String ItemClass; //费用类别
	

	public String getItemClass() {
		return ItemClass;
	}

	public void setItemClass(String itemClass) {
		ItemClass = itemClass;
	}

	public String getMxItemcode() {
		return MxItemcode;
	}

	public String getZhItemcode() {
		return ZhItemcode;
	}

	public String getTjno() {
		return tjno.trim();
	}

	public void setTjno(String tjno) {
		this.tjno = tjno;
	}

	public String getName() {
		return name.trim();
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSex() {
		return sex.trim();
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public long getAge() {
		return age;
	}

	public void setAge(long age) {
		this.age = age;
	}

	public String getHisno() {
		return hisno.trim();
	}

	public void setHisno(String hisno) {
		this.hisno = hisno;
	}

	public String getHisfph() {
		return hisfph.trim();
	}

	public void setHisfph(String hisfph) {
		this.hisfph = hisfph;
	}

	public String getZje() {
		return zje.trim();
	}

	public void setZje(String zje) {
		this.zje = zje;
	}

	public int getHissfzt() {
		return hissfzt;
	}

	public void setHissfzt(int hissfzt) {
		this.hissfzt = hissfzt;
	}

	public String getTfsqrq() {
		return tfsqrq.trim();
	}

	public void setTfsqrq(String tfsqrq) {
		this.tfsqrq = tfsqrq;
	}

//	public String getMxItemcode() {
//		return MxItemcode;
//	}

	public void setMxItemcode(String mxItemcode) {
		MxItemcode = mxItemcode;
	}

	public String getMxItemdj() {
		return MxItemdj.trim();
	}

	public void setMxItemdj(String mxItemdj) {
		MxItemdj = mxItemdj;
	}

	public String getMxItemdw() {
		return MxItemdw.trim();
	}

	public void setMxItemdw(String mxItemdw) {
		MxItemdw = mxItemdw;
	}

	public String getMxItemId() {
		return MxItemId.trim();
	}

	public void setMxItemId(String mxItemId) {
		MxItemId = mxItemId;
	}

	public String getMxItemName() {
		return MxItemName.trim();
	}

	public void setMxItemName(String mxItemName) {
		MxItemName = mxItemName;
	}

	public double getNum() {
		return Num;
	}

	public void setNum(double num) {
		Num = num;
	}

//	public String getZhItemcode() {
//		return ZhItemcode;
//	}

	public void setZhItemcode(String zhItemcode) {
		ZhItemcode = zhItemcode;
	}

	public String getZhItemdj() {
		return ZhItemdj.trim();
	}

	public void setZhItemdj(String zhItemdj) {
		ZhItemdj = zhItemdj;
	}

	public String getZhItemdw() {
		return ZhItemdw.trim();
	}

	public void setZhItemdw(String zhItemdw) {
		ZhItemdw = zhItemdw;
	}

	public String getZhItemId() {
		return ZhItemId.trim();
	}

	public void setZhItemId(String zhItemId) {
		ZhItemId = zhItemId;
	}

	public String getZhItemName() {
		return ZhItemName.trim();
	}

	public void setZhItemName(String zhItemName) {
		ZhItemName = zhItemName;
	}
}
