package com.hjw.webService.client.dbgj.test;

import java.util.ArrayList;
import java.util.List;

import com.hjw.util.DateTimeUtil;
import com.hjw.webService.client.FEESendMessage;
import com.hjw.webService.client.Bean.Fee;
import com.hjw.webService.client.Bean.Fees;
import com.hjw.webService.client.body.FeeMessage;
import com.hjw.webService.client.body.FeeResultBody;

public class FeeSendMessage_test {

	public static void main(String[] args) {
		String url = "http://192.168.111.46:8086/services/Mirth?wsdl";
		FeeMessage fm = new FeeMessage();
		fm.setREQ_NO("S88888887");
		
		Fee fee =new Fee();
		fee.setPATIENT_ID("T666666");
		fee.setEXAM_NUM("T666666");
		fee.setVISIT_DATE(DateTimeUtil.getDate2());
		fee.setVISIT_NO("83153");//就诊序号 必须填写
		fee.setSERIAL_NO("");//流水号 写死 1
		fee.setORDER_CLASS("C");//诊疗项目类别//D-检查 ，C-检验  和his字典一致，也需要传入
		fee.setORDER_NO("1");//ORDER_NO  写死 1
		fee.setORDER_SUB_NO("1");//ORDER_SUB_NO 写死1
		fee.setITEM_NO("1");////顺序号 增加 写死 1
		fee.setITEM_CLASS("C");//收费项目类别
		fee.setITEM_NAME("肥达氏反应");//收费项目名称
		fee.setITEM_CODE("250403038");//项目代码
		fee.setITEM_SPEC("/");//项目规格
		fee.setUNITS("项");//单位
		fee.setREPETITION("");//付数//检查检验，伦次的可以为空
		fee.setAMOUNT("1");//数量
		fee.setORDERED_BY_DEPT("01013511"); //录入科室//体检中心对应his科室，开单科室
		fee.setORDERED_BY_DOCTOR("");//录入医生
		fee.setPERFORMED_BY("01013511"); //执行科室//体检中心对应his科室，开单科室
		fee.setCLASS_ON_RCPT("");  //收费项目分类
		fee.setCOSTS("10");  //计价金额
		fee.setCHARGES("10");  //实收费用流水号
		fee.setRCPT_NO("");  //收据号码
		fee.setCHARGE_INDICATOR("0");  //收费标记
		fee.setCLASS_ON_RECKONING(""); //核算项目分类
		fee.setSUBJ_CODE(""); //会计科目
		fee.setPRICE_QUOTIETY(""); //收费系数
		fee.setCLINIC_NO("2016102583153"); //门诊号 必须填写
		fee.setBILL_DATE(DateTimeUtil.getDate2()); //项目收费日期
		fee.setBILL_NO(""); //项目收费编号
		fee.setSKINTEST("");// 皮试标志
		fee.setPRESC_PSNO(""); //皮试结果
		fee.setINSURANCE_FLAG("0"); //适用医保内外标志：0自费，1医保，2免费
		fee.setINSURANCE_CONSTRAINED_LEVEL("");// 公费用药级别
		fee.setSKIN_SAVE("");//皮试记录时间
		fee.setSKIN_START("");//皮试时间
		fee.setSKIN_BATH("");//药品批号
		fee.setORDER_SUB_NO("1");//ORDER_SUB_NO 写死1
		fee.setITEM_NO("1");////顺序号 增加
		List<Fee> feeList=new ArrayList<Fee>();
		feeList.add(fee);
		
		fee.setSERIAL_NO("2");//流水号
		fee.setORDER_SUB_NO("1");//ORDER_SUB_NO 写死1
		fee.setITEM_NO("2");////顺序号 增加
		feeList.add(fee);
		
		Fees fees = new Fees();
		fees.setPROJECT(feeList);
		fm.setPROJECTS(fees);
		
		FEESendMessage fsm= new FEESendMessage(fm);		
		FeeResultBody frb= new FeeResultBody();
		frb = fsm.feeSend(url,"1", true);
		System.out.println(frb.getResultHeader().getTypeCode() + "-" + frb.getResultHeader().getText());
	}

}
