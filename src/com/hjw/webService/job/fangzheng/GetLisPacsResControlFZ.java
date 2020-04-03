package com.hjw.webService.job.fangzheng;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.ZlReqMqBean;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.fangzheng.LisResMessageFZ;
import com.hjw.webService.client.fangzheng.PacsResMessageFZ;
import com.synjones.framework.persistence.JdbcQueryManager;

public class GetLisPacsResControlFZ {
	private final String logname="getLisPacsResControlFZ";
	private static JdbcQueryManager jdbcQueryManager;
    static{
    	init();
    	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
    public ResultHeader getMessage(){
    	ResultHeader rb = new ResultHeader();
    	List<ZlReqMqBean> ei= new ArrayList<ZlReqMqBean>();
    	ei=getzl_req_mq(logname);
    	for(ZlReqMqBean zm:ei){
    	try{
    		String xmlmessage=zm.getMessages();
    	if((xmlmessage!=null)&&(xmlmessage.trim().length()>0)){
    		InputStream is = new ByteArrayInputStream(xmlmessage.getBytes("utf-8"));
    		Map<String, String> xmlMap = new HashMap<>();
    		xmlMap.put("abc", "urn:hl7-org:v3");
    		SAXReader sax = new SAXReader();
    		sax.getDocumentFactory().setXPathNamespaceURIs(xmlMap);
    		Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
    		String setId = document.selectSingleNode("abc:ClinicalDocument/abc:setId/@extension").getText();// 获取根节点;
    		if("BS320".equals(setId)){//pacs
    			PacsResMessageFZ pacs = new PacsResMessageFZ();
    			rb=pacs.accetpMessagePacs(xmlmessage);
    		}else if("BS369".equals(setId)){//pacs
    			PacsResMessageFZ pacs = new PacsResMessageFZ();
    			rb=pacs.accetpMessagePacs(xmlmessage);
    		}else if("BS319".equals(setId)){//lis
    			LisResMessageFZ lis= new LisResMessageFZ();
    			rb=lis.accetpMessageLis(xmlmessage);
    			/*if("AA".equals(rb.getTypeCode())){
    				deletezl_req_item(zm.getId(),logname);
    			}*/
    		}    		
    		if("AA".equals(rb.getTypeCode())){
    			updatezl_req_item(zm.getId(),"1",logname);
    		}
    	}
    	}catch(Exception ex){
    		ex.printStackTrace();
    	}
    	}
    	return rb;    	
    }
    
    /**
	 * 
	 * @param zlreq
	 * @return
	 * @throws Exception
	 */
	public List<ZlReqMqBean> getzl_req_mq(String logname){
		List<ZlReqMqBean> ei= new ArrayList<ZlReqMqBean>();
		Connection tjtmpconnect = null;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
		StringBuffer sb = new StringBuffer();
		sb.append("select id,messagetype,messages,createtime from zl_req_mq where conttype='0' ");	
		TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " +sb.toString());
		 ResultSet rs = tjtmpconnect.createStatement().executeQuery(sb.toString());
		  while (rs.next()) {
			   
			     ZlReqMqBean zb = new ZlReqMqBean();
			     zb.setCreatetime(rs.getString("createtime"));
			     zb.setMessages(rs.getString("messages"));
			     zb.setId(rs.getLong("id"));
			     ei.add(zb);
			   
		  }
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res: zl_req_pacs_item 操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {				
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return ei;
	}
	
	public void updatezl_req_item(long id,String types,String logname){		
		Connection tjtmpconnect = null;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "update zl_req_mq set conttype='"+types+"' where  id='"+id+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " +sb1);
			tjtmpconnect.createStatement().execute(sb1);
		
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res: zl_req_pacs_item 操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {				
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
	}
	
	public void deletezl_req_item(long id,String logname){		
		Connection tjtmpconnect = null;
		try {
			tjtmpconnect = this.jdbcQueryManager.getConnection();
			String sb1 = "delete from zl_req_mq where  id='"+id+"'";
			TranLogTxt.liswriteEror_to_txt(logname, "res:操作语句： " +sb1);
			tjtmpconnect.createStatement().execute(sb1);
		
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res: zl_req_pacs_item 操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {				
				if (tjtmpconnect != null) {
					tjtmpconnect.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
	}
}
