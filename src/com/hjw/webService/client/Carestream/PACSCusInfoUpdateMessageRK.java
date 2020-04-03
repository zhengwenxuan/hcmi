package com.hjw.webService.client.Carestream;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Carestream.Bean.RKReturnXml;
import com.hjw.webService.client.Carestream.client.CSHESBService;
import com.hjw.webService.client.Carestream.client.CSHESBServiceLocator;
import com.hjw.webService.client.Carestream.client.CSHESBServiceSoap_PortType;
import com.hjw.webService.client.body.ResultPacsBody;
import com.hjw.wst.DTO.ExamInfoUserDTO;

public class PACSCusInfoUpdateMessageRK {

	public ResultPacsBody getMessage(String url,String convalue,String logname,ExamInfoUserDTO eu) {		
		ResultPacsBody rb = new ResultPacsBody();	
//		String[] tokenurls= convalue.split("&");  //系统标记&院区代码&医院代码
		try {
			StringBuffer sb = new StringBuffer("");
			sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
			sb.append("<xmlMessage>");
			//人员基本信息
			sb.append("<Patient>");
//			sb.append("<SocialSecurityNo></SocialSecurityNo>");
//			sb.append("<PatientID><![CDATA[057101"+eu.getExam_num()+"]]></PatientID>");
			sb.append("<RemotePID><![CDATA[057101"+eu.getArch_num()+"]]></RemotePID>");//第三方系统的PatientID	系统标志+院区代码+Patid
			sb.append("<LocalName><![CDATA["+eu.getUser_name()+"]]></LocalName>");
			sb.append("<EnglishName><![CDATA[]]></EnglishName>");
			sb.append("<Gender><![CDATA["+eu.getSex()+"]]></Gender>");
			sb.append("<Birthday><![CDATA["+eu.getBirthday()+"]]></Birthday>");
	        sb.append("<ReferenceNo><![CDATA["+eu.getId_num()+"]]></ReferenceNo>");
	        sb.append("<MedicareNo></MedicareNo>");
	        sb.append("<IsVIP><![CDATA[0]]></IsVIP>");//是否VIP	0-普通   1-VIP
			sb.append("<Telephone><![CDATA["+eu.getPhone()+"]]></Telephone>");
			sb.append("<Address><![CDATA["+eu.getAddress()+"]]></Address>");
			sb.append("</Patient>");
			sb.append("</xmlMessage>");
			//发送申请
			TranLogTxt.liswriteEror_to_txt(logname,"req:"+eu.getExam_num()+":"+sb.toString());
			try {
				CSHESBService  dam = new CSHESBServiceLocator(url);
				CSHESBServiceSoap_PortType dams = dam.getCSHESBServiceSoap();
				String messages = dams.callESB("UpdatePatientInfo",sb.toString());
				TranLogTxt.liswriteEror_to_txt(logname,"res:"+eu.getExam_num()+":"+messages);
				RKReturnXml rt=new RKReturnXml();					
				rt = JaxbUtil.converyToJavaBean(messages, RKReturnXml.class);
				if("1".equals(rt.getResultCode())){
					rb.getResultHeader().setTypeCode("AA");
				}
			} catch (Exception ex) {
				TranLogTxt.liswriteEror_to_txt(logname,"req:"+eu.getExam_num()+":"+com.hjw.interfaces.util.StringUtil.formatException(ex));
				ex.printStackTrace();
			}
		} catch (Exception ex){
		    rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("组装pacs xml格式文件错误");
		}
		return rb;
	}
}
