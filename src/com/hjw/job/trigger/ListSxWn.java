package com.hjw.job.trigger;

import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.sxwn.Bean.LisItemNewDataSet;
import com.hjw.webService.client.sxwn.Bean.LisNewDataSet;
import com.hjw.webService.client.sxwn.Bean.TableBean;
import com.hjw.webService.client.sxwn.client.LISWebService;
import com.hjw.webService.client.sxwn.client.LISWebServiceLocator;
import com.hjw.webService.client.sxwn.client.LISWebServiceSoap_PortType;
import com.hjw.wst.DTO.WebserviceConfigurationDTO;
import com.hjw.wst.service.DataService;
import com.hjw.wst.service.WebserviceConfigurationService;

/**
 * lis 山西卫宁 山西人民医院 获取lis信息
 * 
 * @Title: 火箭蛙体检管理系统
 * @Package com.hjw.wst
 * @Description:
 * @author: dangqi
 * @date: 2017年3月13日 下午5:14:45
 * @version V2.0.0.0
 */
public class ListSxWn {

	private WebserviceConfigurationService webserviceConfigurationService;
	private final String msgname = "lisaccept";
	private final String charset = "utf-8";
	private DataService dataService;
	
	public void setDataService(DataService dataService) {
		this.dataService = dataService;
	}

	public void setWebserviceConfigurationService(WebserviceConfigurationService webserviceConfigurationService) {
		this.webserviceConfigurationService = webserviceConfigurationService;
	}

	public void acceptMessage() {
		TranLogTxt.liswriteEror_to_txt(msgname, "res: 开始同步lis项目及其对应关系");
		try {
			WebserviceConfigurationDTO wcd = new WebserviceConfigurationDTO();
			wcd = webserviceConfigurationService.getWebServiceConfig("LIS_ACCEPT");
			String web_url = wcd.getConfig_url().trim();
			String web_meth = wcd.getConfig_method().trim();
			if ("3".equals(web_meth)) {
				try {
					LISWebService liswsl = new LISWebServiceLocator(web_url);
					LISWebServiceSoap_PortType lis = liswsl.getLISWebServiceSoap();
					String messages = lis.LIS_FindTestClass();// 获取lis 检查项目 大项
					TranLogTxt.liswriteEror_to_txt(msgname, "req: " + messages);
					LisNewDataSet lisacc = new LisNewDataSet();
					try {
						lisacc = JaxbUtil.converyToJavaBean(messages, LisNewDataSet.class);
						for (TableBean tb : lisacc.getTable()) {
							boolean lisclassflag = this.dataService.saveLisClass(tb);
							if (lisclassflag) {
								try {
									int testclassnum = Integer.valueOf(tb.getNum());
									messages = lis.LIS_FindTestClassDetail(testclassnum);
									TranLogTxt.liswriteEror_to_txt(msgname, "req: " + messages);
									LisItemNewDataSet lisaccitem = new LisItemNewDataSet();
									lisaccitem = JaxbUtil.converyToJavaBean(messages, LisItemNewDataSet.class);
									if ((lisaccitem != null) && (lisaccitem.getTable().size()>0)) {
										this.dataService.saveLisClassItem(tb.getNum(),
												lisaccitem.getTable());
									}
								} catch (Exception ex) {
									TranLogTxt.liswriteEror_to_txt(msgname,
											"req: " + tb.getNum() + "获取细项失败" + com.hjw.interfaces.util.StringUtil.formatException(ex));
								}
							}
						}
					} catch (Exception ex) {
						TranLogTxt.liswriteEror_to_txt(msgname, " lis 解析错误");
					}
				} catch (Exception ex) {
					TranLogTxt.liswriteEror_to_txt(msgname, " 调用webservice  " + web_url + " 错误");
				}
			} else {
				TranLogTxt.liswriteEror_to_txt(msgname, " LIS_ACCEPT   web_meth配置了0 不用同步");
			}
		} catch (Exception ex) {
			TranLogTxt.liswriteEror_to_txt(msgname, com.hjw.interfaces.util.StringUtil.formatException(ex));
		}

	}
}
