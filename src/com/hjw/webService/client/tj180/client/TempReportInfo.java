/**
 * TempReportInfo.java
 *
 * This file was auto-generated from WSDL
 * by the Apache Axis 1.4 Apr 22, 2006 (06:55:48 PDT) WSDL2Java emitter.
 */

package com.hjw.webService.client.tj180.client;

public class TempReportInfo  implements java.io.Serializable {
    private java.lang.String[] healthConsultant;

    private java.lang.String libraryId;

    private java.lang.String gradeID;

    private java.lang.String organID;

    private java.lang.String reportNo;

    private java.util.Calendar reportDate;

    private java.lang.String reportState;

    private java.lang.String userName;

    private java.lang.String userNo;

    private java.lang.String gender;

    private java.util.Calendar birthday;

    private java.lang.String cardNo;

    private java.lang.String mobile;

    private java.lang.String telephone;

    private java.lang.String email;

    private java.lang.String workUnit;

    private java.lang.String addresss;

    private java.lang.String loginName;

    private java.lang.String loginPwd;

    private java.lang.String summarize;

    private java.lang.String advice;

    private java.lang.String chargesItem;

    private java.lang.String ZJDoctor;

    private java.util.Calendar ZJDate;

    private java.lang.String bakField1;

    private java.lang.String bakField2;

    private java.lang.String bakField3;

    private java.lang.String bakField4;

    private java.lang.String bakField5;

    private com.hjw.webService.client.tj180.client.TempReportAbnormal[] reportAbnormalS;

    private com.hjw.webService.client.tj180.client.TempReportConclusion[] reportConclusionS;

    private com.hjw.webService.client.tj180.client.TempReportItem[] reportItemS;

    private int isRepeat;

    private int isUpdate;

    private int isUpdateReport;

    public TempReportInfo() {
    }

    public TempReportInfo(
           java.lang.String[] healthConsultant,
           java.lang.String libraryId,
           java.lang.String gradeID,
           java.lang.String organID,
           java.lang.String reportNo,
           java.util.Calendar reportDate,
           java.lang.String reportState,
           java.lang.String userName,
           java.lang.String userNo,
           java.lang.String gender,
           java.util.Calendar birthday,
           java.lang.String cardNo,
           java.lang.String mobile,
           java.lang.String telephone,
           java.lang.String email,
           java.lang.String workUnit,
           java.lang.String addresss,
           java.lang.String loginName,
           java.lang.String loginPwd,
           java.lang.String summarize,
           java.lang.String advice,
           java.lang.String chargesItem,
           java.lang.String ZJDoctor,
           java.util.Calendar ZJDate,
           java.lang.String bakField1,
           java.lang.String bakField2,
           java.lang.String bakField3,
           java.lang.String bakField4,
           java.lang.String bakField5,
           com.hjw.webService.client.tj180.client.TempReportAbnormal[] reportAbnormalS,
           com.hjw.webService.client.tj180.client.TempReportConclusion[] reportConclusionS,
           com.hjw.webService.client.tj180.client.TempReportItem[] reportItemS,
           int isRepeat,
           int isUpdate,
           int isUpdateReport) {
           this.healthConsultant = healthConsultant;
           this.libraryId = libraryId;
           this.gradeID = gradeID;
           this.organID = organID;
           this.reportNo = reportNo;
           this.reportDate = reportDate;
           this.reportState = reportState;
           this.userName = userName;
           this.userNo = userNo;
           this.gender = gender;
           this.birthday = birthday;
           this.cardNo = cardNo;
           this.mobile = mobile;
           this.telephone = telephone;
           this.email = email;
           this.workUnit = workUnit;
           this.addresss = addresss;
           this.loginName = loginName;
           this.loginPwd = loginPwd;
           this.summarize = summarize;
           this.advice = advice;
           this.chargesItem = chargesItem;
           this.ZJDoctor = ZJDoctor;
           this.ZJDate = ZJDate;
           this.bakField1 = bakField1;
           this.bakField2 = bakField2;
           this.bakField3 = bakField3;
           this.bakField4 = bakField4;
           this.bakField5 = bakField5;
           this.reportAbnormalS = reportAbnormalS;
           this.reportConclusionS = reportConclusionS;
           this.reportItemS = reportItemS;
           this.isRepeat = isRepeat;
           this.isUpdate = isUpdate;
           this.isUpdateReport = isUpdateReport;
    }


