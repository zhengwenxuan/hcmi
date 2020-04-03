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
public class PersonDeleteBean {
	private String reserveId="";//	体检预约号
	private String reserveType="O";//	体检预约类型 	P：个人；O：团体
	private String organizationId="";//	所属团队编码
	private String orgReserveId="";//	团体预约号
	private String groupId="";//	分组编码
	
	public String getReserveId() {
		return reserveId;
	}
	public void setReserveId(String reserveId) {
		this.reserveId = reserveId;
	}
	public String getReserveType() {
		return reserveType;
	}
	public void setReserveType(String reserveType) {
		this.reserveType = reserveType;
	}
	public String getOrganizationId() {
		return organizationId;
	}
	public void setOrganizationId(String organizationId) {
		this.organizationId = organizationId;
	}
	public String getOrgReserveId() {
		return orgReserveId;
	}
	public void setOrgReserveId(String orgReserveId) {
		this.orgReserveId = orgReserveId;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	
}
