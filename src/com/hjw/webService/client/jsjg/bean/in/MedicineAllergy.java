package com.hjw.webService.client.jsjg.bean.in;

public class MedicineAllergy {

	private String SortNo = "";//药敏序号
	private String MedicineCode = "";//药品编码
	private String MedicineName = "";//药品名称
	private String MedicineAllergyResult = "";//药敏结果
	private String MIC = "";//抑菌浓度
	private String YJHZJ = "";//抑菌环直径
	
	public String getSortNo() {
		return SortNo;
	}
	public String getMedicineCode() {
		return MedicineCode;
	}
	public String getMedicineName() {
		return MedicineName;
	}
	public String getMedicineAllergyResult() {
		return MedicineAllergyResult;
	}
	public String getMIC() {
		return MIC;
	}
	public String getYJHZJ() {
		return YJHZJ;
	}
	public void setSortNo(String sortNo) {
		SortNo = sortNo;
	}
	public void setMedicineCode(String medicineCode) {
		MedicineCode = medicineCode;
	}
	public void setMedicineName(String medicineName) {
		MedicineName = medicineName;
	}
	public void setMedicineAllergyResult(String medicineAllergyResult) {
		MedicineAllergyResult = medicineAllergyResult;
	}
	public void setMIC(String mIC) {
		MIC = mIC;
	}
	public void setYJHZJ(String yJHZJ) {
		YJHZJ = yJHZJ;
	}
}
