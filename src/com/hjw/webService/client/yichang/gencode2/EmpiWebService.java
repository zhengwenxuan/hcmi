/**
 * EmpiWebService.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.yichang.gencode2;

public interface EmpiWebService extends java.rmi.Remote {
    public java.lang.String delPatientInfo(java.lang.String patientInfo) throws java.rmi.RemoteException;
    public java.lang.String updatePatientInfo(java.lang.String patientInfo) throws java.rmi.RemoteException;
    public java.lang.String regV3Empi(java.lang.String patientInfo) throws java.rmi.RemoteException;
    public java.lang.String getPatientInfoByLocalID(java.lang.String localID) throws java.rmi.RemoteException;
    public java.lang.String getPatientInfoByEmpi(java.lang.String empi) throws java.rmi.RemoteException;
    public java.lang.String getPatientInfoByLocalIndex(java.lang.String localindex) throws java.rmi.RemoteException;
    public java.lang.String getIndexByLocalIndex(java.lang.String localindex) throws java.rmi.RemoteException;
    public com.hjw.webService.client.yichang.gencode2.ReplyBean getSimilarPatient(java.lang.String patientInfo) throws java.rmi.RemoteException;
    public java.lang.String cardInfoUnBind(java.lang.String unbindxml) throws java.rmi.RemoteException;
    public java.lang.String regEmpi(java.lang.String patientInfo) throws java.rmi.RemoteException;
    public java.lang.String getLocalIdByEmpi(java.lang.String empi) throws java.rmi.RemoteException;
}
