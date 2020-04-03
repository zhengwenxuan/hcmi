/**
 * TempReportAbnormal.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.tj180.client;

public class TempReportAbnormal  implements java.io.Serializable {
    private java.lang.String reportNo;

    private java.lang.String abnormalName;

    public TempReportAbnormal() {
    }

    public TempReportAbnormal(
           java.lang.String reportNo,
           java.lang.String abnormalName) {
           this.reportNo = reportNo;
           this.abnormalName = abnormalName;
    }


    /**
     * Gets the reportNo value for this TempReportAbnormal.
     * 
     * @return reportNo
     */
    public java.lang.String getReportNo() {
        return reportNo;
    }


    /**
     * Sets the reportNo value for this TempReportAbnormal.
     * 
     * @param reportNo
     */
    public void setReportNo(java.lang.String reportNo) {
        this.reportNo = reportNo;
    }


    /**
     * Gets the abnormalName value for this TempReportAbnormal.
     * 
     * @return abnormalName
     */
    public java.lang.String getAbnormalName() {
        return abnormalName;
    }


    /**
     * Sets the abnormalName value for this TempReportAbnormal.
     * 
     * @param abnormalName
     */
    public void setAbnormalName(java.lang.String abnormalName) {
        this.abnormalName = abnormalName;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TempReportAbnormal)) return false;
        TempReportAbnormal other = (TempReportAbnormal) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.reportNo==null && other.getReportNo()==null) || 
             (this.reportNo!=null &&
              this.reportNo.equals(other.getReportNo()))) &&
            ((this.abnormalName==null && other.getAbnormalName()==null) || 
             (this.abnormalName!=null &&
              this.abnormalName.equals(other.getAbnormalName())));
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
        if (getReportNo() != null) {
            _hashCode += getReportNo().hashCode();
        }
        if (getAbnormalName() != null) {
            _hashCode += getAbnormalName().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TempReportAbnormal.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", "TempReportAbnormal"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reportNo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "ReportNo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("abnormalName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "AbnormalName"));
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
