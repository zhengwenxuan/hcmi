/**
 * HisService_ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.hzzbmz.client;

public class HisService_ServiceLocator extends org.apache.axis.client.Service implements com.hjw.webService.client.hzzbmz.client.HisService_Service {

    public HisService_ServiceLocator(String url) {
    	this.HisService_address=url;
    }


    public HisService_ServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public HisService_ServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for HisService
    private java.lang.String HisService_address = "http://11.11.100.219:8080/hisWebService/HisService";

    public java.lang.String getHisServiceAddress() {
        return HisService_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String HisServiceWSDDServiceName = "HisService";

    public java.lang.String getHisServiceWSDDServiceName() {
        return HisServiceWSDDServiceName;
    }

    public void setHisServiceWSDDServiceName(java.lang.String name) {
        HisServiceWSDDServiceName = name;
    }

    public com.hjw.webService.client.hzzbmz.client.HisServiceDelegate getHisService() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(HisService_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getHisService(endpoint);
    }

    public com.hjw.webService.client.hzzbmz.client.HisServiceDelegate getHisService(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.hjw.webService.client.hzzbmz.client.HisServiceBindingStub _stub = new com.hjw.webService.client.hzzbmz.client.HisServiceBindingStub(portAddress, this);
            _stub.setPortName(getHisServiceWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setHisServiceEndpointAddress(java.lang.String address) {
        HisService_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.hjw.webService.client.hzzbmz.client.HisServiceDelegate.class.isAssignableFrom(serviceEndpointInterface)) {
                com.hjw.webService.client.hzzbmz.client.HisServiceBindingStub _stub = new com.hjw.webService.client.hzzbmz.client.HisServiceBindingStub(new java.net.URL(HisService_address), this);
                _stub.setPortName(getHisServiceWSDDServiceName());
                return _stub;
            }
        }
        catch (java.lang.Throwable t) {
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
        java.lang.String inputPortName = portName.getLocalPart();
        if ("HisService".equals(inputPortName)) {
            return getHisService();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://ws.his.bsoft.com/", "HisService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://ws.his.bsoft.com/", "HisService"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("HisService".equals(portName)) {
            setHisServiceEndpointAddress(address);
        }
        else 
{ // Unknown Port Name
            throw new javax.xml.rpc.ServiceException(" Cannot set Endpoint Address for Unknown Port" + portName);
        }
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(javax.xml.namespace.QName portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        setEndpointAddress(portName.getLocalPart(), address);
    }

}
