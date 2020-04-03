package com.hjw.webService.client.tj180.Bean;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.webService.Bean.dbgj   
     * @Description: 消息体 
     * @author: yangm     
     * @date:   2016年10月7日 上午11:08:27   
     * @version V2.0.0.0
 */
public class ExamAppointMessageBody{

	private String examNo="";//	检查申请流水号
	private String clinSymp="";//	妇科既往史
	private String relevantDiag="";//	宫颈体查结果
	private String physSign="";//	询问结果 	末次月经：和是否绝经：

	public String getExamNo() {
		return examNo;
	}
	public void setExamNo(String examNo) {
		this.examNo = examNo;
	}
	public String getClinSymp() {
		return clinSymp;
	}
	public void setClinSymp(String clinSymp) {
		this.clinSymp = clinSymp;
	}
	public String getRelevantDiag() {
		return relevantDiag;
	}
	public void setRelevantDiag(String relevantDiag) {
		this.relevantDiag = relevantDiag;
	}
	public String getPhysSign() {
		return physSign;
	}
	public void setPhysSign(String physSign) {
		this.physSign = physSign;
	}


}
