/**
 * GWI_TJJKServicesLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.nanhua.gencode;

public class GWI_TJJKServicesLocator extends org.apache.axis.client.Service implements com.hjw.webService.client.nanhua.gencode.GWI_TJJKServices {

	private GWI_TJJKServicesLocator() {
		
	}
	
    public GWI_TJJKServicesLocator(java.lang.String address) {
        GWI_TJJKServicesSoap_address = address;
    }


    public GWI_TJJKServicesLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public GWI_TJJKServicesLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for GWI_TJJKServicesSoap12
    private java.lang.String GWI_TJJKServicesSoap12_address = "http://192.168.0.225/GWI_TJJKServices/GWI_TJJKServices.asmx";

    public java.lang.String getGWI_TJJKServicesSoap12Address() {
        return GWI_TJJKServicesSoap12_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String GWI_TJJKServicesSoap12WSDDServiceName = "GWI_TJJKServicesSoap12";

    public java.lang.String getGWI_TJJKServicesSoap12WSDDServiceName() {
        return GWI_TJJKServicesSoap12WSDDServiceName;
    }

    public void setGWI_TJJKServicesSoap12WSDDServiceName(java.lang.String name) {
        GWI_TJJKServicesSoap12WSDDServiceName = name;
    }

    public com.hjw.webService.client.nanhua.gencode.GWI_TJJKServicesSoap_PortType getGWI_TJJKServicesSoap12() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(GWI_TJJKServicesSoap12_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getGWI_TJJKServicesSoap12(endpoint);
    }

    public com.hjw.webService.client.nanhua.gencode.GWI_TJJKServicesSoap_PortType getGWI_TJJKServicesSoap12(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.hjw.webService.client.nanhua.gencode.GWI_TJJKServicesSoap12Stub _stub = new com.hjw.webService.client.nanhua.gencode.GWI_TJJKServicesSoap12Stub(portAddress, this);
            _stub.setPortName(getGWI_TJJKServicesSoap12WSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setGWI_TJJKServicesSoap12EndpointAddress(java.lang.String address) {
        GWI_TJJKServicesSoap12_address = address;
    }


    // Use to get a proxy class for GWI_TJJKServicesSoap
    private java.lang.String GWI_TJJKServicesSoap_address = "http://192.168.0.225/GWI_TJJKServices/GWI_TJJKServices.asmx";

    public java.lang.String getGWI_TJJKServicesSoapAddress() {
        return GWI_TJJKServicesSoap_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String GWI_TJJKServicesSoapWSDDServiceName = "GWI_TJJKServicesSoap";

    public java.lang.String getGWI_TJJKServicesSoapWSDDServiceName() {
        return GWI_TJJKServicesSoapWSDDServiceName;
    }

    public void setGWI_TJJKServicesSoapWSDDServiceName(java.lang.String name) {
        GWI_TJJKServicesSoapWSDDServiceName = name;
    }

    public com.hjw.webService.client.nanhua.gencode.GWI_TJJKServicesSoap_PortType getGWI_TJJKServicesSoap() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(GWI_TJJKServicesSoap_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getGWI_TJJKServicesSoap(endpoint);
    }

    public com.hjw.webService.client.nanhua.gencode.GWI_TJJKServicesSoap_PortType getGWI_TJJKServicesSoap(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.hjw.webService.client.nanhua.gencode.GWI_TJJKServicesSoap_BindingStub _stub = new com.hjw.webService.client.nanhua.gencode.GWI_TJJKServicesSoap_BindingStub(portAddress, this);
            _stub.setPortName(getGWI_TJJKServicesSoapWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setGWI_TJJKServicesSoapEndpointAddress(java.lang.String address) {
        GWI_TJJKServicesSoap_address = address;
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
            if (com.hjw.webService.client.nanhua.gencode.GWI_TJJKServicesSoap_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.hjw.webService.client.nanhua.gencode.GWI_TJJKServicesSoap12Stub _stub = new com.hjw.webService.client.nanhua.gencode.GWI_TJJKServicesSoap12Stub(new java.net.URL(GWI_TJJKServicesSoap12_address), this);
                _stub.setPortName(getGWI_TJJKServicesSoap12WSDDServiceName());
                return _stub;
            }
            if (com.hjw.webService.client.nanhua.gencode.GWI_TJJKServicesSoap_PortType.class.isAssignableFrom(serviceEndpointInterface)) {
                com.hjw.webService.client.nanhua.gencode.GWI_TJJKServicesSoap_BindingStub _stub = new com.hjw.webService.client.nanhua.gencode.GWI_TJJKServicesSoap_BindingStub(new java.net.URL(GWI_TJJKServicesSoap_address), this);
                _stub.setPortName(getGWI_TJJKServicesSoapWSDDServiceName());
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
        if ("GWI_TJJKServicesSoap12".equals(inputPortName)) {
            return getGWI_TJJKServicesSoap12();
        }
        else if ("GWI_TJJKServicesSoap".equals(inputPortName)) {
            return getGWI_TJJKServicesSoap();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://www.gwi.com.cn/gwi_tjjkseviceservice/", "GWI_TJJKServices");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://www.gwi.com.cn/gwi_tjjkseviceservice/", "GWI_TJJKServicesSoap12"));
            ports.add(new javax.xml.namespace.QName("http://www.gwi.com.cn/gwi_tjjkseviceservice/", "GWI_TJJKServicesSoap"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("GWI_TJJKServicesSoap12".equals(portName)) {
            setGWI_TJJKServicesSoap12EndpointAddress(address);
        }
        else 
if ("GWI_TJJKServicesSoap".equals(portName)) {
            setGWI_TJJKServicesSoapEndpointAddress(address);
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
