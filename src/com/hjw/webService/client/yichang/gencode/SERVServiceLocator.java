/**
 * SERVServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.yichang.gencode;

public class SERVServiceLocator extends org.apache.axis.client.Service implements com.hjw.webService.client.yichang.gencode.SERVService {

    public SERVServiceLocator(String url) {
    	this.SERVPort_address = url;
    }


    public SERVServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public SERVServiceLocator(String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for SERVPort
    private String SERVPort_address = "http://10.27.254.33:9002/HIP/webService/hl7/HL7Delegate1_1";

    public String getSERVPortAddress() {
        return SERVPort_address;
    }

    // The WSDD service name defaults to the port name.
    private String SERVPortWSDDServiceName = "SERVPort";

    public String getSERVPortWSDDServiceName() {
        return SERVPortWSDDServiceName;
    }

    public void setSERVPortWSDDServiceName(String name) {
        SERVPortWSDDServiceName = name;
    }

    public com.hjw.webService.client.yichang.gencode.SERVDelegate getSERVPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(SERVPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getSERVPort(endpoint);
    }

    public com.hjw.webService.client.yichang.gencode.SERVDelegate getSERVPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.hjw.webService.client.yichang.gencode.SERVServiceSoapBindingStub _stub = new com.hjw.webService.client.yichang.gencode.SERVServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getSERVPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setSERVPortEndpointAddress(String address) {
        SERVPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.hjw.webService.client.yichang.gencode.SERVDelegate.class.isAssignableFrom(serviceEndpointInterface)) {
                com.hjw.webService.client.yichang.gencode.SERVServiceSoapBindingStub _stub = new com.hjw.webService.client.yichang.gencode.SERVServiceSoapBindingStub(new java.net.URL(SERVPort_address), this);
                _stub.setPortName(getSERVPortWSDDServiceName());
                return _stub;
            }
        }
        catch (Throwable t) {
            throw new javax.xml.rpc.ServiceException(t);
        }
        throw new javax.xml.rpc.ServiceException("There is no stub implementation for the interface:  " + (serviceEndpointInterface == null ? "null" : serviceEndpointInterface.getName()));
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(javax.xml.namespace.QName portName, Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        if (portName == null) {
            return getPort(serviceEndpointInterface);
        }
        String inputPortName = portName.getLocalPart();
        if ("SERVPort".equals(inputPortName)) {
            return getSERVPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("urn:hl7-org:v3", "SERVService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("urn:hl7-org:v3", "SERVPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(String portName, String address) throws javax.xml.rpc.ServiceException {
        
if ("SERVPort".equals(portName)) {
            setSERVPortEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