    /**
     * Gets the healthConsultant value for this TempReportInfo.
     * 
     * @return healthConsultant
     */
    public java.lang.String[] getHealthConsultant() {
        return healthConsultant;
    }


    /**
     * Sets the healthConsultant value for this TempReportInfo.
     * 
     * @param healthConsultant
     */
    public void setHealthConsultant(java.lang.String[] healthConsultant) {
        this.healthConsultant = healthConsultant;
    }


    /**
     * Gets the libraryId value for this TempReportInfo.
     * 
     * @return libraryId
     */
    public java.lang.String getLibraryId() {
        return libraryId;
    }


    /**
     * Sets the libraryId value for this TempReportInfo.
     * 
     * @param libraryId
     */
    public void setLibraryId(java.lang.String libraryId) {
        this.libraryId = libraryId;
    }


    /**
     * Gets the gradeID value for this TempReportInfo.
     * 
     * @return gradeID
     */
    public java.lang.String getGradeID() {
        return gradeID;
    }


    /**
     * Sets the gradeID value for this TempReportInfo.
     * 
     * @param gradeID
     */
    public void setGradeID(java.lang.String gradeID) {
        this.gradeID = gradeID;
    }


    /**
     * Gets the organID value for this TempReportInfo.
     * 
     * @return organID
     */
    public java.lang.String getOrganID() {
        return organID;
    }


    /**
     * Sets the organID value for this TempReportInfo.
     * 
     * @param organID
     */
    public void setOrganID(java.lang.String organID) {
        this.organID = organID;
    }


    /**
     * Gets the reportNo value for this TempReportInfo.
     * 
     * @return reportNo
     */
    public java.lang.String getReportNo() {
        return reportNo;
    }


    /**
     * Sets the reportNo value for this TempReportInfo.
     * 
     * @param reportNo
     */
    public void setReportNo(java.lang.String reportNo) {
        this.reportNo = reportNo;
    }


    /**
     * Gets the reportDate value for this TempReportInfo.
     * 
     * @return reportDate
     */
    public java.util.Calendar getReportDate() {
        return reportDate;
    }


    /**
     * Sets the reportDate value for this TempReportInfo.
     * 
     * @param reportDate
     */
    public void setReportDate(java.util.Calendar reportDate) {
        this.reportDate = reportDate;
    }


    /**
     * Gets the reportState value for this TempReportInfo.
     * 
     * @return reportState
     */
    public java.lang.String getReportState() {
        return reportState;
    }


    /**
     * Sets the reportState value for this TempReportInfo.
     * 
     * @param reportState
     */
    public void setReportState(java.lang.String reportState) {
        this.reportState = reportState;
    }


    /**
     * Gets the userName value for this TempReportInfo.
     * 
     * @return userName
     */
    public java.lang.String getUserName() {
        return userName;
    }


    /**
     * Sets the userName value for this TempReportInfo.
     * 
     * @param userName
     */
    public void setUserName(java.lang.String userName) {
        this.userName = userName;
    }


    /**
     * Gets the userNo value for this TempReportInfo.
     * 
     * @return userNo
     */
    public java.lang.String getUserNo() {
        return userNo;
    }


    /**
     * Sets the userNo value for this TempReportInfo.
     * 
     * @param userNo
     */
    public void setUserNo(java.lang.String userNo) {
        this.userNo = userNo;
    }


    /**
     * Gets the gender value for this TempReportInfo.
     * 
     * @return gender
     */
    public java.lang.String getGender() {
        return gender;
    }


    /**
     * Sets the gender value for this TempReportInfo.
     * 
     * @param gender
     */
    public void setGender(java.lang.String gender) {
        this.gender = gender;
    }


    /**
     * Gets the birthday value for this TempReportInfo.
     * 
     * @return birthday
     */
    public java.util.Calendar getBirthday() {
        return birthday;
    }


    /**
     * Sets the birthday value for this TempReportInfo.
     * 
     * @param birthday
     */
    public void setBirthday(java.util.Calendar birthday) {
        this.birthday = birthday;
    }


