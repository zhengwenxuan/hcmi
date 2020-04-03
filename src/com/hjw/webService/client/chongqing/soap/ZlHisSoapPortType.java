/**
 * ZlHisSoapPortType.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.chongqing.soap;

public interface ZlHisSoapPortType extends java.rmi.Remote {

    /**
     * HL7明文交易接口
     */
    public java.lang.String zlWS_HL7(java.lang.String inputXml) throws java.rmi.RemoteException, ZlWS_HL7Fault_Element;

    /**
     * 测试接口 提供给外部调用
     */
    public java.lang.String netTest(java.lang.String xml) throws java.rmi.RemoteException, NetTestFault_Element;

    /**
     * 测试加密,提供给外部调用返回加密后数据
     */
    public java.lang.String encryptTest(java.lang.String xml) throws java.rmi.RemoteException, EncryptTestFault_Element;
}
