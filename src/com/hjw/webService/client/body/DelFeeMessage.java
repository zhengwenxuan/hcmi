package com.hjw.webService.client.body;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.wst.DTO.ExaminfoChargingItemDTO;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.dbgj.body   
     * @Description:  2.19	项目减项
     * @author: yangm     
     * @date:   2016年10月8日 下午7:19:48   
     * @version V2.0.0.0
 */
@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "root")  
@XmlType(propOrder = {})  
public class DelFeeMessage {
	@XmlElement
	private String MSG_TYPE="TJ606";//	服务编码		TJ606（固定）
	@XmlElement
	private String REQ_NO;//申请单号
    private String exam_num;//体检编号
	@XmlElement
	private String PATIENT_ID;//病人id
	@XmlElement
	private String VISIT_NO;//就诊号
	@XmlElement
	private String VISIT_DATE;//就诊日期	
	
	private List<ExaminfoChargingItemDTO> itemCodeList = new ArrayList<ExaminfoChargingItemDTO>();
	
	public List<ExaminfoChargingItemDTO> getItemCodeList() {
		return itemCodeList;
	}
	public void setItemCodeList(List<ExaminfoChargingItemDTO> itemCodeList) {
		this.itemCodeList = itemCodeList;
	}
	public String getExam_num() {
		return exam_num;
	}
	public void setExam_num(String exam_num) {
		this.exam_num = exam_num;
	}
	public String getMSG_TYPE() {
		return MSG_TYPE;
	}
	public void setMSG_TYPE(String mSG_TYPE) {
		MSG_TYPE = mSG_TYPE;
	}
	public String getREQ_NO() {
		return REQ_NO;
	}
	public void setREQ_NO(String rEQ_NO) {
		REQ_NO = rEQ_NO;
	}
	public String getPATIENT_ID() {
		return PATIENT_ID;
	}
	public void setPATIENT_ID(String pATIENT_ID) {
		PATIENT_ID = pATIENT_ID;
	}
	public String getVISIT_NO() {
		return VISIT_NO;
	}
	public void setVISIT_NO(String vISIT_NO) {
		VISIT_NO = vISIT_NO;
	}
	public String getVISIT_DATE() {
		return VISIT_DATE;
	}
	public void setVISIT_DATE(String vISIT_DATE) {
		VISIT_DATE = vISIT_DATE;
	}

}
