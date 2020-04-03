/**
 * WebServiceEntryServiceLocator.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.RADinfo.client;

public class WebServiceEntryServiceLocator extends org.apache.axis.client.Service implements com.hjw.webService.client.RADinfo.client.WebServiceEntryService {

    public WebServiceEntryServiceLocator(String url) {
    	this.WebServiceEntryPort_address = url;
    }


    public WebServiceEntryServiceLocator(org.apache.axis.EngineConfiguration config) {
        super(config);
    }

    public WebServiceEntryServiceLocator(java.lang.String wsdlLoc, javax.xml.namespace.QName sName) throws javax.xml.rpc.ServiceException {
        super(wsdlLoc, sName);
    }

    // Use to get a proxy class for WebServiceEntryPort
    private java.lang.String WebServiceEntryPort_address = "http://192.36.5.9:8888/WebServiceEntry";

    public java.lang.String getWebServiceEntryPortAddress() {
        return WebServiceEntryPort_address;
    }

    // The WSDD service name defaults to the port name.
    private java.lang.String WebServiceEntryPortWSDDServiceName = "WebServiceEntryPort";

    public java.lang.String getWebServiceEntryPortWSDDServiceName() {
        return WebServiceEntryPortWSDDServiceName;
    }

    public void setWebServiceEntryPortWSDDServiceName(java.lang.String name) {
        WebServiceEntryPortWSDDServiceName = name;
    }

    public com.hjw.webService.client.RADinfo.client.WebServiceEntry getWebServiceEntryPort() throws javax.xml.rpc.ServiceException {
       java.net.URL endpoint;
        try {
            endpoint = new java.net.URL(WebServiceEntryPort_address);
        }
        catch (java.net.MalformedURLException e) {
            throw new javax.xml.rpc.ServiceException(e);
        }
        return getWebServiceEntryPort(endpoint);
    }

    public com.hjw.webService.client.RADinfo.client.WebServiceEntry getWebServiceEntryPort(java.net.URL portAddress) throws javax.xml.rpc.ServiceException {
        try {
            com.hjw.webService.client.RADinfo.client.WebServiceEntryPortBindingStub _stub = new com.hjw.webService.client.RADinfo.client.WebServiceEntryPortBindingStub(portAddress, this);
            _stub.setPortName(getWebServiceEntryPortWSDDServiceName());
            return _stub;
        }
        catch (org.apache.axis.AxisFault e) {
            return null;
        }
    }

    public void setWebServiceEntryPortEndpointAddress(java.lang.String address) {
        WebServiceEntryPort_address = address;
    }

    /**
     * For the given interface, get the stub implementation.
     * If this service has no port for the given interface,
     * then ServiceException is thrown.
     */
    public java.rmi.Remote getPort(Class serviceEndpointInterface) throws javax.xml.rpc.ServiceException {
        try {
            if (com.hjw.webService.client.RADinfo.client.WebServiceEntry.class.isAssignableFrom(serviceEndpointInterface)) {
                com.hjw.webService.client.RADinfo.client.WebServiceEntryPortBindingStub _stub = new com.hjw.webService.client.RADinfo.client.WebServiceEntryPortBindingStub(new java.net.URL(WebServiceEntryPort_address), this);
                _stub.setPortName(getWebServiceEntryPortWSDDServiceName());
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
        if ("WebServiceEntryPort".equals(inputPortName)) {
            return getWebServiceEntryPort();
        }
        else  {
            java.rmi.Remote _stub = getPort(serviceEndpointInterface);
            ((org.apache.axis.client.Stub) _stub).setPortName(portName);
            return _stub;
        }
    }

    public javax.xml.namespace.QName getServiceName() {
        return new javax.xml.namespace.QName("http://ws.adapter.bsoft.com/", "WebServiceEntryService");
    }

    private java.util.HashSet ports = null;

    public java.util.Iterator getPorts() {
        if (ports == null) {
            ports = new java.util.HashSet();
            ports.add(new javax.xml.namespace.QName("http://ws.adapter.bsoft.com/", "WebServiceEntryPort"));
        }
        return ports.iterator();
    }

    /**
    * Set the endpoint address for the specified port name.
    */
    public void setEndpointAddress(java.lang.String portName, java.lang.String address) throws javax.xml.rpc.ServiceException {
        
if ("WebServiceEntryPort".equals(portName)) {
            setWebServiceEntryPortEndpointAddress(address);
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
