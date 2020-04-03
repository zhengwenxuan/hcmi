/**
 * EMPI_ServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.yichang.gencode2;

public class EMPI_ServiceLocator extends org.apache.axis.client.Service implements com.hjw.webService.client.yichang.gencode2.EMPI_Service {

    public EMPI_ServiceLocator(String url) {
    	this.EMPI_address = url;
    }


    public EMPI_ServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public EMPI_ServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for EMPI
    private java.lang.String EMPI_address = "http://10.27.1.56:8082/Empi/ws/empi";

    public java.lang.String getEMPIAddress() {
        return EMPI_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String EMPIWSDDServiceName = "EMPI";

    public java.lang.String getEMPIWSDDServiceName() {
        return EMPIWSDDServiceName;
    }

    public void setEMPIWSDDServiceName(java.lang.String name) {
        EMPIWSDDServiceName = name;
    }

    public com.hjw.webService.client.yichang.gencode2.EmpiWebService getEMPI() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(EMPI_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getEMPI(endpoint);
    }

    public com.hjw.webService.client.yichang.gencode2.EmpiWebService getEMPI(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.hjw.webService.client.yichang.gencode2.EMPISoapBindingStub _stub = new com.hjw.webService.client.yichang.gencode2.EMPISoapBindingStub(portAddress, this);
            _stub.setPortName(getEMPIWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setEMPIEndpointAddress(java.lang.String address) {
        EMPI_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.hjw.webService.client.yichang.gencode2.EmpiWebService.class.isAssignableFrom(serviceEndpointInterface)) {
                com.hjw.webService.client.yichang.gencode2.EMPISoapBindingStub _stub = new com.hjw.webService.client.yichang.gencode2.EMPISoapBindingStub(new java.net.URL(EMPI_address), this);
                _stub.setPortName(getEMPIWSDDServiceName());
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
        if ("EMPI".equals(inputPortName)) {
            return getEMPI();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://cdr.empi.neusoft.com/", "EMPI");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://cdr.empi.neusoft.com/", "EMPI"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("EMPI".equals(portName)) {
            setEMPIEndpointAddress(address);
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
