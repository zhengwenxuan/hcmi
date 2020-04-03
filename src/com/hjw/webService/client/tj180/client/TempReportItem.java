/**
 * TempReportItem.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.tj180.client;

public class TempReportItem  implements java.io.Serializable {
    private java.lang.String reportNo;

    private java.lang.String sectionName;

    private java.lang.String itemName;

    private java.lang.String itemValue;

    private java.lang.String itemTag;

    private java.math.BigDecimal minValue;

    private java.math.BigDecimal maxValue;

    private java.lang.String units;

    private java.lang.String reference;

    public TempReportItem() {
    }

    public TempReportItem(
           java.lang.String reportNo,
           java.lang.String sectionName,
           java.lang.String itemName,
           java.lang.String itemValue,
           java.lang.String itemTag,
           java.math.BigDecimal minValue,
           java.math.BigDecimal maxValue,
           java.lang.String units,
           java.lang.String reference) {
           this.reportNo = reportNo;
           this.sectionName = sectionName;
           this.itemName = itemName;
           this.itemValue = itemValue;
           this.itemTag = itemTag;
           this.minValue = minValue;
           this.maxValue = maxValue;
           this.units = units;
           this.reference = reference;
    }


    /**
     * Gets the reportNo value for this TempReportItem.
     * 
     * @return reportNo
     */
    public java.lang.String getReportNo() {
        return reportNo;
    }


    /**
     * Sets the reportNo value for this TempReportItem.
     * 
     * @param reportNo
     */
    public void setReportNo(java.lang.String reportNo) {
        this.reportNo = reportNo;
    }


    /**
     * Gets the sectionName value for this TempReportItem.
     * 
     * @return sectionName
     */
    public java.lang.String getSectionName() {
        return sectionName;
    }


    /**
     * Sets the sectionName value for this TempReportItem.
     * 
     * @param sectionName
     */
    public void setSectionName(java.lang.String sectionName) {
        this.sectionName = sectionName;
    }


    /**
     * Gets the itemName value for this TempReportItem.
     * 
     * @return itemName
     */
    public java.lang.String getItemName() {
        return itemName;
    }


    /**
     * Sets the itemName value for this TempReportItem.
     * 
     * @param itemName
     */
    public void setItemName(java.lang.String itemName) {
        this.itemName = itemName;
    }


    /**
     * Gets the itemValue value for this TempReportItem.
     * 
     * @return itemValue
     */
    public java.lang.String getItemValue() {
        return itemValue;
    }


    /**
     * Sets the itemValue value for this TempReportItem.
     * 
     * @param itemValue
     */
    public void setItemValue(java.lang.String itemValue) {
        this.itemValue = itemValue;
    }


    /**
     * Gets the itemTag value for this TempReportItem.
     * 
     * @return itemTag
     */
    public java.lang.String getItemTag() {
        return itemTag;
    }


    /**
     * Sets the itemTag value for this TempReportItem.
     * 
     * @param itemTag
     */
    public void setItemTag(java.lang.String itemTag) {
        this.itemTag = itemTag;
    }


    /**
     * Gets the minValue value for this TempReportItem.
     * 
     * @return minValue
     */
    public java.math.BigDecimal getMinValue() {
        return minValue;
    }


    /**
     * Sets the minValue value for this TempReportItem.
     * 
     * @param minValue
     */
    public void setMinValue(java.math.BigDecimal minValue) {
        this.minValue = minValue;
    }


    /**
     * Gets the maxValue value for this TempReportItem.
     * 
     * @return maxValue
     */
    public java.math.BigDecimal getMaxValue() {
        return maxValue;
    }


    /**
     * Sets the maxValue value for this TempReportItem.
     * 
     * @param maxValue
     */
    public void setMaxValue(java.math.BigDecimal maxValue) {
        this.maxValue = maxValue;
    }


    /**
     * Gets the units value for this TempReportItem.
     * 
     * @return units
     */
    public java.lang.String getUnits() {
        return units;
    }


    /**
     * Sets the units value for this TempReportItem.
     * 
     * @param units
     */
    public void setUnits(java.lang.String units) {
        this.units = units;
    }


    /**
     * Gets the reference value for this TempReportItem.
     * 
     * @return reference
     */
    public java.lang.String getReference() {
        return reference;
    }


    /**
     * Sets the reference value for this TempReportItem.
     * 
     * @param reference
     */
    public void setReference(java.lang.String reference) {
        this.reference = reference;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TempReportItem)) return false;
        TempReportItem other = (TempReportItem) obj;
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
            ((this.itemName==null && other.getItemName()==null) || 
             (this.itemName!=null &&
              this.itemName.equals(other.getItemName()))) &&
            ((this.itemValue==null && other.getItemValue()==null) || 
             (this.itemValue!=null &&
              this.itemValue.equals(other.getItemValue()))) &&
            ((this.itemTag==null && other.getItemTag()==null) || 
             (this.itemTag!=null &&
              this.itemTag.equals(other.getItemTag()))) &&
            ((this.minValue==null && other.getMinValue()==null) || 
             (this.minValue!=null &&
              this.minValue.equals(other.getMinValue()))) &&
            ((this.maxValue==null && other.getMaxValue()==null) || 
             (this.maxValue!=null &&
              this.maxValue.equals(other.getMaxValue()))) &&
            ((this.units==null && other.getUnits()==null) || 
             (this.units!=null &&
              this.units.equals(other.getUnits()))) &&
            ((this.reference==null && other.getReference()==null) || 
             (this.reference!=null &&
              this.reference.equals(other.getReference())));
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
        if (getItemName() != null) {
            _hashCode += getItemName().hashCode();
        }
        if (getItemValue() != null) {
            _hashCode += getItemValue().hashCode();
        }
        if (getItemTag() != null) {
            _hashCode += getItemTag().hashCode();
        }
        if (getMinValue() != null) {
            _hashCode += getMinValue().hashCode();
        }
        if (getMaxValue() != null) {
            _hashCode += getMaxValue().hashCode();
        }
        if (getUnits() != null) {
            _hashCode += getUnits().hashCode();
        }
        if (getReference() != null) {
            _hashCode += getReference().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TempReportItem.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", "TempReportItem"));
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
        elemField.setFieldName("itemName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "ItemName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("itemValue");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "ItemValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("itemTag");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "ItemTag"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("minValue");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "MinValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("maxValue");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "MaxValue"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "decimal"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("units");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "Units"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reference");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "Reference"));
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
