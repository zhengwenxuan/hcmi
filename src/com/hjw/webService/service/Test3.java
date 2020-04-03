package com.hjw.webService.service;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.webService.service.bean.ResultSerBody;

public class Test3 {

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
      String xmlStr="<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><EXAM_NUM/><PATIENT_ID>T666666</PATIENT_ID><NAME>魏朝6</NAME>"
      		+ "<CHARGES><RCPT_NO>20161025776066</RCPT_NO><OPERATOR_NO>00021</OPERATOR_NO><OPERATOR_NAME/><CHARGE_DATE>2016-10-25 16:2i:01</CHARGE_DATE>"
      		+ "<CHARGE>40</CHARGE><INVOICE_NO/><REQ_NOS><ITEM><REQ_NO>S88888884</REQ_NO>"
      		+ "<COST>10</COST></ITEM><ITEM><REQ_NO>S88888885</REQ_NO><COST>20</COST>"
      		+ "</ITEM><ITEM><REQ_NO>S88888886</REQ_NO><COST>10</COST></ITEM></REQ_NOS>"
      		+ "<CHARGE_TYPES><CHARGE_TYPE><CHARGE_TYPE_CODE>1</CHARGE_TYPE_CODE><CHARGE_TYPE>现金</CHARGE_TYPE>"
      		+ "<AMOUNT>40</AMOUNT></CHARGE_TYPE></CHARGE_TYPES></CHARGES></root>";
      
      String xmlStrs="<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
      		+ "<root><EXAM_NUM/><PATIENT_ID>T16B020001</PATIENT_ID><NAME>test1</NAME><CHARGES>"
      		+ "<RCPT_NO>20161101776124</RCPT_NO><OPERATOR_NO>00021</OPERATOR_NO><OPERATOR_NAME/>"
      		+ "<CHARGE_DATE>2016-11-01 16:43:18</CHARGE_DATE><CHARGE>10</CHARGE><INVOICE_NO/>"
      		+ "<REQ_NOS><ITEM><REQ_NO>161101000013</REQ_NO><COST/></ITEM>"
      		+ "</REQ_NOS><CHARGE_TYPES><CHARGE_TYPE><CHARGE_TYPE_CODE>1</CHARGE_TYPE_CODE>"
      		+ "<CHARGE_TYPE>现金</CHARGE_TYPE><AMOUNT>406</AMOUNT></CHARGE_TYPE></CHARGE_TYPES>"
      		+ "</CHARGES></root>";
      ResultSerBody rb =new ResultSerBody();
      rb = JaxbUtil.converyToJavaBean(xmlStrs,ResultSerBody.class);
      System.out.println(rb.getCHARGES().getREQ_NOS().getITEM());
      }

}