    /**
     * Gets the cardNo value for this TempReportInfo.
     * 
     * @return cardNo
     */
    public java.lang.String getCardNo() {
        return cardNo;
    }


    /**
     * Sets the cardNo value for this TempReportInfo.
     * 
     * @param cardNo
     */
    public void setCardNo(java.lang.String cardNo) {
        this.cardNo = cardNo;
    }


    /**
     * Gets the mobile value for this TempReportInfo.
     * 
     * @return mobile
     */
    public java.lang.String getMobile() {
        return mobile;
    }


    /**
     * Sets the mobile value for this TempReportInfo.
     * 
     * @param mobile
     */
    public void setMobile(java.lang.String mobile) {
        this.mobile = mobile;
    }


    /**
     * Gets the telephone value for this TempReportInfo.
     * 
     * @return telephone
     */
    public java.lang.String getTelephone() {
        return telephone;
    }


    /**
     * Sets the telephone value for this TempReportInfo.
     * 
     * @param telephone
     */
    public void setTelephone(java.lang.String telephone) {
        this.telephone = telephone;
    }


    /**
     * Gets the email value for this TempReportInfo.
     * 
     * @return email
     */
    public java.lang.String getEmail() {
        return email;
    }


    /**
     * Sets the email value for this TempReportInfo.
     * 
     * @param email
     */
    public void setEmail(java.lang.String email) {
        this.email = email;
    }


    /**
     * Gets the workUnit value for this TempReportInfo.
     * 
     * @return workUnit
     */
    public java.lang.String getWorkUnit() {
        return workUnit;
    }


    /**
     * Sets the workUnit value for this TempReportInfo.
     * 
     * @param workUnit
     */
    public void setWorkUnit(java.lang.String workUnit) {
        this.workUnit = workUnit;
    }


    /**
     * Gets the addresss value for this TempReportInfo.
     * 
     * @return addresss
     */
    public java.lang.String getAddresss() {
        return addresss;
    }


    /**
     * Sets the addresss value for this TempReportInfo.
     * 
     * @param addresss
     */
    public void setAddresss(java.lang.String addresss) {
        this.addresss = addresss;
    }


    /**
     * Gets the loginName value for this TempReportInfo.
     * 
     * @return loginName
     */
    public java.lang.String getLoginName() {
        return loginName;
    }


    /**
     * Sets the loginName value for this TempReportInfo.
     * 
     * @param loginName
     */
    public void setLoginName(java.lang.String loginName) {
        this.loginName = loginName;
    }


    /**
     * Gets the loginPwd value for this TempReportInfo.
     * 
     * @return loginPwd
     */
    public java.lang.String getLoginPwd() {
        return loginPwd;
    }


    /**
     * Sets the loginPwd value for this TempReportInfo.
     * 
     * @param loginPwd
     */
    public void setLoginPwd(java.lang.String loginPwd) {
        this.loginPwd = loginPwd;
    }


    /**
     * Gets the summarize value for this TempReportInfo.
     * 
     * @return summarize
     */
    public java.lang.String getSummarize() {
        return summarize;
    }


    /**
     * Sets the summarize value for this TempReportInfo.
     * 
     * @param summarize
     */
    public void setSummarize(java.lang.String summarize) {
        this.summarize = summarize;
    }


    /**
     * Gets the advice value for this TempReportInfo.
     * 
     * @return advice
     */
    public java.lang.String getAdvice() {
        return advice;
    }


    /**
     * Sets the advice value for this TempReportInfo.
     * 
     * @param advice
     */
    public void setAdvice(java.lang.String advice) {
        this.advice = advice;
    }


    /**
     * Gets the chargesItem value for this TempReportInfo.
     * 
     * @return chargesItem
     */
    public java.lang.String getChargesItem() {
        return chargesItem;
    }


    /**
     * Sets the chargesItem value for this TempReportInfo.
     * 
     * @param chargesItem
     */
    public void setChargesItem(java.lang.String chargesItem) {
        this.chargesItem = chargesItem;
    }


    /**
     * Gets the ZJDoctor value for this TempReportInfo.
     * 
     * @return ZJDoctor
     */
    public java.lang.String getZJDoctor() {
        return ZJDoctor;
    }


