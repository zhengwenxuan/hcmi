package com.hjw.webService.client.sxwn.Bean;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "LISReport")  
@XmlType(propOrder = {})  
public class LISReport {
	@XmlElement
	private String ID; //  化验单编码
	@XmlElement
    private String HZBM;//患者编码
	@XmlElement
    private int xb;//患者性别  1 男；2女；0 未知
	@XmlElement
    private String HZLX;//患者类型
	@XmlElement
    private String BBLX;//电解质</BBLX> 报告类型
	@XmlElement
    private int XMBH;////>1000078</XMBH>结果项目编码
	@XmlElement
    private String BBLXBM;//>10007</BBLXBM>报告类型编码
	@XmlElement
    private String KS;// 科室
	@XmlElement
    private String LCZD;//临床诊断
	@XmlElement
    private int BBH=1;//>1</BBH> 标本号
	@XmlElement
    private String BB;//>血清</BB> 标本名称
	@XmlElement
    private String BBYZ;//标本样状
	@XmlElement
    private String kdr;//开单人
	@XmlElement
    private int KdrNum;//>0</KdrNum> 开单人编码
	@XmlElement
    private String cbr="系统管理员";//>系统管理员</cbr>   采标人
	@XmlElement
    private int CbrNum;//采标人编码
	@XmlElement
    private String jbr="系统管理员";//</jbr> 接标人
	@XmlElement
    private int jbrNum;//接标人编码
	@XmlElement
    private String jyr="系统管理员";//</jyr> 检验人
	@XmlElement
    private int JyrNum;//</JyrNum>  检验人编码
	@XmlElement
    private String hdr="系统管理员";//</hdr>  核对人
	@XmlElement
    private int HdrNum;//>1</HdrNum> 核对人编码
	@XmlElement
    private String JGMS;//结果描述
	@XmlElement
    private String kdsj="";//2015-01-20T10:24:00+08:00</kdsj>  开始时间
	@XmlElement
    private String cbsj="";//2015-01-20T10:24:00+08:00</cbsj>  采标时间
	@XmlElement
    private String jbsj="";//2015-01-20T10:24:00+08:00</jbsj>    接标时间
	@XmlElement
    private String jysj="";//2015-01-20T10:24:00+08:00</jysj>   检验时间
	@XmlElement
    private String cjgsj="";//2015-01-20T10:24:50+08:00</cjgsj> 报告时间
	@XmlElement
    private String XMMC="";//>钾</XMMC>     项目名称
	@XmlElement
    private String JG="";// 项目结果
	@XmlElement
    private String DW="";//>mmol/L</DW>  项目单位
	@XmlElement
    private String CKFW="";//>3.50--5.50</CKFW> 项目参考范围
	@XmlElement
    private String SYFF="";//      试验方法
	@XmlElement
    private String Flag="";//>↑</Flag> 检验结果标记
	@XmlElement
    private String YQMC="";//>HK2003(665)电解质分析仪</YQMC>  仪器名称
	@XmlElement
    private int JGXH;//  结果序号
	
