/**
 * UpdatePersonServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.fangzheng.client;

public class UpdatePersonServiceLocator extends org.apache.axis.client.Service implements com.hjw.webService.client.fangzheng.client.UpdatePersonService {

    public UpdatePersonServiceLocator() {
    }


    public UpdatePersonServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public UpdatePersonServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for UpdatePersonServicePort
    private java.lang.String UpdatePersonServicePort_address = "http://192.168.8.212:8001/empi-ws/doc/updatePerson";

    public java.lang.String getUpdatePersonServicePortAddress() {
        return UpdatePersonServicePort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String UpdatePersonServicePortWSDDServiceName = "UpdatePersonServicePort";

    public java.lang.String getUpdatePersonServicePortWSDDServiceName() {
        return UpdatePersonServicePortWSDDServiceName;
    }

    public void setUpdatePersonServicePortWSDDServiceName(java.lang.String name) {
        UpdatePersonServicePortWSDDServiceName = name;
    }

    public com.hjw.webService.client.fangzheng.client.IUpdatePersonService getUpdatePersonServicePort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(UpdatePersonServicePort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getUpdatePersonServicePort(endpoint);
    }

    public com.hjw.webService.client.fangzheng.client.IUpdatePersonService getUpdatePersonServicePort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.hjw.webService.client.fangzheng.client.UpdatePersonServiceSoapBindingStub _stub = new com.hjw.webService.client.fangzheng.client.UpdatePersonServiceSoapBindingStub(portAddress, this);
            _stub.setPortName(getUpdatePersonServicePortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setUpdatePersonServicePortEndpointAddress(java.lang.String address) {
        UpdatePersonServicePort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.hjw.webService.client.fangzheng.client.IUpdatePersonService.class.isAssignableFrom(serviceEndpointInterface)) {
                com.hjw.webService.client.fangzheng.client.UpdatePersonServiceSoapBindingStub _stub = new com.hjw.webService.client.fangzheng.client.UpdatePersonServiceSoapBindingStub(new java.net.URL(UpdatePersonServicePort_address), this);
                _stub.setPortName(getUpdatePersonServicePortWSDDServiceName());
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
        if ("UpdatePersonServicePort".equals(inputPortName)) {
            return getUpdatePersonServicePort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://doc.service.cxf.founder.com/", "UpdatePersonService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://doc.service.cxf.founder.com/", "UpdatePersonServicePort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("UpdatePersonServicePort".equals(portName)) {
            setUpdatePersonServicePortEndpointAddress(address);
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
