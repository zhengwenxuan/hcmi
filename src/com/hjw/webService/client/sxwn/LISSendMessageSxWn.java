package com.hjw.webService.client.sxwn;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.fr.third.org.hsqldb.lib.StringUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ApplyNOBean;
import com.hjw.webService.client.Bean.LisComponent;
import com.hjw.webService.client.Bean.LisComponents;
import com.hjw.webService.client.body.LisMessageBody;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.body.ResultLisBody;
import com.hjw.webService.client.sxwn.Bean.LisReqNewDataSet;
import com.hjw.webService.client.sxwn.client.LISWebService;
import com.hjw.webService.client.sxwn.client.LISWebServiceLocator;
import com.hjw.webService.client.sxwn.client.LISWebServiceSoap_PortType;
import com.hjw.wst.DTO.ThridLisClassDTO;
import com.hjw.wst.service.DataService;

import net.sf.json.JSONSerializer;

/**
 * 
 * @Title: 火箭蛙体检管理系统
 * @Package com.hjw.webService.dbgj
 * @Description: 2.6 检查申请信息服务 天健 平台对接-东北国际
 * @author: yangm
 * @date: 2016年10月7日 下午2:50:56
 * @version V2.0.0.0
 */
public class LISSendMessageSxWn {
	private LisMessageBody lismessage;
	private static DataService dataService;

	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		dataService = (DataService) wac.getBean("dataService");
	}

	public LISSendMessageSxWn(LisMessageBody lismessage) {
		this.lismessage = lismessage;
	}

	/**
	 * 
	 * @Title: lisSend @Description:
	 *         TODO(这里用一句话描述这个方法的作用) @param: @return @return: ResultBody @throws
	 */
	public ResultLisBody getMessage(String url, String logname) {
		TranLogTxt.liswriteEror_to_txt(logname, "1-----------------开始lis请求--------------------");
		ResultLisBody rb = new ResultLisBody();
		try {
			String jsonString = JSONSerializer.toJSON(lismessage).toString();
			TranLogTxt.liswriteEror_to_txt(logname, "req:" + jsonString);
			try {
				TranLogTxt.liswriteEror_to_txt(logname, "2-----------------开始处理lis--------------------");
				LISWebService liswsl = new LISWebServiceLocator(url);
				TranLogTxt.liswriteEror_to_txt(logname, "3-----------------创建webservices成功--------------------");
				LISWebServiceSoap_PortType lis = liswsl.getLISWebServiceSoap();
				List<ApplyNOBean> list = new ArrayList<ApplyNOBean>();
				for (LisComponents lcs : this.lismessage.getComponents()) {
					try {
						boolean reslisfalgs = false;
						boolean reslisclassflags = false;
						Calendar calendar = Calendar.getInstance();
						calendar.setTime(new Date());
						ResultHeader rsh= new ResultHeader();
						TranLogTxt.liswriteEror_to_txt(logname, "4.1-----------------开始检查项目--------------------");
						rsh = this.checkSam(logname,lcs.getItemList());
						TranLogTxt.liswriteEror_to_txt(logname, "4.2-----------------结束检查项目--------------------");
						if ((rsh != null)&&("AA".equals(rsh.getTypeCode()))) {
							String iSampleId=rsh.getText().split("-")[0].trim();
							String tubeID=rsh.getText().split("-")[1].trim();
							String samname=rsh.getText().split("-")[2].trim();
							TranLogTxt.liswriteEror_to_txt(logname, "4.3-----------------开始调用 LIS_InsLisApplyMiddle方法--------------------");
							//System.out.println("userType--5--调用lis.LIS_InsLisApplyMiddle ");
							String messages = lis.LIS_InsLisApplyMiddle("体检", "体检",
									this.lismessage.getCustom().getExam_num(), this.lismessage.getCustom().getName(),
									this.lismessage.getCustom().getSexname(), this.lismessage.getCustom().getOld(), "岁",
									lcs.getCsampleName(), calendar, calendar, "",
									this.lismessage.getDoctor().getDept_code(),
									this.lismessage.getDoctor().getDoctorName(), calendar, "", calendar, "",
									Integer.valueOf(iSampleId), tubeID,
									samname);
							TranLogTxt.liswriteEror_to_txt(logname, "4.4-----------------结束调用 LIS_InsLisApplyMiddle方法--------------------");
							
							//System.out.println("userType--6--" + messages);
							TranLogTxt.liswriteEror_to_txt(logname,
									"res:" + lismessage.getMessageid() + ":lis.LIS_InsLisApplyMiddle(\"体检\",\"体检\","
											+ this.lismessage.getCustom().getExam_num() + ","
											+ this.lismessage.getCustom().getName() + ","
											+ this.lismessage.getCustom().getSexname()+ ","
											+ this.lismessage.getCustom().getOld() + ",\"岁\"," + lcs.getCsampleName()
											+ "," + calendar + "," + calendar + ",\"\""
											+ this.lismessage.getDoctor().getDept_name() + ","
											+ this.lismessage.getDoctor().getDoctorName() + "," + calendar + ",\"\","
											+ calendar + ",\"\"," + Integer.valueOf(iSampleId) + ","
											+ tubeID + "," + samname+ ")");
							TranLogTxt.liswriteEror_to_txt(logname, "res1:" + messages);
							LisReqNewDataSet lrb = new LisReqNewDataSet();
							lrb = JaxbUtil.converyToJavaBean(messages, LisReqNewDataSet.class);
							//System.out.println("userType--7--" + messages);
							int iRequestId = 0;
							String cRequestCode = "";
							if ((lrb != null) && (lrb.getTable2() != null) && (lrb.getTable2().size() > 0)) {

								iRequestId = lrb.getTable2().get(0).getiRequestId();// 申请序号
								cRequestCode = lrb.getTable2().get(0).getcRequestCode();// 申请号(即条码)
								if (!StringUtil.isEmpty(cRequestCode)) {
									reslisfalgs = true;
									for (LisComponent lc : lcs.getItemList()) {
										int classid = Integer.valueOf(lc.getItemCode());
										//System.out.println("userType--8--" + messages);
										TranLogTxt.liswriteEror_to_txt(logname, "5.1-----------------开始调用 LIS_InsLisApplyMiddleDeatil方法--------------------");
										
										String resstring = lis.LIS_InsLisApplyMiddleDeatil(iRequestId, cRequestCode,
												classid, lc.getItemName());
										TranLogTxt.liswriteEror_to_txt(logname, "5.2-----------------结束调用 LIS_InsLisApplyMiddleDeatil方法--------------------"+resstring);
										//System.out.println("userType--9--" + resstring);
										TranLogTxt.liswriteEror_to_txt(logname,
												"res:" + lismessage.getMessageid() + ":lis.LIS_InsLisApplyMiddleDeatil("
														+ iRequestId + "," + classid + "," + lc.getItemName() + ");");
										if ("0".equals(resstring)) {
											reslisclassflags = true;
											break;
										}
									}
								} else {
									reslisfalgs = false;
								}
							}
							if (reslisfalgs && !reslisclassflags) {
								ApplyNOBean ap = new ApplyNOBean();
								ap.setApplyNO(lcs.getReq_no());
								ap.setLis_id(lcs.getLis_id());
								ap.setBarcode(cRequestCode);
								list.add(ap);
							}
						}
					} catch (Exception ex) {
						TranLogTxt.liswriteEror_to_txt(logname, "error:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
					}
				}
				rb.getResultHeader().setTypeCode("AA");
				rb.getControlActProcess().setList(list);
			} catch (Exception ex) {
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("lis调用webservice错误");
			}
		} catch (Exception ex) {
			rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("组装lis xml格式文件错误");
		}
		TranLogTxt.liswriteEror_to_txt(logname, "6-----------------结束lis请求--------------------");
		return rb;
	}

	/**
	 * 
	 * @param logname
	 * @param itemList
	 * @return
	 */
	private ResultHeader checkSam(String logname,List<LisComponent> itemList) {
		ResultHeader rb = new ResultHeader();
		try {			
			String iSampleId="";
			String samname="";
			String tubeID="";
			String classid="";
			String classname="";
			//TranLogTxt.liswriteEror_to_txt(logname, "error:-------------------1--------------------");
			for (int i=0;i<itemList.size();i++) {
				LisComponent lc=  new LisComponent();
				lc=itemList.get(i);
				ThridLisClassDTO tdl = new ThridLisClassDTO();
				tdl = this.dataService.getLisClass(lc.getItemCode());				
				if(i>0){
					if((StringUtil.isEmpty(tdl.getiSampleId()))||(StringUtil.isEmpty(tdl.getTubeID()))){
						rb.setTypeCode("AE");
						rb.setText(lc.getItemCode()+"("+lc.getItemName()+")"+"对应样本id或者试管id为空");
						TranLogTxt.liswriteEror_to_txt(logname, "error:" +lc.getItemCode()+"("+lc.getItemName()+")"+"对应样本id或者试管id为空");
						break;
					}else if(!iSampleId.equals(tdl.getiSampleId())){
						rb.setTypeCode("AE");
						rb.setText(lc.getItemCode()+"("+lc.getItemName()+") "+tdl.getiSampleId()+"与"+classid+"("+classname+") "+iSampleId+"对应样本编码不一致");
						TranLogTxt.liswriteEror_to_txt(logname, "error:" +lc.getItemCode()+"("+lc.getItemName()+") "+tdl.getiSampleId()+"与"+classid+"("+classname+") "+iSampleId+"对应样本编码不一致");
						break;
					}else if(!tubeID.equals(tdl.getTubeID())){
						rb.setTypeCode("AE");
						rb.setText(lc.getItemCode()+"("+lc.getItemName()+") "+tdl.getTubeID()+"与"+classid+"("+classname+") "+tubeID+"对应试管编码不一致");
						TranLogTxt.liswriteEror_to_txt(logname, "error:" +lc.getItemCode()+"("+lc.getItemName()+") "+tdl.getTubeID()+"与"+classid+"("+classname+") "+tubeID+"对应试管编码不一致");
						break;
					}
				}
				//TranLogTxt.liswriteEror_to_txt(logname, "error:-----------------7----------------------");
				iSampleId=tdl.getiSampleId();
				tubeID=tdl.getTubeID();
				samname=tdl.getTesttubeName();
				classid=lc.getItemCode();
				classname=lc.getItemName();
				TranLogTxt.liswriteEror_to_txt(logname, "info:--检查项目="+classid+"--检查项目名称="+classname+"--大项id="+lc.getItemCode()+"--样本="+iSampleId+"--试管id="+tubeID+"--样本名称="+samname);
				rb.setTypeCode("AA");
				rb.setText(iSampleId+" -"+tubeID+" - "+samname);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			rb.setTypeCode("AE");
			rb.setText("class 操作错误");
		}
    return rb;
	}

}
