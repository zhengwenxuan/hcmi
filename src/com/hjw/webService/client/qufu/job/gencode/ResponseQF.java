/**
 * Response.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.qufu.job.gencode;

public class ResponseQF  implements java.io.Serializable {
    private com.hjw.webService.client.qufu.job.gencode.ResponseHeader responseHeader = new com.hjw.webService.client.qufu.job.gencode.ResponseHeader();

    private java.lang.String responseBody;

    public ResponseQF() {
    }

    public ResponseQF(
           com.hjw.webService.client.qufu.job.gencode.ResponseHeader responseHeader,
           java.lang.String responseBody) {
           this.responseHeader = responseHeader;
           this.responseBody = responseBody;
    }


    /**
     * Gets the responseHeader value for this Response.
     * 
     * @return responseHeader
     */
    public com.hjw.webService.client.qufu.job.gencode.ResponseHeader getResponseHeader() {
        return responseHeader;
    }


    /**
     * Sets the responseHeader value for this Response.
     * 
     * @param responseHeader
     */
    public void setResponseHeader(com.hjw.webService.client.qufu.job.gencode.ResponseHeader responseHeader) {
        this.responseHeader = responseHeader;
    }


    /**
     * Gets the responseBody value for this Response.
     * 
     * @return responseBody
     */
    public java.lang.String getResponseBody() {
        return responseBody;
    }


    /**
     * Sets the responseBody value for this Response.
     * 
     * @param responseBody
     */
    public void setResponseBody(java.lang.String responseBody) {
        this.responseBody = responseBody;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ResponseQF)) return false;
        ResponseQF other = (ResponseQF) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.responseHeader==null && other.getResponseHeader()==null) || 
             (this.responseHeader!=null &&
              this.responseHeader.equals(other.getResponseHeader()))) &&
            ((this.responseBody==null && other.getResponseBody()==null) || 
             (this.responseBody!=null &&
              this.responseBody.equals(other.getResponseBody())));
        __equalsCalc = null;
        return _equals;
    }

    private boolean __hashCodeCalc = false;
    public synchronized int hashCode() {
        if (__hashCodeCalc) {
            return 0;
        }
        __hashCodeCalc = true;
        int _hashCode = 1;
        if (getResponseHeader() != null) {
            _hashCode += getResponseHeader().hashCode();
        }
        if (getResponseBody() != null) {
            _hashCode += getResponseBody().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ResponseQF.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://www.shats.com/DataReceive", "Response"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("responseHeader");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.shats.com/DataReceive", "responseHeader"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.shats.com/DataReceive", "ResponseHeader"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("responseBody");
        elemField.setXmlName(new javax.xml.namespace.QName("http://www.shats.com/DataReceive", "responseBody"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
    }

    /**
     * Return type metadata object
     */
    public static org.apache.axis.description.TypeDesc getTypeDesc() {
        return typeDesc;
    }

    /**
     * Get Custom Serializer
     */
    public static org.apache.axis.encoding.Serializer getSerializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanSerializer(
            _javaType, _xmlType, typeDesc);
    }

    /**
     * Get Custom Deserializer
     */
    public static org.apache.axis.encoding.Deserializer getDeserializer(
           java.lang.String mechType, 
           java.lang.Class _javaType,  
           javax.xml.namespace.QName _xmlType) {
        return 
          new  org.apache.axis.encoding.ser.BeanDeserializer(
            _javaType, _xmlType, typeDesc);
    }

}
