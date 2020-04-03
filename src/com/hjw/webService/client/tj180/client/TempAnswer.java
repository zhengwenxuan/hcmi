/**
 * TempAnswer.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.tj180.client;

public class TempAnswer  implements java.io.Serializable {
    private java.lang.String id;

    private java.lang.String questionNo;

    private java.lang.String answerNo;

    private java.lang.String isSystem;

    public TempAnswer() {
    }

    public TempAnswer(
           java.lang.String id,
           java.lang.String questionNo,
           java.lang.String answerNo,
           java.lang.String isSystem) {
           this.id = id;
           this.questionNo = questionNo;
           this.answerNo = answerNo;
           this.isSystem = isSystem;
    }


    /**
     * Gets the id value for this TempAnswer.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this TempAnswer.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the questionNo value for this TempAnswer.
     * 
     * @return questionNo
     */
    public java.lang.String getQuestionNo() {
        return questionNo;
    }


    /**
     * Sets the questionNo value for this TempAnswer.
     * 
     * @param questionNo
     */
    public void setQuestionNo(java.lang.String questionNo) {
        this.questionNo = questionNo;
    }


    /**
     * Gets the answerNo value for this TempAnswer.
     * 
     * @return answerNo
     */
    public java.lang.String getAnswerNo() {
        return answerNo;
    }


    /**
     * Sets the answerNo value for this TempAnswer.
     * 
     * @param answerNo
     */
    public void setAnswerNo(java.lang.String answerNo) {
        this.answerNo = answerNo;
    }


    /**
     * Gets the isSystem value for this TempAnswer.
     * 
     * @return isSystem
     */
    public java.lang.String getIsSystem() {
        return isSystem;
    }


    /**
     * Sets the isSystem value for this TempAnswer.
     * 
     * @param isSystem
     */
    public void setIsSystem(java.lang.String isSystem) {
        this.isSystem = isSystem;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TempAnswer)) return false;
        TempAnswer other = (TempAnswer) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            ((this.questionNo==null && other.getQuestionNo()==null) || 
             (this.questionNo!=null &&
              this.questionNo.equals(other.getQuestionNo()))) &&
            ((this.answerNo==null && other.getAnswerNo()==null) || 
             (this.answerNo!=null &&
              this.answerNo.equals(other.getAnswerNo()))) &&
            ((this.isSystem==null && other.getIsSystem()==null) || 
             (this.isSystem!=null &&
              this.isSystem.equals(other.getIsSystem())));
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
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        if (getQuestionNo() != null) {
            _hashCode += getQuestionNo().hashCode();
        }
        if (getAnswerNo() != null) {
            _hashCode += getAnswerNo().hashCode();
        }
        if (getIsSystem() != null) {
            _hashCode += getIsSystem().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TempAnswer.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", "TempAnswer"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "Id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("questionNo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "QuestionNo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("answerNo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "AnswerNo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isSystem");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "IsSystem"));
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
