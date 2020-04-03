/**
 * IHisApplay.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.hzty.client;

public interface IHisApplay extends java.rmi.Remote {
    public java.lang.Integer runService(java.lang.String tradeType, java.lang.String tradeMsg, javax.xml.rpc.holders.StringHolder tradeMsgOut) throws java.rmi.RemoteException;
    public java.lang.Integer runService_Main(java.lang.String tradeType, java.lang.String tradeMsg, java.lang.String sendMode, javax.xml.rpc.holders.StringHolder tradeMsgOut) throws java.rmi.RemoteException;
    public java.lang.Integer runService_Http(java.lang.String tradeType, java.lang.String tradeMsg, java.lang.String sendMode, java.lang.String key, javax.xml.rpc.holders.StringHolder tradeMsgOut) throws java.rmi.RemoteException;
    public java.lang.String funMain(java.lang.String inData) throws java.rmi.RemoteException;
    public java.lang.String resourceMethod(java.lang.String xmlData) throws java.rmi.RemoteException;
    public java.lang.String yypt_service(java.lang.String ywgndm, java.lang.String ywxml) throws java.rmi.RemoteException;
}
