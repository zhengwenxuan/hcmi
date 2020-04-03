/**
 * CollectionInterfaceSoap_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.tj180.client;

public interface CollectionInterfaceSoap_PortType extends java.rmi.Remote {

    /**
     * Webservice说明
     */
    public java.lang.String a_WebServiceExplain() throws java.rmi.RemoteException;

    /**
     * 登录
     */
    public java.lang.String login(java.lang.String _orgId, java.lang.String _uid, java.lang.String _pwd) throws java.rmi.RemoteException;

    /**
     * 取人员类别
     */
    public java.lang.String getUserGradeList(java.lang.String _organID) throws java.rmi.RemoteException;

    /**
     * 取系统用户
     */
    public java.lang.String getSystemUser(java.lang.String _organID) throws java.rmi.RemoteException;

    /**
     * 取机构项目库
     */
    public java.lang.String getItemLibraryList(java.lang.String _organID) throws java.rmi.RemoteException;

    /**
     * 检查体检编号是否存在 返回结果：1有 2没有 3异常
     */
    public int checkReportNo(java.lang.String _organId, java.lang.String _reportNO) throws java.rmi.RemoteException;

    /**
     * 上传问卷采集数据 1.成功 2.失败 3.该体检编号不存在 4.发生异常
     */
    public java.lang.String upQuestionCollectionData(com.hjw.webService.client.tj180.client.TempQuestion _question) throws java.rmi.RemoteException;

    /**
     * 上传报告采集数据 1成功 2失败 3异常
     */
    public java.lang.String upReportCollectionData(com.hjw.webService.client.tj180.client.TempReportInfo _report) throws java.rmi.RemoteException;

    /**
     * 根据体检编号获得报告的最新一份个报
     */
    public java.lang.String getPersonReportVirtualPath(java.lang.String _reportNO) throws java.rmi.RemoteException;

    /**
     * 获取审核报告的路径
     */
    public java.lang.String getAuditReportPath(java.lang.String _reportNO) throws java.rmi.RemoteException;
}
