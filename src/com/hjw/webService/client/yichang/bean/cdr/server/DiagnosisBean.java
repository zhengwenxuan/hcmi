package com.hjw.webService.client.yichang.bean.cdr.server;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "diseaseDiagnosisList")  
@XmlType(propOrder = {})
public class DiagnosisBean {

	@XmlElement
	private String	diagnosisDeptCode	 = "";//	诊断科室编码	
	@XmlElement
	private String	diagnosisDeptName	 = "";//	诊断科室名称	
	@XmlElement
	private String	diagnosisDoctorCode	 = "";//	诊断医生编码	
	@XmlElement
	private String	diagnosisDoctorName	 = "";//	诊断医生姓名	
	@XmlElement
	private String	diagnosisTypeCode	 = "";//	诊断类别代码	是
	@XmlElement
	private String	diagnosisTypeName	 = "";//	诊断类别名称	是
	@XmlElement
	private String	diagnosisDate	 = "";//	诊断日期	
	@XmlElement
	private String	diagnosisSequence	 = "";//	诊断顺位（从属关系）	
	@XmlElement
	private String	diseaseCode	 = "";//	疾病代码	
	@XmlElement
	private String	diseaseName	 = "";//	疾病名称	
	@XmlElement
	private String	diagnosisBasisName	 = "";//	诊断依据名称	
	@XmlElement
	private String	diagnosisBasisCode	 = "";//	诊断依据代码	
	@XmlElement
	private String	mainDiagnosisFlag	 = "";//	是否主要诊断	
	@XmlElement
	private String	chineseMedicineDiseaseName	 = "";//	中医病名名称	
	@XmlElement
	private String	chineseMedicineDiseaseCode	 = "";//	中医病名代码	
	@XmlElement
	private String	chineseMedicineSymptomName	 = "";//	中医证候名称	
	@XmlElement
	private String	chineseMedicineSymptomCode	 = "";//	中医证候代码	
	@XmlElement
	private String	chineseMedicineDiagnosisFlag	 = "";//	是否是中医诊断	
	
	public String getDiagnosisDeptCode() {
		return diagnosisDeptCode;
	}
	public String getDiagnosisDeptName() {
		return diagnosisDeptName;
	}
	public String getDiagnosisDoctorCode() {
		return diagnosisDoctorCode;
	}
	public String getDiagnosisDoctorName() {
		return diagnosisDoctorName;
	}
	public String getDiagnosisTypeCode() {
		return diagnosisTypeCode;
	}
	public String getDiagnosisTypeName() {
		return diagnosisTypeName;
	}
	public String getDiagnosisDate() {
		return diagnosisDate;
	}
	public String getDiagnosisSequence() {
		return diagnosisSequence;
	}
	public String getDiseaseCode() {
		return diseaseCode;
	}
	public String getDiseaseName() {
		return diseaseName;
	}
	public String getDiagnosisBasisName() {
		return diagnosisBasisName;
	}
	public String getDiagnosisBasisCode() {
		return diagnosisBasisCode;
	}
	public String getMainDiagnosisFlag() {
		return mainDiagnosisFlag;
	}
	public String getChineseMedicineDiseaseName() {
		return chineseMedicineDiseaseName;
	}
	public String getChineseMedicineDiseaseCode() {
		return chineseMedicineDiseaseCode;
	}
	public String getChineseMedicineSymptomName() {
		return chineseMedicineSymptomName;
	}
	public String getChineseMedicineSymptomCode() {
		return chineseMedicineSymptomCode;
	}
	public String getChineseMedicineDiagnosisFlag() {
		return chineseMedicineDiagnosisFlag;
	}
	public void setDiagnosisDeptCode(String diagnosisDeptCode) {
		this.diagnosisDeptCode = diagnosisDeptCode;
	}
	public void setDiagnosisDeptName(String diagnosisDeptName) {
		this.diagnosisDeptName = diagnosisDeptName;
	}
	public void setDiagnosisDoctorCode(String diagnosisDoctorCode) {
		this.diagnosisDoctorCode = diagnosisDoctorCode;
	}
	public void setDiagnosisDoctorName(String diagnosisDoctorName) {
		this.diagnosisDoctorName = diagnosisDoctorName;
	}
	public void setDiagnosisTypeCode(String diagnosisTypeCode) {
		this.diagnosisTypeCode = diagnosisTypeCode;
	}
	public void setDiagnosisTypeName(String diagnosisTypeName) {
		this.diagnosisTypeName = diagnosisTypeName;
	}
	public void setDiagnosisDate(String diagnosisDate) {
		this.diagnosisDate = diagnosisDate;
	}
	public void setDiagnosisSequence(String diagnosisSequence) {
		this.diagnosisSequence = diagnosisSequence;
	}
	public void setDiseaseCode(String diseaseCode) {
		this.diseaseCode = diseaseCode;
	}
	public void setDiseaseName(String diseaseName) {
		this.diseaseName = diseaseName;
	}
	public void setDiagnosisBasisName(String diagnosisBasisName) {
		this.diagnosisBasisName = diagnosisBasisName;
	}
	public void setDiagnosisBasisCode(String diagnosisBasisCode) {
		this.diagnosisBasisCode = diagnosisBasisCode;
	}
	public void setMainDiagnosisFlag(String mainDiagnosisFlag) {
		this.mainDiagnosisFlag = mainDiagnosisFlag;
	}
	public void setChineseMedicineDiseaseName(String chineseMedicineDiseaseName) {
		this.chineseMedicineDiseaseName = chineseMedicineDiseaseName;
	}
	public void setChineseMedicineDiseaseCode(String chineseMedicineDiseaseCode) {
		this.chineseMedicineDiseaseCode = chineseMedicineDiseaseCode;
	}
	public void setChineseMedicineSymptomName(String chineseMedicineSymptomName) {
		this.chineseMedicineSymptomName = chineseMedicineSymptomName;
	}
	public void setChineseMedicineSymptomCode(String chineseMedicineSymptomCode) {
		this.chineseMedicineSymptomCode = chineseMedicineSymptomCode;
	}
	public void setChineseMedicineDiagnosisFlag(String chineseMedicineDiagnosisFlag) {
		this.chineseMedicineDiagnosisFlag = chineseMedicineDiagnosisFlag;
	}
}
