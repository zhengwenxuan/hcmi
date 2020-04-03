/**
 * OtherSysServiceSoap_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.erfuyuan.gencode;

public interface OtherSysServiceSoap_PortType extends java.rmi.Remote {
    public java.lang.String helloWorld() throws java.rmi.RemoteException;
    public java.lang.String f_getViersion() throws java.rmi.RemoteException;
    public java.lang.String sendExamApp_test() throws java.rmi.RemoteException;
    public java.lang.String cancelFeeApp(java.lang.String pHisIID) throws java.rmi.RemoteException;
    public java.lang.String sendExamApp(java.lang.String pXmlValue) throws java.rmi.RemoteException;
    public java.lang.String regInfo(java.lang.String pIID) throws java.rmi.RemoteException;
    public java.lang.String checkComplete(java.lang.String pIID) throws java.rmi.RemoteException;
    public java.lang.String returnReports(java.lang.String pIID) throws java.rmi.RemoteException;
    public java.lang.String cancelReport(java.lang.String pIID) throws java.rmi.RemoteException;
    public java.lang.String sendExamAppDtHealth(java.lang.String pXmlValue) throws java.rmi.RemoteException;
}