	public String getID() {
		return ID;
	}
	public void setID(String iD) {
		ID = iD;
	}
	public String getHZBM() {
		return HZBM;
	}
	public void setHZBM(String hZBM) {
		HZBM = hZBM;
	}
	public int getXb() {
		return xb;
	}
	public void setXb(int xb) {
		this.xb = xb;
	}
	public String getHZLX() {
		return HZLX;
	}
	public void setHZLX(String hZLX) {
		HZLX = hZLX;
	}
	public String getBBLX() {
		return BBLX;
	}
	public void setBBLX(String bBLX) {
		BBLX = bBLX;
	}
	public int getXMBH() {
		return XMBH;
	}
	public void setXMBH(int xMBH) {
		XMBH = xMBH;
	}
	public String getBBLXBM() {
		return BBLXBM;
	}
	public void setBBLXBM(String bBLXBM) {
		BBLXBM = bBLXBM;
	}
	public String getKS() {
		return KS;
	}
	public void setKS(String kS) {
		KS = kS;
	}
	public String getLCZD() {
		return LCZD;
	}
	public void setLCZD(String lCZD) {
		LCZD = lCZD;
	}
	public int getBBH() {
		return BBH;
	}
	public void setBBH(int bBH) {
		BBH = bBH;
	}
	public String getBB() {
		return BB;
	}
	public void setBB(String bB) {
		BB = bB;
	}
	public String getBBYZ() {
		return BBYZ;
	}
	public void setBBYZ(String bBYZ) {
		BBYZ = bBYZ;
	}
	public String getKdr() {
		return kdr;
	}
	public void setKdr(String kdr) {
		this.kdr = kdr;
	}
	public int getKdrNum() {
		return KdrNum;
	}
	public void setKdrNum(int kdrNum) {
		KdrNum = kdrNum;
	}
	public String getCbr() {
		return cbr;
	}
	public void setCbr(String cbr) {
		this.cbr = cbr;
	}
	public int getCbrNum() {
		return CbrNum;
	}
	public void setCbrNum(int cbrNum) {
		CbrNum = cbrNum;
	}
	public String getJbr() {
		return jbr;
	}
	public void setJbr(String jbr) {
		this.jbr = jbr;
	}
	public int getJbrNum() {
		return jbrNum;
	}
	public void setJbrNum(int jbrNum) {
		this.jbrNum = jbrNum;
	}
	public String getJyr() {
		return jyr;
	}
	public void setJyr(String jyr) {
		this.jyr = jyr;
	}
	public int getJyrNum() {
		return JyrNum;
	}
	public void setJyrNum(int jyrNum) {
		JyrNum = jyrNum;
	}
	public String getHdr() {
		return hdr;
	}
	public void setHdr(String hdr) {
		this.hdr = hdr;
	}
	public int getHdrNum() {
		return HdrNum;
	}
	public void setHdrNum(int hdrNum) {
		HdrNum = hdrNum;
	}
	public String getJGMS() {
		return JGMS;
	}
	public void setJGMS(String jGMS) {
		JGMS = jGMS;
	}
	public String getKdsj() {
		return kdsj;
	}
	public void setKdsj(String kdsj) {
		this.kdsj = kdsj;
	}
	public String getCbsj() {
		return cbsj;
	}
	public void setCbsj(String cbsj) {
		this.cbsj = cbsj;
	}
	public String getJbsj() {
		return jbsj;
	}
	public void setJbsj(String jbsj) {
		this.jbsj = jbsj;
	}
	public String getJysj() {
		return jysj;
	}
	public void setJysj(String jysj) {
		this.jysj = jysj;
	}
	public String getCjgsj() {
		return cjgsj;
	}
	public void setCjgsj(String cjgsj) {
		this.cjgsj = cjgsj;
	}
	public String getXMMC() {
		return XMMC;
	}
	public void setXMMC(String xMMC) {
		XMMC = xMMC;
	}
	public String getJG() {
		return JG;
	}
	public void setJG(String jG) {
		JG = jG;
	}
	public String getDW() {
		return DW;
	}
	public void setDW(String dW) {
		DW = dW;
	}
	public String getCKFW() {
		return CKFW;
	}
	public void setCKFW(String cKFW) {
		CKFW = cKFW;
	}
	public String getSYFF() {
		return SYFF;
	}
	public void setSYFF(String sYFF) {
		SYFF = sYFF;
	}
	public String getFlag() {
		return Flag;
	}
	public void setFlag(String flag) {
		Flag = flag;
	}
	public String getYQMC() {
		return YQMC;
	}
	public void setYQMC(String yQMC) {
		YQMC = yQMC;
	}
	public int getJGXH() {
		return JGXH;
	}
	public void setJGXH(int jGXH) {
		JGXH = jGXH;
	}

	
}