    /**
     * Sets the ZJDoctor value for this TempReportInfo.
     * 
     * @param ZJDoctor
     */
    public void setZJDoctor(java.lang.String ZJDoctor) {
        this.ZJDoctor = ZJDoctor;
    }


    /**
     * Gets the ZJDate value for this TempReportInfo.
     * 
     * @return ZJDate
     */
    public java.util.Calendar getZJDate() {
        return ZJDate;
    }


    /**
     * Sets the ZJDate value for this TempReportInfo.
     * 
     * @param ZJDate
     */
    public void setZJDate(java.util.Calendar ZJDate) {
        this.ZJDate = ZJDate;
    }


    /**
     * Gets the bakField1 value for this TempReportInfo.
     * 
     * @return bakField1
     */
    public java.lang.String getBakField1() {
        return bakField1;
    }


    /**
     * Sets the bakField1 value for this TempReportInfo.
     * 
     * @param bakField1
     */
    public void setBakField1(java.lang.String bakField1) {
        this.bakField1 = bakField1;
    }


    /**
     * Gets the bakField2 value for this TempReportInfo.
     * 
     * @return bakField2
     */
    public java.lang.String getBakField2() {
        return bakField2;
    }


    /**
     * Sets the bakField2 value for this TempReportInfo.
     * 
     * @param bakField2
     */
    public void setBakField2(java.lang.String bakField2) {
        this.bakField2 = bakField2;
    }


    /**
     * Gets the bakField3 value for this TempReportInfo.
     * 
     * @return bakField3
     */
    public java.lang.String getBakField3() {
        return bakField3;
    }


    /**
     * Sets the bakField3 value for this TempReportInfo.
     * 
     * @param bakField3
     */
    public void setBakField3(java.lang.String bakField3) {
        this.bakField3 = bakField3;
    }


    /**
     * Gets the bakField4 value for this TempReportInfo.
     * 
     * @return bakField4
     */
    public java.lang.String getBakField4() {
        return bakField4;
    }


    /**
     * Sets the bakField4 value for this TempReportInfo.
     * 
     * @param bakField4
     */
    public void setBakField4(java.lang.String bakField4) {
        this.bakField4 = bakField4;
    }


    /**
     * Gets the bakField5 value for this TempReportInfo.
     * 
     * @return bakField5
     */
    public java.lang.String getBakField5() {
        return bakField5;
    }


    /**
     * Sets the bakField5 value for this TempReportInfo.
     * 
     * @param bakField5
     */
    public void setBakField5(java.lang.String bakField5) {
        this.bakField5 = bakField5;
    }


    /**
     * Gets the reportAbnormalS value for this TempReportInfo.
     * 
     * @return reportAbnormalS
     */
    public com.hjw.webService.client.tj180.client.TempReportAbnormal[] getReportAbnormalS() {
        return reportAbnormalS;
    }


    /**
     * Sets the reportAbnormalS value for this TempReportInfo.
     * 
     * @param reportAbnormalS
     */
    public void setReportAbnormalS(com.hjw.webService.client.tj180.client.TempReportAbnormal[] reportAbnormalS) {
        this.reportAbnormalS = reportAbnormalS;
    }


    /**
     * Gets the reportConclusionS value for this TempReportInfo.
     * 
     * @return reportConclusionS
     */
    public com.hjw.webService.client.tj180.client.TempReportConclusion[] getReportConclusionS() {
        return reportConclusionS;
    }


    /**
     * Sets the reportConclusionS value for this TempReportInfo.
     * 
     * @param reportConclusionS
     */
    public void setReportConclusionS(com.hjw.webService.client.tj180.client.TempReportConclusion[] reportConclusionS) {
        this.reportConclusionS = reportConclusionS;
    }


    /**
     * Gets the reportItemS value for this TempReportInfo.
     * 
     * @return reportItemS
     */
    public com.hjw.webService.client.tj180.client.TempReportItem[] getReportItemS() {
        return reportItemS;
    }


    /**
     * Sets the reportItemS value for this TempReportInfo.
     * 
     * @param reportItemS
     */
    public void setReportItemS(com.hjw.webService.client.tj180.client.TempReportItem[] reportItemS) {
        this.reportItemS = reportItemS;
    }


    /**
     * Gets the isRepeat value for this TempReportInfo.
     * 
     * @return isRepeat
     */
    public int getIsRepeat() {
        return isRepeat;
    }


