/**
 * TempReportConclusion.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.tj180.client;

public class TempReportConclusion  implements java.io.Serializable {
    private java.lang.String reportNo;

    private java.lang.String sectionName;

    private java.lang.String conclusion;

    private java.lang.String checkUser;

    private java.util.Calendar checkTime;

    public TempReportConclusion() {
    }

    public TempReportConclusion(
           java.lang.String reportNo,
           java.lang.String sectionName,
           java.lang.String conclusion,
           java.lang.String checkUser,
           java.util.Calendar checkTime) {
           this.reportNo = reportNo;
           this.sectionName = sectionName;
           this.conclusion = conclusion;
           this.checkUser = checkUser;
           this.checkTime = checkTime;
    }


    /**
     * Gets the reportNo value for this TempReportConclusion.
     * 
     * @return reportNo
     */
    public java.lang.String getReportNo() {
        return reportNo;
    }


    /**
     * Sets the reportNo value for this TempReportConclusion.
     * 
     * @param reportNo
     */
    public void setReportNo(java.lang.String reportNo) {
        this.reportNo = reportNo;
    }


    /**
     * Gets the sectionName value for this TempReportConclusion.
     * 
     * @return sectionName
     */
    public java.lang.String getSectionName() {
        return sectionName;
    }


    /**
     * Sets the sectionName value for this TempReportConclusion.
     * 
     * @param sectionName
     */
    public void setSectionName(java.lang.String sectionName) {
        this.sectionName = sectionName;
    }


    /**
     * Gets the conclusion value for this TempReportConclusion.
     * 
     * @return conclusion
     */
    public java.lang.String getConclusion() {
        return conclusion;
    }


    /**
     * Sets the conclusion value for this TempReportConclusion.
     * 
     * @param conclusion
     */
    public void setConclusion(java.lang.String conclusion) {
        this.conclusion = conclusion;
    }


    /**
     * Gets the checkUser value for this TempReportConclusion.
     * 
     * @return checkUser
     */
    public java.lang.String getCheckUser() {
        return checkUser;
    }


    /**
     * Sets the checkUser value for this TempReportConclusion.
     * 
     * @param checkUser
     */
    public void setCheckUser(java.lang.String checkUser) {
        this.checkUser = checkUser;
    }


    /**
     * Gets the checkTime value for this TempReportConclusion.
     * 
     * @return checkTime
     */
    public java.util.Calendar getCheckTime() {
        return checkTime;
    }


    /**
     * Sets the checkTime value for this TempReportConclusion.
     * 
     * @param checkTime
     */
    public void setCheckTime(java.util.Calendar checkTime) {
        this.checkTime = checkTime;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TempReportConclusion)) return false;
        TempReportConclusion other = (TempReportConclusion) obj;
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
            ((this.sectionName==null && other.getSectionName()==null) || 
             (this.sectionName!=null &&
              this.sectionName.equals(other.getSectionName()))) &&
            ((this.conclusion==null && other.getConclusion()==null) || 
             (this.conclusion!=null &&
              this.conclusion.equals(other.getConclusion()))) &&
            ((this.checkUser==null && other.getCheckUser()==null) || 
             (this.checkUser!=null &&
              this.checkUser.equals(other.getCheckUser()))) &&
            ((this.checkTime==null && other.getCheckTime()==null) || 
             (this.checkTime!=null &&
              this.checkTime.equals(other.getCheckTime())));
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
        if (getSectionName() != null) {
            _hashCode += getSectionName().hashCode();
        }
        if (getConclusion() != null) {
            _hashCode += getConclusion().hashCode();
        }
        if (getCheckUser() != null) {
            _hashCode += getCheckUser().hashCode();
        }
        if (getCheckTime() != null) {
            _hashCode += getCheckTime().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TempReportConclusion.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", "TempReportConclusion"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reportNo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "ReportNo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("sectionName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "SectionName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("conclusion");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "Conclusion"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("checkUser");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "CheckUser"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("checkTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "CheckTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
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
