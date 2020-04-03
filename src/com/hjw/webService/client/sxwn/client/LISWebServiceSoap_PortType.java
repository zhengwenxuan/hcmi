/**
 * LISWebServiceSoap_PortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.sxwn.client;

public interface LISWebServiceSoap_PortType extends java.rmi.Remote {
    public java.lang.String LIS_FindTestClassDetail(int testclassnum) throws java.rmi.RemoteException;
    public java.lang.String LIS_FindTestClass() throws java.rmi.RemoteException;
    public java.lang.String LIS_InsLisApplyMiddleDeatil(int iRequestId, java.lang.String no_Apply, int ITestClassID, java.lang.String testClassName) throws java.rmi.RemoteException;
    public java.lang.String LIS_FindReport(java.lang.String suffererType, java.lang.String cCaseCode) throws java.rmi.RemoteException;
    public java.lang.String LIS_InsLisApplyMiddle(java.lang.String cPatTypeFlag, java.lang.String suffererType, java.lang.String cCaseCode, java.lang.String cPatientName, java.lang.String CSex, int iYearsOld, java.lang.String ageUnit, java.lang.String cSampleName, java.util.Calendar dCreaBarTime, java.util.Calendar dPrintBarTime, java.lang.String CDiagnose, java.lang.String CSendDeptName, java.lang.String CDoctor, java.util.Calendar KDSJ, java.lang.String CCollectSampOp, java.util.Calendar collectSampDate, java.lang.String collectSampleDept, int iSampleId, java.lang.String tubeID, java.lang.String iColor) throws java.rmi.RemoteException;
    public java.lang.String getPtMedOrderList(java.lang.String xml) throws java.rmi.RemoteException;
    public java.lang.String medOrderCharge(java.lang.String xml) throws java.rmi.RemoteException;
    public java.lang.String sendCriticalValue(java.lang.String xml) throws java.rmi.RemoteException;
    public java.lang.String getMedCodeSet(java.lang.String xml) throws java.rmi.RemoteException;
    public java.lang.String sendMedStatus(java.lang.String xml) throws java.rmi.RemoteException;
    public java.lang.String insertCheckLISPatient(java.lang.String jsonStr) throws java.rmi.RemoteException;
    public java.lang.String updateAppointNextLISPatient(java.lang.String jsonStr) throws java.rmi.RemoteException;
    public java.lang.String updateAppointLISPatientEnd(java.lang.String jsonStr) throws java.rmi.RemoteException;
    public java.lang.String LIS_UpdateTipinfo(java.lang.String tipCheckOp, java.lang.String tipCheckOpName, java.util.Calendar tipCheckDate, java.lang.String msgID) throws java.rmi.RemoteException;
    public java.lang.String LIS_FindReport2(java.lang.String suffererType, java.lang.String cCaseCode) throws java.rmi.RemoteException;
}