    /**
     * Sets the isRepeat value for this TempReportInfo.
     * 
     * @param isRepeat
     */
    public void setIsRepeat(int isRepeat) {
        this.isRepeat = isRepeat;
    }


    /**
     * Gets the isUpdate value for this TempReportInfo.
     * 
     * @return isUpdate
     */
    public int getIsUpdate() {
        return isUpdate;
    }


    /**
     * Sets the isUpdate value for this TempReportInfo.
     * 
     * @param isUpdate
     */
    public void setIsUpdate(int isUpdate) {
        this.isUpdate = isUpdate;
    }


    /**
     * Gets the isUpdateReport value for this TempReportInfo.
     * 
     * @return isUpdateReport
     */
    public int getIsUpdateReport() {
        return isUpdateReport;
    }


    /**
     * Sets the isUpdateReport value for this TempReportInfo.
     * 
     * @param isUpdateReport
     */
    public void setIsUpdateReport(int isUpdateReport) {
        this.isUpdateReport = isUpdateReport;
    }

    private java.lang.Object __equalsCalc = null;
    public synchronized boolean equals(java.lang.Object obj) {
        if (!(obj instanceof TempReportInfo)) return false;
        TempReportInfo other = (TempReportInfo) obj;
        if (obj == null) return false;
        if (this == obj) return true;
        if (__equalsCalc != null) {
            return (__equalsCalc == obj);
        }
        __equalsCalc = obj;
        boolean _equals;
        _equals = true && 
            ((this.healthConsultant==null && other.getHealthConsultant()==null) || 
             (this.healthConsultant!=null &&
              java.util.Arrays.equals(this.healthConsultant, other.getHealthConsultant()))) &&
            ((this.libraryId==null && other.getLibraryId()==null) || 
             (this.libraryId!=null &&
              this.libraryId.equals(other.getLibraryId()))) &&
            ((this.gradeID==null && other.getGradeID()==null) || 
             (this.gradeID!=null &&
              this.gradeID.equals(other.getGradeID()))) &&
            ((this.organID==null && other.getOrganID()==null) || 
             (this.organID!=null &&
              this.organID.equals(other.getOrganID()))) &&
            ((this.reportNo==null && other.getReportNo()==null) || 
             (this.reportNo!=null &&
              this.reportNo.equals(other.getReportNo()))) &&
            ((this.reportDate==null && other.getReportDate()==null) || 
             (this.reportDate!=null &&
              this.reportDate.equals(other.getReportDate()))) &&
            ((this.reportState==null && other.getReportState()==null) || 
             (this.reportState!=null &&
              this.reportState.equals(other.getReportState()))) &&
            ((this.userName==null && other.getUserName()==null) || 
             (this.userName!=null &&
              this.userName.equals(other.getUserName()))) &&
            ((this.userNo==null && other.getUserNo()==null) || 
             (this.userNo!=null &&
              this.userNo.equals(other.getUserNo()))) &&
            ((this.gender==null && other.getGender()==null) || 
             (this.gender!=null &&
              this.gender.equals(other.getGender()))) &&
            ((this.birthday==null && other.getBirthday()==null) || 
             (this.birthday!=null &&
              this.birthday.equals(other.getBirthday()))) &&
            ((this.cardNo==null && other.getCardNo()==null) || 
             (this.cardNo!=null &&
              this.cardNo.equals(other.getCardNo()))) &&
            ((this.mobile==null && other.getMobile()==null) || 
             (this.mobile!=null &&
              this.mobile.equals(other.getMobile()))) &&
            ((this.telephone==null && other.getTelephone()==null) || 
             (this.telephone!=null &&
              this.telephone.equals(other.getTelephone()))) &&
            ((this.email==null && other.getEmail()==null) || 
             (this.email!=null &&
              this.email.equals(other.getEmail()))) &&
            ((this.workUnit==null && other.getWorkUnit()==null) || 
             (this.workUnit!=null &&
              this.workUnit.equals(other.getWorkUnit()))) &&
            ((this.addresss==null && other.getAddresss()==null) || 
             (this.addresss!=null &&
              this.addresss.equals(other.getAddresss()))) &&
            ((this.loginName==null && other.getLoginName()==null) || 
             (this.loginName!=null &&
              this.loginName.equals(other.getLoginName()))) &&
            ((this.loginPwd==null && other.getLoginPwd()==null) || 
             (this.loginPwd!=null &&
              this.loginPwd.equals(other.getLoginPwd()))) &&
            ((this.summarize==null && other.getSummarize()==null) || 
             (this.summarize!=null &&
              this.summarize.equals(other.getSummarize()))) &&
            ((this.advice==null && other.getAdvice()==null) || 
             (this.advice!=null &&
              this.advice.equals(other.getAdvice()))) &&
            ((this.chargesItem==null && other.getChargesItem()==null) || 
             (this.chargesItem!=null &&
              this.chargesItem.equals(other.getChargesItem()))) &&
            ((this.ZJDoctor==null && other.getZJDoctor()==null) || 
             (this.ZJDoctor!=null &&
              this.ZJDoctor.equals(other.getZJDoctor()))) &&
            ((this.ZJDate==null && other.getZJDate()==null) || 
             (this.ZJDate!=null &&
              this.ZJDate.equals(other.getZJDate()))) &&
            ((this.bakField1==null && other.getBakField1()==null) || 
             (this.bakField1!=null &&
              this.bakField1.equals(other.getBakField1()))) &&
            ((this.bakField2==null && other.getBakField2()==null) || 
             (this.bakField2!=null &&
              this.bakField2.equals(other.getBakField2()))) &&
            ((this.bakField3==null && other.getBakField3()==null) || 
             (this.bakField3!=null &&
              this.bakField3.equals(other.getBakField3()))) &&
            ((this.bakField4==null && other.getBakField4()==null) || 
             (this.bakField4!=null &&
              this.bakField4.equals(other.getBakField4()))) &&
            ((this.bakField5==null && other.getBakField5()==null) || 
             (this.bakField5!=null &&
              this.bakField5.equals(other.getBakField5()))) &&
            ((this.reportAbnormalS==null && other.getReportAbnormalS()==null) || 
             (this.reportAbnormalS!=null &&
              java.util.Arrays.equals(this.reportAbnormalS, other.getReportAbnormalS()))) &&
            ((this.reportConclusionS==null && other.getReportConclusionS()==null) || 
             (this.reportConclusionS!=null &&
              java.util.Arrays.equals(this.reportConclusionS, other.getReportConclusionS()))) &&
            ((this.reportItemS==null && other.getReportItemS()==null) || 
             (this.reportItemS!=null &&
              java.util.Arrays.equals(this.reportItemS, other.getReportItemS()))) &&
            this.isRepeat == other.getIsRepeat() &&
            this.isUpdate == other.getIsUpdate() &&
            this.isUpdateReport == other.getIsUpdateReport();
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
        if (getHealthConsultant() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getHealthConsultant());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getHealthConsultant(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getLibraryId() != null) {
            _hashCode += getLibraryId().hashCode();
        }
        if (getGradeID() != null) {
            _hashCode += getGradeID().hashCode();
        }
        if (getOrganID() != null) {
            _hashCode += getOrganID().hashCode();
        }
        if (getReportNo() != null) {
            _hashCode += getReportNo().hashCode();
        }
        if (getReportDate() != null) {
            _hashCode += getReportDate().hashCode();
        }
        if (getReportState() != null) {
            _hashCode += getReportState().hashCode();
        }
        if (getUserName() != null) {
            _hashCode += getUserName().hashCode();
        }
        if (getUserNo() != null) {
            _hashCode += getUserNo().hashCode();
        }
        if (getGender() != null) {
            _hashCode += getGender().hashCode();
        }
        if (getBirthday() != null) {
            _hashCode += getBirthday().hashCode();
        }
        if (getCardNo() != null) {
            _hashCode += getCardNo().hashCode();
        }
        if (getMobile() != null) {
            _hashCode += getMobile().hashCode();
        }
        if (getTelephone() != null) {
            _hashCode += getTelephone().hashCode();
        }
        if (getEmail() != null) {
            _hashCode += getEmail().hashCode();
        }
        if (getWorkUnit() != null) {
            _hashCode += getWorkUnit().hashCode();
        }
        if (getAddresss() != null) {
            _hashCode += getAddresss().hashCode();
        }
        if (getLoginName() != null) {
            _hashCode += getLoginName().hashCode();
        }
        if (getLoginPwd() != null) {
            _hashCode += getLoginPwd().hashCode();
        }
        if (getSummarize() != null) {
            _hashCode += getSummarize().hashCode();
        }
        if (getAdvice() != null) {
            _hashCode += getAdvice().hashCode();
        }
        if (getChargesItem() != null) {
            _hashCode += getChargesItem().hashCode();
        }
        if (getZJDoctor() != null) {
            _hashCode += getZJDoctor().hashCode();
        }
        if (getZJDate() != null) {
            _hashCode += getZJDate().hashCode();
        }
        if (getBakField1() != null) {
            _hashCode += getBakField1().hashCode();
        }
        if (getBakField2() != null) {
            _hashCode += getBakField2().hashCode();
        }
        if (getBakField3() != null) {
            _hashCode += getBakField3().hashCode();
        }
        if (getBakField4() != null) {
            _hashCode += getBakField4().hashCode();
        }
        if (getBakField5() != null) {
            _hashCode += getBakField5().hashCode();
        }
        if (getReportAbnormalS() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getReportAbnormalS());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getReportAbnormalS(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getReportConclusionS() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getReportConclusionS());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getReportConclusionS(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        if (getReportItemS() != null) {
            for (int i=0;
                 i<java.lang.reflect.Array.getLength(getReportItemS());
                 i++) {
                java.lang.Object obj = java.lang.reflect.Array.get(getReportItemS(), i);
                if (obj != null &&
                    !obj.getClass().isArray()) {
                    _hashCode += obj.hashCode();
                }
            }
        }
        _hashCode += getIsRepeat();
        _hashCode += getIsUpdate();
        _hashCode += getIsUpdateReport();
        __hashCodeCalc = false;
        return _hashCode;
    }

    // Type metadata
    private static org.apache.axis.description.TypeDesc typeDesc =
        new org.apache.axis.description.TypeDesc(TempReportInfo.class, true);

    static {
        typeDesc.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", "TempReportInfo"));
        org.apache.axis.description.ElementDesc elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("healthConsultant");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "HealthConsultant"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://tempuri.org/", "string"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("libraryId");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "LibraryId"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("gradeID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "GradeID"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("organID");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "OrganID"));
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
        elemField.setFieldName("reportDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "ReportDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reportState");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "ReportState"));
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
        elemField.setFieldName("userNo");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "UserNo"));
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
        elemField.setFieldName("birthday");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "Birthday"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
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
        elemField.setFieldName("telephone");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "Telephone"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("email");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "Email"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("workUnit");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "WorkUnit"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("addresss");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "Addresss"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("loginName");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "LoginName"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("loginPwd");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "LoginPwd"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("summarize");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "Summarize"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("advice");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "Advice"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("chargesItem");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "ChargesItem"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ZJDoctor");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "ZJDoctor"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("ZJDate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "ZJDate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "dateTime"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bakField1");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "BakField1"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bakField2");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "BakField2"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bakField3");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "BakField3"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bakField4");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "BakField4"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("bakField5");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "BakField5"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "string"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reportAbnormalS");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "ReportAbnormalS"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", "TempReportAbnormal"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://tempuri.org/", "TempReportAbnormal"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reportConclusionS");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "ReportConclusionS"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", "TempReportConclusion"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://tempuri.org/", "TempReportConclusion"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("reportItemS");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "ReportItemS"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://tempuri.org/", "TempReportItem"));
        elemField.setMinOccurs(0);
        elemField.setNillable(false);
        elemField.setItemQName(new javax.xml.namespace.QName("http://tempuri.org/", "TempReportItem"));
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isRepeat");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "IsRepeat"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isUpdate");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "IsUpdate"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
        elemField.setNillable(false);
        typeDesc.addFieldDesc(elemField);
        elemField = new org.apache.axis.description.ElementDesc();
        elemField.setFieldName("isUpdateReport");
        elemField.setXmlName(new javax.xml.namespace.QName("http://tempuri.org/", "IsUpdateReport"));
        elemField.setXmlType(new javax.xml.namespace.QName("http://www.w3.org/2001/XMLSchema", "int"));
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
