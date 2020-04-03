package com.hjw.webService.client.Bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.Bean.dbgj   
     * @Description: s
     * @author: yangm     
     * @date:   2016年10月7日 上午11:50:51   
     * @version V2.0.0.0
 */
public class LisComponents {
	private List<LisComponent> itemList = new ArrayList<LisComponent>();
    private String req_no;//申请单号
    private String lis_id;//id
    private double costs;//费用
    private String datetime;//申请单日期
	private String yzName = "检验类";// 医嘱类型
	private String yzCode = "1";// 医嘱类型代码
	
	private String csampleName="";//标本类型  //山西人民 lis 添加 
		

	public String getLis_id() {
		return lis_id;
	}

	public void setLis_id(String lis_id) {
		this.lis_id = lis_id;
	}

	public String getCsampleName() {
		return csampleName;
	}

	public void setCsampleName(String csampleName) {
		this.csampleName = csampleName;
	}

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

	public String getDatetime() {
		return datetime;
	}

	public void setDatetime(String datetime) {
		this.datetime = datetime;
	}	

	public String getReq_no() {
		return req_no;
	}

	public void setReq_no(String req_no) {
		this.req_no = req_no;
	}

	public List<LisComponent> getItemList() {
		return itemList;
	}

	public void setItemList(List<LisComponent> itemList) {
		this.itemList = itemList;
	}

}
