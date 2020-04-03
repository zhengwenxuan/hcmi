/**
 * DataWebServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.qufu.job.gencode;

public class DataWebServiceLocator extends org.apache.axis.client.Service implements com.hjw.webService.client.qufu.job.gencode.DataWebService {

	private DataWebServiceLocator() {
    }

    public DataWebServiceLocator(String url) {
    	this.DataWebServiceSoap_address = url;
    }

    public DataWebServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public DataWebServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for DataWebServiceSoap
    private java.lang.String DataWebServiceSoap_address = "http://localhost:4463/DataWebService.asmx";

    public java.lang.String getDataWebServiceSoapAddress() {
        return DataWebServiceSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String DataWebServiceSoapWSDDServiceName = "DataWebServiceSoap";

    public java.lang.String getDataWebServiceSoapWSDDServiceName() {
        return DataWebServiceSoapWSDDServiceName;
    }

    public void setDataWebServiceSoapWSDDServiceName(java.lang.String name) {
        DataWebServiceSoapWSDDServiceName = name;
    }

    public com.hjw.webService.client.qufu.job.gencode.DataWebServiceSoap_PortType getDataWebServiceSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(DataWebServiceSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getDataWebServiceSoap(endpoint);
    }

    public com.hjw.webService.client.qufu.job.gencode.DataWebServiceSoap_PortType getDataWebServiceSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.hjw.webService.client.qufu.job.gencode.DataWebServiceSoap_BindingStub _stub = new com.hjw.webService.client.qufu.job.gencode.DataWebServiceSoap_BindingStub(portAddress, this);
            _stub.setPortName(getDataWebServiceSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setDataWebServiceSoapEndpointAddress(java.lang.String address) {
        DataWebServiceSoap_address = address;
    }


    // Use to get a proxy class for DataWebServiceSoap12
    private java.lang.String DataWebServiceSoap12_address = "http://localhost:4463/DataWebService.asmx";

    public java.lang.String getDataWebServiceSoap12Address() {
        return DataWebServiceSoap12_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String DataWebServiceSoap12WSDDServiceName = "DataWebServiceSoap12";

    public java.lang.String getDataWebServiceSoap12WSDDServiceName() {
        return DataWebServiceSoap12WSDDServiceName;
    }

    public void setDataWebServiceSoap12WSDDServiceName(java.lang.String name) {
        DataWebServiceSoap12WSDDServiceName = name;
    }

    public com.hjw.webService.client.qufu.job.gencode.DataWebServiceSoap_PortType getDataWebServiceSoap12() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(DataWebServiceSoap12_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getDataWebServiceSoap12(endpoint);
    }

    public com.hjw.webService.client.qufu.job.gencode.DataWebServiceSoap_PortType getDataWebServiceSoap12(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.hjw.webService.client.qufu.job.gencode.DataWebServiceSoap12Stub _stub = new com.hjw.webService.client.qufu.job.gencode.DataWebServiceSoap12Stub(portAddress, this);
            _stub.setPortName(getDataWebServiceSoap12WSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setDataWebServiceSoap12EndpointAddress(java.lang.String address) {
        DataWebServiceSoap12_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     * This service has multiple ports for a given interface;
     * the proxy implementation returned may be indeterminate.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.hjw.webService.client.qufu.job.gencode.DataWebServiceSoap_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.hjw.webService.client.qufu.job.gencode.DataWebServiceSoap_BindingStub _stub = new com.hjw.webService.client.qufu.job.gencode.DataWebServiceSoap_BindingStub(new java.net.URL(DataWebServiceSoap_address), this);
                _stub.setPortName(getDataWebServiceSoapWSDDServiceName());
                return _stub;
            }
            if (com.hjw.webService.client.qufu.job.gencode.DataWebServiceSoap_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.hjw.webService.client.qufu.job.gencode.DataWebServiceSoap12Stub _stub = new com.hjw.webService.client.qufu.job.gencode.DataWebServiceSoap12Stub(new java.net.URL(DataWebServiceSoap12_address), this);
                _stub.setPortName(getDataWebServiceSoap12WSDDServiceName());
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
        if ("DataWebServiceSoap".equals(inputPortName)) {
            return getDataWebServiceSoap();
        }
        else if ("DataWebServiceSoap12".equals(inputPortName)) {
            return getDataWebServiceSoap12();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.shats.com/DataReceive", "DataWebService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.shats.com/DataReceive", "DataWebServiceSoap"));
            ports.add(new javax.xml.namespace.QName("http://www.shats.com/DataReceive", "DataWebServiceSoap12"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("DataWebServiceSoap".equals(portName)) {
            setDataWebServiceSoapEndpointAddress(address);
        }
        else 
if ("DataWebServiceSoap12".equals(portName)) {
            setDataWebServiceSoap12EndpointAddress(address);
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
