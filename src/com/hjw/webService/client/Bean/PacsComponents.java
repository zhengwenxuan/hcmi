package com.hjw.webService.client.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @Title: 火箭蛙体检管理系统
 * @Package com.hjw.webService.Bean.dbgj
 * @Description: 申请单
 * @author: yangm
 * @date: 2016年10月7日 上午11:39:54
 * @version V2.0.0.0
 */
public class PacsComponents {
	private String req_no="";//申请单号
    private String datetime="";//申请单日期
    private double costs;//申请单号对应的项目总金额
	private List<PacsComponent> pacsComponent = new ArrayList<PacsComponent>();	
	private String yzName = "检查类";// 医嘱类型
	private String yzCode = "2";// 医嘱类型代码		
	
	public double getCosts() {
		return costs;
	}
	public void setCosts(double costs) {
		this.costs = costs;
	}
	public String getYzName() {
		return yzName;
	}
	public void setYzName(String yzName) {
		this.yzName = yzName;
	}
	public String getYzCode() {
		return yzCode;
	}
	public void setYzCode(String yzCode) {
		this.yzCode = yzCode;
	}
	public String getReq_no() {
		return req_no;
	}
	public void setReq_no(String req_no) {
		this.req_no = req_no;
	}
	public String getDatetime() {
		return datetime;
	}
	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}
	public List<PacsComponent> getPacsComponent() {
		return pacsComponent;
	}
	public void setPacsComponent(List<PacsComponent> pacsComponent) {
		this.pacsComponent = pacsComponent;
	}
}
