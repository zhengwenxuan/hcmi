/**
 * TempQuestion.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.tj180.client;

public class TempQuestion  implements java.io.Serializable {
    private java.lang.String organId;

    private java.lang.String id;

    private java.lang.String userName;

    private java.lang.String gender;

    private java.lang.String cardNo;

    private java.lang.String mobile;

    private java.lang.String bloodType;

    private java.lang.String profession;

    private java.lang.String matrimony;

    private java.lang.String degreeEducation;

    private java.lang.String nationalty;

    private java.lang.String reportNo;

    private java.lang.String questionType;

    private java.util.Calendar questionDate;

    private java.lang.String source;

    private java.lang.String is_Up;

    private java.util.Calendar addTime;

    private java.lang.String bak_Field1;

    private java.lang.String bak_Field2;

    private java.lang.String bak_Field3;

    private java.lang.String bak_Field4;

    private java.lang.String bak_Field5;

    private com.hjw.webService.client.tj180.client.TempAnswer[] tempAnswerS;

    private java.lang.String customId;

    public TempQuestion() {
    }

    public TempQuestion(
           java.lang.String organId,
           java.lang.String id,
           java.lang.String userName,
           java.lang.String gender,
           java.lang.String cardNo,
           java.lang.String mobile,
           java.lang.String bloodType,
           java.lang.String profession,
           java.lang.String matrimony,
           java.lang.String degreeEducation,
           java.lang.String nationalty,
           java.lang.String reportNo,
           java.lang.String questionType,
           java.util.Calendar questionDate,
           java.lang.String source,
           java.lang.String is_Up,
           java.util.Calendar addTime,
           java.lang.String bak_Field1,
           java.lang.String bak_Field2,
           java.lang.String bak_Field3,
           java.lang.String bak_Field4,
           java.lang.String bak_Field5,
           com.hjw.webService.client.tj180.client.TempAnswer[] tempAnswerS,
           java.lang.String customId) {
           this.organId = organId;
           this.id = id;
           this.userName = userName;
           this.gender = gender;
           this.cardNo = cardNo;
           this.mobile = mobile;
           this.bloodType = bloodType;
           this.profession = profession;
           this.matrimony = matrimony;
           this.degreeEducation = degreeEducation;
           this.nationalty = nationalty;
           this.reportNo = reportNo;
           this.questionType = questionType;
           this.questionDate = questionDate;
           this.source = source;
           this.is_Up = is_Up;
           this.addTime = addTime;
           this.bak_Field1 = bak_Field1;
           this.bak_Field2 = bak_Field2;
           this.bak_Field3 = bak_Field3;
           this.bak_Field4 = bak_Field4;
           this.bak_Field5 = bak_Field5;
           this.tempAnswerS = tempAnswerS;
           this.customId = customId;
    }


    /**
     * Gets the organId value for this TempQuestion.
     * 
     * @return organId
     */
    public java.lang.String getOrganId() {
        return organId;
    }


    /**
     * Sets the organId value for this TempQuestion.
     * 
     * @param organId
     */
    public void setOrganId(java.lang.String organId) {
        this.organId = organId;
    }


    /**
     * Gets the id value for this TempQuestion.
     * 
     * @return id
     */
    public java.lang.String getId() {
        return id;
    }


    /**
     * Sets the id value for this TempQuestion.
     * 
     * @param id
     */
    public void setId(java.lang.String id) {
        this.id = id;
    }


    /**
     * Gets the userName value for this TempQuestion.
     * 
     * @return userName
     */
    public java.lang.String getUserName() {
        return userName;
    }


    /**
     * Sets the userName value for this TempQuestion.
     * 
     * @param userName
     */
    public void setUserName(java.lang.String userName) {
        this.userName = userName;
    }


    /**
     * Gets the gender value for this TempQuestion.
     * 
     * @return gender
     */
    public java.lang.String getGender() {
        return gender;
    }


    /**
     * Sets the gender value for this TempQuestion.
     * 
     * @param gender
     */
    public void setGender(java.lang.String gender) {
        this.gender = gender;
    }


    /**
     * Gets the cardNo value for this TempQuestion.
     * 
     * @return cardNo
     */
    public java.lang.String getCardNo() {
        return cardNo;
    }


    /**
     * Sets the cardNo value for this TempQuestion.
     * 
     * @param cardNo
     */
    public void setCardNo(java.lang.String cardNo) {
        this.cardNo = cardNo;
    }


    /**
     * Gets the mobile value for this TempQuestion.
     * 
     * @return mobile
     */
    public java.lang.String getMobile() {
        return mobile;
    }


    /**
     * Sets the mobile value for this TempQuestion.
     * 
     * @param mobile
     */
    public void setMobile(java.lang.String mobile) {
        this.mobile = mobile;
    }


    /**
     * Gets the bloodType value for this TempQuestion.
     * 
     * @return bloodType
     */
    public java.lang.String getBloodType() {
        return bloodType;
    }


    /**
     * Sets the bloodType value for this TempQuestion.
     * 
     * @param bloodType
     */
    public void setBloodType(java.lang.String bloodType) {
        this.bloodType = bloodType;
    }


    /**
     * Gets the profession value for this TempQuestion.
     * 
     * @return profession
     */
    public java.lang.String getProfession() {
        return profession;
    }


    /**
     * Sets the profession value for this TempQuestion.
     * 
     * @param profession
     */
    public void setProfession(java.lang.String profession) {
        this.profession = profession;
    }


    /**
     * Gets the matrimony value for this TempQuestion.
     * 
     * @return matrimony
     */
    public java.lang.String getMatrimony() {
        return matrimony;
    }


    /**
     * Sets the matrimony value for this TempQuestion.
     * 
     * @param matrimony
     */
    public void setMatrimony(java.lang.String matrimony) {
        this.matrimony = matrimony;
    }


    /**
     * Gets the degreeEducation value for this TempQuestion.
     * 
     * @return degreeEducation
     */
    public java.lang.String getDegreeEducation() {
        return degreeEducation;
    }


    /**
     * Sets the degreeEducation value for this TempQuestion.
     * 
     * @param degreeEducation
     */
    public void setDegreeEducation(java.lang.String degreeEducation) {
        this.degreeEducation = degreeEducation;
    }


    /**
     * Gets the nationalty value for this TempQuestion.
     * 
     * @return nationalty
     */
    public java.lang.String getNationalty() {
        return nationalty;
    }


    /**
     * Sets the nationalty value for this TempQuestion.
     * 
     * @param nationalty
     */
    public void setNationalty(java.lang.String nationalty) {
        this.nationalty = nationalty;
    }


    /**
     * Gets the reportNo value for this TempQuestion.
     * 
     * @return reportNo
     */
    public java.lang.String getReportNo() {
        return reportNo;
    }


    /**
     * Sets the reportNo value for this TempQuestion.
     * 
     * @param reportNo
     */
    public void setReportNo(java.lang.String reportNo) {
        this.reportNo = reportNo;
    }


    /**
     * Gets the questionType value for this TempQuestion.
     * 
     * @return questionType
     */
    public java.lang.String getQuestionType() {
        return questionType;
    }


    /**
     * Sets the questionType value for this TempQuestion.
     * 
     * @param questionType
     */
    public void setQuestionType(java.lang.String questionType) {
        this.questionType = questionType;
    }


    /**
     * Gets the questionDate value for this TempQuestion.
     * 
     * @return questionDate
     */
    public java.util.Calendar getQuestionDate() {
        return questionDate;
    }


    /**
     * Sets the questionDate value for this TempQuestion.
     * 
     * @param questionDate
     */
    public void setQuestionDate(java.util.Calendar questionDate) {
        this.questionDate = questionDate;
    }


    /**
     * Gets the source value for this TempQuestion.
     * 
     * @return source
     */
    public java.lang.String getSource() {
        return source;
    }


    /**
     * Sets the source value for this TempQuestion.
     * 
     * @param source
     */
    public void setSource(java.lang.String source) {
        this.source = source;
    }


    /**
     * Gets the is_Up value for this TempQuestion.
     * 
     * @return is_Up
     */
    public java.lang.String getIs_Up() {
        return is_Up;
    }


    /**
     * Sets the is_Up value for this TempQuestion.
     * 
     * @param is_Up
     */
    public void setIs_Up(java.lang.String is_Up) {
        this.is_Up = is_Up;
    }


    /**
     * Gets the addTime value for this TempQuestion.
     * 
     * @return addTime
     */
    public java.util.Calendar getAddTime() {
        return addTime;
    }


    /**
     * Sets the addTime value for this TempQuestion.
     * 
     * @param addTime
     */
    public void setAddTime(java.util.Calendar addTime) {
        this.addTime = addTime;
    }


    /**
     * Gets the bak_Field1 value for this TempQuestion.
     * 
     * @return bak_Field1
     */
    public java.lang.String getBak_Field1() {
        return bak_Field1;
    }


    /**
     * Sets the bak_Field1 value for this TempQuestion.
     * 
     * @param bak_Field1
     */
    public void setBak_Field1(java.lang.String bak_Field1) {
        this.bak_Field1 = bak_Field1;
    }


    /**
     * Gets the bak_Field2 value for this TempQuestion.
     * 
     * @return bak_Field2
     */
    public java.lang.String getBak_Field2() {
        return bak_Field2;
    }


    /**
     * Sets the bak_Field2 value for this TempQuestion.
     * 
     * @param bak_Field2
     */
    public void setBak_Field2(java.lang.String bak_Field2) {
        this.bak_Field2 = bak_Field2;
    }


    /**
     * Gets the bak_Field3 value for this TempQuestion.
     * 
     * @return bak_Field3
     */
    public java.lang.String getBak_Field3() {
        return bak_Field3;
    }


    /**
     * Sets the bak_Field3 value for this TempQuestion.
     * 
     * @param bak_Field3
     */
    public void setBak_Field3(java.lang.String bak_Field3) {
        this.bak_Field3 = bak_Field3;
    }


    /**
     * Gets the bak_Field4 value for this TempQuestion.
     * 
     * @return bak_Field4
     */
    public java.lang.String getBak_Field4() {
        return bak_Field4;
    }


    /**
     * Sets the bak_Field4 value for this TempQuestion.
     * 
     * @param bak_Field4
     */
    public void setBak_Field4(java.lang.String bak_Field4) {
        this.bak_Field4 = bak_Field4;
    }


    /**
     * Gets the bak_Field5 value for this TempQuestion.
     * 
     * @return bak_Field5
     */
    public java.lang.String getBak_Field5() {
        return bak_Field5;
    }


    /**
     * Sets the bak_Field5 value for this TempQuestion.
     * 
     * @param bak_Field5
     */
    public void setBak_Field5(java.lang.String bak_Field5) {
        this.bak_Field5 = bak_Field5;
    }


    /**
     * Gets the tempAnswerS value for this TempQuestion.
     * 
     * @return tempAnswerS
     */
    public com.hjw.webService.client.tj180.client.TempAnswer[] getTempAnswerS() {
        return tempAnswerS;
    }


    /**
     * Sets the tempAnswerS value for this TempQuestion.
     * 
     * @param tempAnswerS
     */
    public void setTempAnswerS(com.hjw.webService.client.tj180.client.TempAnswer[] tempAnswerS) {
        this.tempAnswerS = tempAnswerS;
    }


    /**
     * Gets the customId value for this TempQuestion.
     * 
     * @return customId
     */
    public java.lang.String getCustomId() {
        return customId;
    }


    /**
     * Sets the customId value for this TempQuestion.
     * 
     * @param customId
     */
    public void setCustomId(java.lang.String customId) {
        this.customId = customId;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TempQuestion)) return false;
        TempQuestion other = (TempQuestion) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.organId==null && other.getOrganId()==null) || 
             (this.organId!=null &&
              this.organId.equals(other.getOrganId()))) &&
            ((this.id==null && other.getId()==null) || 
             (this.id!=null &&
              this.id.equals(other.getId()))) &&
            ((this.userName==null && other.getUserName()==null) || 
             (this.userName!=null &&
              this.userName.equals(other.getUserName()))) &&
            ((this.gender==null && other.getGender()==null) || 
             (this.gender!=null &&
              this.gender.equals(other.getGender()))) &&
            ((this.cardNo==null && other.getCardNo()==null) || 
             (this.cardNo!=null &&
              this.cardNo.equals(other.getCardNo()))) &&
            ((this.mobile==null && other.getMobile()==null) || 
             (this.mobile!=null &&
              this.mobile.equals(other.getMobile()))) &&
            ((this.bloodType==null && other.getBloodType()==null) || 
             (this.bloodType!=null &&
              this.bloodType.equals(other.getBloodType()))) &&
            ((this.profession==null && other.getProfession()==null) || 
             (this.profession!=null &&
              this.profession.equals(other.getProfession()))) &&
            ((this.matrimony==null && other.getMatrimony()==null) || 
             (this.matrimony!=null &&
              this.matrimony.equals(other.getMatrimony()))) &&
            ((this.degreeEducation==null && other.getDegreeEducation()==null) || 
             (this.degreeEducation!=null &&
              this.degreeEducation.equals(other.getDegreeEducation()))) &&
            ((this.nationalty==null && other.getNationalty()==null) || 
             (this.nationalty!=null &&
              this.nationalty.equals(other.getNationalty()))) &&
            ((this.reportNo==null && other.getReportNo()==null) || 
             (this.reportNo!=null &&
              this.reportNo.equals(other.getReportNo()))) &&
            ((this.questionType==null && other.getQuestionType()==null) || 
             (this.questionType!=null &&
              this.questionType.equals(other.getQuestionType()))) &&
            ((this.questionDate==null && other.getQuestionDate()==null) || 
             (this.questionDate!=null &&
              this.questionDate.equals(other.getQuestionDate()))) &&
            ((this.source==null && other.getSource()==null) || 
             (this.source!=null &&
              this.source.equals(other.getSource()))) &&
            ((this.is_Up==null && other.getIs_Up()==null) || 
             (this.is_Up!=null &&
              this.is_Up.equals(other.getIs_Up()))) &&
            ((this.addTime==null && other.getAddTime()==null) || 
             (this.addTime!=null &&
              this.addTime.equals(other.getAddTime()))) &&
            ((this.bak_Field1==null && other.getBak_Field1()==null) || 
             (this.bak_Field1!=null &&
              this.bak_Field1.equals(other.getBak_Field1()))) &&
            ((this.bak_Field2==null && other.getBak_Field2()==null) || 
             (this.bak_Field2!=null &&
              this.bak_Field2.equals(other.getBak_Field2()))) &&
            ((this.bak_Field3==null && other.getBak_Field3()==null) || 
             (this.bak_Field3!=null &&
              this.bak_Field3.equals(other.getBak_Field3()))) &&
            ((this.bak_Field4==null && other.getBak_Field4()==null) || 
             (this.bak_Field4!=null &&
              this.bak_Field4.equals(other.getBak_Field4()))) &&
            ((this.bak_Field5==null && other.getBak_Field5()==null) || 
             (this.bak_Field5!=null &&
              this.bak_Field5.equals(other.getBak_Field5()))) &&
            ((this.tempAnswerS==null && other.getTempAnswerS()==null) || 
             (this.tempAnswerS!=null &&
              java.util.Arrays.equals(this.tempAnswerS, other.getTempAnswerS()))) &&
            ((this.customId==null && other.getCustomId()==null) || 
             (this.customId!=null &&
              this.customId.equals(other.getCustomId())));
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
        if (getOrganId() != null) {
            _hashCode += getOrganId().hashCode();
        }
        if (getId() != null) {
            _hashCode += getId().hashCode();
        }
        if (getUserName() != null) {
            _hashCode += getUserName().hashCode();
        }
        if (getGender() != null) {
            _hashCode += getGender().hashCode();
        }
        if (getCardNo() != null) {
            _hashCode += getCardNo().hashCode();
        }
        if (getMobile() != null) {
            _hashCode += getMobile().hashCode();
        }
        if (getBloodType() != null) {
            _hashCode += getBloodType().hashCode();
        }
        if (getProfession() != null) {
            _hashCode += getProfession().hashCode();
        }
        if (getMatrimony() != null) {
            _hashCode += getMatrimony().hashCode();
        }
        if (getDegreeEducation() != null) {
            _hashCode += getDegreeEducation().hashCode();
        }
        if (getNationalty() != null) {
            _hashCode += getNationalty().hashCode();
        }
        if (getReportNo() != null) {
            _hashCode += getReportNo().hashCode();
        }
        if (getQuestionType() != null) {
            _hashCode += getQuestionType().hashCode();
        }
        if (getQuestionDate() != null) {
            _hashCode += getQuestionDate().hashCode();
        }
        if (getSource() != null) {
            _hashCode += getSource().hashCode();
        }
        if (getIs_Up() != null) {
            _hashCode += getIs_Up().hashCode();
        }
        if (getAddTime() != null) {
            _hashCode += getAddTime().hashCode();
        }
        if (getBak_Field1() != null) {
            _hashCode += getBak_Field1().hashCode();
        }
        if (getBak_Field2() != null) {
            _hashCode += getBak_Field2().hashCode();
        }
        if (getBak_Field3() != null) {
            _hashCode += getBak_Field3().hashCode();
        }
        if (getBak_Field4() != null) {
            _hashCode += getBak_Field4().hashCode();
        }
        if (getBak_Field5() != null) {
            _hashCode += getBak_Field5().hashCode();
        }
        if (getTempAnswerS() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getTempAnswerS());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getTempAnswerS(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getCustomId() != null) {
            _hashCode += getCustomId().hashCode();
        }
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TempQuestion.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", "TempQuestion"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("organId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "OrganId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("id");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "Id"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("userName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "UserName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("gender");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "Gender"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("cardNo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "CardNo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("mobile");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "Mobile"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bloodType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "BloodType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("profession");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "Profession"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("matrimony");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "Matrimony"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("degreeEducation");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "DegreeEducation"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("nationalty");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "Nationalty"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reportNo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "ReportNo"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("questionType");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "QuestionType"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("questionDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "QuestionDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("source");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "Source"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("is_Up");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "Is_Up"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("addTime");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "AddTime"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bak_Field1");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "Bak_Field1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bak_Field2");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "Bak_Field2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bak_Field3");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "Bak_Field3"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bak_Field4");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "Bak_Field4"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bak_Field5");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "Bak_Field5"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("tempAnswerS");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "TempAnswerS"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", "TempAnswer"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://tempuri.org/", "TempAnswer"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("customId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "CustomId"));
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
