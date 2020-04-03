package com.hjw.webService.client.jsjg.bean.in;

public class Result {

	private String ResultID = "";//结果ID
	private String GermFlag = "";//细菌标志	0=非细菌报告，1=细菌阴性报告，2=细菌阳性报告
	private String ReviewFlag = "";//复查标志
	private String ReportItemID = "";//报告项目ID
	private String ReportItemCode = "";//报告项目代码
	private String ReportItemName = "";//报告项目名称
	private String ReportCellType = "";//报告单元业务类型	10＝通用，20＝细菌，30＝图文，40=骨髓，90＝血库
	private String ReportCellName = "";//报告单元名称
	private String RportType = "";//报告项目所属类别代码
	private String ReportMethod = "";//报告项目方法学
	private String PrintReportCode = "";//报告项目打印代码【多为英文缩写，显示在报告单上】
	private String BigTextResult = "";//大文本结果	源result_txt
	private String ItemResult = "";//检验结果	源result_str
	private String Unit = "";//单位
	private String ResultType = "";//结果类型	值域参见<主数据集成规范>字典编码为[LIS_REPORT_RESULT_TYPE]的数据	例如，数字，阴阳等枚举值
	private String Reference = "";//参考范围
	private String CheckMachineID = "";//检测仪器ID（为空表示手工录入或计算项目）
	private String QuantitativeResult = "";//疾病标识
	private String Remark = "";//读取标识
	private String AbnormalFlag = "";//异常标志	源result_flag字段	H偏高、L偏低、P阳性、Q弱阳性、E错误
	private String IsPanic = "";//危急标志	0 常规报告 	1危急值报告
	private String ReportMeno = "";//报告备注	源report_comm
	private String LabAdvice = "";//实验室意见	源lab_advice
	private String SortNo = "";//细菌序号
	private String ItemCode = "";//细菌代码
	private String ItemName = "";//细菌名称
//	private String ItemResult = "";//细菌培养结果
	private String ColonyCounting = "";//菌落计数
	private String FloraProportion = "";//菌群比例
	private String FieldCustom1 = "";//预留字段1
	private String FieldCustom2 = "";//预留字段2
	private MedicineAllergys MedicineAllergys = new MedicineAllergys();//预留字段2
	
	public String getResultID() {
		return ResultID;
	}
	public String getGermFlag() {
		return GermFlag;
	}
	public String getReviewFlag() {
		return ReviewFlag;
	}
	public String getReportItemID() {
		return ReportItemID;
	}
	public String getReportItemCode() {
		return ReportItemCode;
	}
	public String getReportItemName() {
		return ReportItemName;
	}
	public String getReportCellType() {
		return ReportCellType;
	}
	public String getReportCellName() {
		return ReportCellName;
	}
	public String getRportType() {
		return RportType;
	}
	public String getReportMethod() {
		return ReportMethod;
	}
	public String getPrintReportCode() {
		return PrintReportCode;
	}
	public String getBigTextResult() {
		return BigTextResult;
	}
	public String getItemResult() {
		return ItemResult;
	}
	public String getUnit() {
		return Unit;
	}
	public String getResultType() {
		return ResultType;
	}
	public String getReference() {
		return Reference;
	}
	public String getCheckMachineID() {
		return CheckMachineID;
	}
	public String getQuantitativeResult() {
		return QuantitativeResult;
	}
	public String getRemark() {
		return Remark;
	}
	public String getAbnormalFlag() {
		return AbnormalFlag;
	}
	public String getIsPanic() {
		return IsPanic;
	}
	public String getReportMeno() {
		return ReportMeno;
	}
	public String getLabAdvice() {
		return LabAdvice;
	}
	public void setResultID(String resultID) {
		ResultID = resultID;
	}
	public void setGermFlag(String germFlag) {
		GermFlag = germFlag;
	}
	public void setReviewFlag(String reviewFlag) {
		ReviewFlag = reviewFlag;
	}
	public void setReportItemID(String reportItemID) {
		ReportItemID = reportItemID;
	}
	public void setReportItemCode(String reportItemCode) {
		ReportItemCode = reportItemCode;
	}
	public void setReportItemName(String reportItemName) {
		ReportItemName = reportItemName;
	}
	public void setReportCellType(String reportCellType) {
		ReportCellType = reportCellType;
	}
	public void setReportCellName(String reportCellName) {
		ReportCellName = reportCellName;
	}
	public void setRportType(String rportType) {
		RportType = rportType;
	}
	public void setReportMethod(String reportMethod) {
		ReportMethod = reportMethod;
	}
	public void setPrintReportCode(String printReportCode) {
		PrintReportCode = printReportCode;
	}
	public void setBigTextResult(String bigTextResult) {
		BigTextResult = bigTextResult;
	}
	public void setItemResult(String itemResult) {
		ItemResult = itemResult;
	}
	public void setUnit(String unit) {
		Unit = unit;
	}
	public void setResultType(String resultType) {
		ResultType = resultType;
	}
	public void setReference(String reference) {
		Reference = reference;
	}
	public void setCheckMachineID(String checkMachineID) {
		CheckMachineID = checkMachineID;
	}
	public void setQuantitativeResult(String quantitativeResult) {
		QuantitativeResult = quantitativeResult;
	}
	public void setRemark(String remark) {
		Remark = remark;
	}
	public void setAbnormalFlag(String abnormalFlag) {
		AbnormalFlag = abnormalFlag;
	}
	public void setIsPanic(String isPanic) {
		IsPanic = isPanic;
	}
	public void setReportMeno(String reportMeno) {
		ReportMeno = reportMeno;
	}
	public void setLabAdvice(String labAdvice) {
		LabAdvice = labAdvice;
	}
	public String getSortNo() {
		return SortNo;
	}
	public String getItemCode() {
		return ItemCode;
	}
	public String getItemName() {
		return ItemName;
	}
	public String getColonyCounting() {
		return ColonyCounting;
	}
	public String getFloraProportion() {
		return FloraProportion;
	}
	public String getFieldCustom1() {
		return FieldCustom1;
	}
	public String getFieldCustom2() {
		return FieldCustom2;
	}
	public MedicineAllergys getMedicineAllergys() {
		return MedicineAllergys;
	}
	public void setSortNo(String sortNo) {
		SortNo = sortNo;
	}
	public void setItemCode(String itemCode) {
		ItemCode = itemCode;
	}
	public void setItemName(String itemName) {
		ItemName = itemName;
	}
	public void setColonyCounting(String colonyCounting) {
		ColonyCounting = colonyCounting;
	}
	public void setFloraProportion(String floraProportion) {
		FloraProportion = floraProportion;
	}
	public void setFieldCustom1(String fieldCustom1) {
		FieldCustom1 = fieldCustom1;
	}
	public void setFieldCustom2(String fieldCustom2) {
		FieldCustom2 = fieldCustom2;
	}
	public void setMedicineAllergys(MedicineAllergys medicineAllergys) {
		MedicineAllergys = medicineAllergys;
	}
}
