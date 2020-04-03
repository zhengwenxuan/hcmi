/**
 * ReplyBean.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.yichang.gencode2;

public class ReplyBean  implements java.io.Serializable {
    private java.lang.String empiId;

    private java.lang.String message;

    private com.hjw.webService.client.yichang.gencode2.PatientInfoRep[] patientInfoRepList;

    private java.lang.String responseId;

    private java.lang.String status;

    public ReplyBean() {
    }

    public ReplyBean(
           java.lang.String empiId,
           java.lang.String message,
           com.hjw.webService.client.yichang.gencode2.PatientInfoRep[] patientInfoRepList,
           java.lang.String responseId,
           java.lang.String status) {
           this.empiId = empiId;
           this.message = message;
           this.patientInfoRepList = patientInfoRepList;
           this.responseId = responseId;
           this.status = status;
    }


    /**
     * Gets the empiId value for this ReplyBean.
     * 
     * @return empiId
     */
    public java.lang.String getEmpiId() {
        return empiId;
    }


    /**
     * Sets the empiId value for this ReplyBean.
     * 
     * @param empiId
     */
    public void setEmpiId(java.lang.String empiId) {
        this.empiId = empiId;
    }


    /**
     * Gets the message value for this ReplyBean.
     * 
     * @return message
     */
    public java.lang.String getMessage() {
        return message;
    }


    /**
     * Sets the message value for this ReplyBean.
     * 
     * @param message
     */
    public void setMessage(java.lang.String message) {
        this.message = message;
    }


    /**
     * Gets the patientInfoRepList value for this ReplyBean.
     * 
     * @return patientInfoRepList
     */
    public com.hjw.webService.client.yichang.gencode2.PatientInfoRep[] getPatientInfoRepList() {
        return patientInfoRepList;
    }


    /**
     * Sets the patientInfoRepList value for this ReplyBean.
     * 
     * @param patientInfoRepList
     */
    public void setPatientInfoRepList(com.hjw.webService.client.yichang.gencode2.PatientInfoRep[] patientInfoRepList) {
        this.patientInfoRepList = patientInfoRepList;
    }

    public com.hjw.webService.client.yichang.gencode2.PatientInfoRep getPatientInfoRepList(int i) {
        return this.patientInfoRepList[i];
    }

    public void setPatientInfoRepList(int i, com.hjw.webService.client.yichang.gencode2.PatientInfoRep _value) {
        this.patientInfoRepList[i] = _value;
    }


    /**
     * Gets the responseId value for this ReplyBean.
     * 
     * @return responseId
     */
    public java.lang.String getResponseId() {
        return responseId;
    }


    /**
     * Sets the responseId value for this ReplyBean.
     * 
     * @param responseId
     */
    public void setResponseId(java.lang.String responseId) {
        this.responseId = responseId;
    }


    /**
     * Gets the status value for this ReplyBean.
     * 
     * @return status
     */
    public java.lang.String getStatus() {
        return status;
    }


    /**
     * Sets the status value for this ReplyBean.
     * 
     * @param status
     */
    public void setStatus(java.lang.String status) {
        this.status = status;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof ReplyBean)) return false;
        ReplyBean other = (ReplyBean) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.empiId==null && other.getEmpiId()==null) || 
             (this.empiId!=null &&
              this.empiId.equals(other.getEmpiId()))) &&
            ((this.message==null && other.getMessage()==null) || 
             (this.message!=null &&
              this.message.equals(other.getMessage()))) &&
            ((this.patientInfoRepList==null && other.getPatientInfoRepList()==null) || 
             (this.patientInfoRepList!=null &&
              java.util.Arrays.equals(this.patientInfoRepList, other.getPatientInfoRepList()))) &&
            ((this.responseId==null && other.getResponseId()==null) || 
             (this.responseId!=null &&
              this.responseId.equals(other.getResponseId()))) &&
            ((this.status==null && other.getStatus()==null) || 
             (this.status!=null &&
              this.status.equals(other.getStatus())));
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
        if (getEmpiId() != null) {
            _hashCode += getEmpiId().hashCode();
        }
        if (getMessage() != null) {
            _hashCode += getMessage().hashCode();
        }
        if (getPatientInfoRepList() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getPatientInfoRepList());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getPatientInfoRepList(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getResponseId() != null) {
            _hashCode += getResponseId().hashCode();
        }
        if (getStatus() != null) {
            _hashCode += getStatus().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(ReplyBean.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://cdr.empi.neusoft.com/", "replyBean"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("empiId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "empiId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("message");
        elemField.setXmlName(new javax.xml.namespace.QName("", "message"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("patientInfoRepList");
        elemField.setXmlName(new javax.xml.namespace.QName("", "patientInfoRepList"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://cdr.empi.neusoft.com/", "patientInfoRep"));
        elemField.setMinOccurs(0);
        elemField.setNillable(true);
        elemField.setMaxOccursUnbounded(true);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("responseId");
        elemField.setXmlName(new javax.xml.namespace.QName("", "responseId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("status");
        elemField.setXmlName(new javax.xml.namespace.QName("", "status"));
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
