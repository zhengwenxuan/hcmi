package com.hjw.webService.client.tj180.Bean;

import java.util.ArrayList;
import java.util.List;

import com.hjw.interfaces.util.StringUtil;
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
public class GroupMessageBeanTJ180{
	private String orgReserveId="";//	团体预约号
	private String organizationId="";//	所属团队编码
	private String organizationName="";//	所属团队名称
	private String editor="";//	编辑人 登录用户名如：WYY
	private String editDate="";//	编辑时间 	Yyyy-mm-dd hh24:mi:ss
	private String stampFlag="0";//是否打印记账印章 	默认：1-打印；0-不打印
	private String priceflag="0";//是否显示价格 默认：0-不显示；1-显示
	private String groupId="";//	分组编码
	private String groupName="";//	分组名称
	private String totalCosts="";//	分组项目总金额
	private String fitSex="";//	适合性别 不限：0，适合男：1，适合女：2
	private String fitWedded="";//	适合婚否
	private String relativeName="";//	单位联系人
	private String relativePhone="";//	单位联系人号码
	private String deptManager="";//	院方科室负责人
	private String orgAddress="";//	单位地址
	private String orgEmail="";//	单位Email
	private String memo="";//单位信息备注

	private List<GroupItemMessageBeanTJ180> groupsInfo= new ArrayList<GroupItemMessageBeanTJ180>();//	unionProjectId	体检项目编码

	public String getRelativeName() {
		return relativeName;
	}
	public void setRelativeName(String relativeName) {
		this.relativeName = relativeName;
	}
	public String getRelativePhone() {
		return relativePhone;
	}
	public void setRelativePhone(String relativePhone) {
		this.relativePhone = relativePhone;
	}
	public String getDeptManager() {
		return deptManager;
	}
	public void setDeptManager(String deptManager) {
		this.deptManager = deptManager;
	}
	public String getOrgAddress() {
		if((orgAddress!=null)&&(orgAddress.trim().length()>0)){
			orgAddress=(StringUtil.subTextString(orgAddress.trim(),98));
		}else if((orgAddress!=null)&&(orgAddress.trim().length()==0)){
			orgAddress=("");
		}			
		return orgAddress;
	}
	public void setOrgAddress(String orgAddress) {
		this.orgAddress = orgAddress;
	}
	public String getOrgEmail() {
		return orgEmail;
	}
	public void setOrgEmail(String orgEmail) {
		this.orgEmail = orgEmail;
	}
	public String getMemo() {
		if((memo!=null)&&(memo.trim().length()>0)){
			memo=(StringUtil.subTextString(memo.trim(),48));
		}else if((memo!=null)&&(memo.trim().length()==0)){
			memo=("");
		}		
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public String getStampFlag() {
		return stampFlag;
	}
	public void setStampFlag(String stampFlag) {
		this.stampFlag = stampFlag;
	}
	public String getPriceflag() {
		return priceflag;
	}
	public void setPriceflag(String priceflag) {
		this.priceflag = priceflag;
	}
	public String getOrgReserveId() {
		return orgReserveId;
	}
	public void setOrgReserveId(String orgReserveId) {
		this.orgReserveId = orgReserveId;
	}
	public String getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
	public String getOrganizationName() {
		return organizationName;
	}
	public void setOrganizationName(String organizationName) {
		this.organizationName = organizationName;
	}
	public String getEditor() {
		return editor;
	}
	public void setEditor(String editor) {
		this.editor = editor;
	}
	public String getEditDate() {
		return editDate;
	}
	public void setEditDate(String editDate) {
		this.editDate = editDate;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getGroupName() {
		return groupName;
	}
	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}
	public String getTotalCosts() {
		return totalCosts;
	}
	public void setTotalCosts(String totalCosts) {
		this.totalCosts = totalCosts;
	}
	public String getFitSex() {
		return fitSex;
	}
	public void setFitSex(String fitSex) {
		this.fitSex = fitSex;
	}
	public String getFitWedded() {
		return fitWedded;
	}
	public void setFitWedded(String fitWedded) {
		this.fitWedded = fitWedded;
	}
	public List<GroupItemMessageBeanTJ180> getGroupsInfo() {
		return groupsInfo;
	}
	public void setGroupsInfo(List<GroupItemMessageBeanTJ180> groupsInfo) {
		this.groupsInfo = groupsInfo;
	}
}
