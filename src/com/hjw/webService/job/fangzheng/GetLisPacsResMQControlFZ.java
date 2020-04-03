package com.hjw.webService.job.fangzheng;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.DateTimeUtil;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.body.ResultHeader;
import com.hjw.webService.client.fangzheng.LisResMessageFZ;
import com.hjw.webService.client.fangzheng.PacsResMessageFZ;
import com.hjw.webService.client.fangzheng.Bean.MQMessageBean;
import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.MQConstants;
import com.synjones.framework.persistence.JdbcQueryManager;

public class GetLisPacsResMQControlFZ {
	
	@SuppressWarnings("rawtypes")
	private static Hashtable<String, Comparable> env = new Hashtable<String, Comparable>();

	// 队列管理器名
	private static String queueManagerName;
	// 队列管理器引用
	private static MQQueueManager queueManager;
	// 队列名
	private static String queueName;
	// 队列引用
	private MQQueue queue;
	
	
	private static String hostName = "192.168.8.201";
	private static String channel = "IE.SVRCONN";
	private static int port = 6000;
	private static String userId = "miao_zhenxin";
	
	private final String logname="getLisPacsResMQ";
	private static JdbcQueryManager jdbcQueryManager;
    static{
    	init();
    	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}
	
	/**
	 * <b>应用启动时初始化队列管理器连�?/b> <br>
	 * 由于连接队列管理器如同连接数据一样，建立时需要资源较多， 连接时间较长�?因此不要每次创建关闭�?建议应用程序保持�?��
	 * 或多个队列管理器连接�? 但应用关闭时注意关闭连接，释放资源！
	 * 
	 * @throws Exception
	 */

	public static void initEnvironment(String hostName,int port) throws Exception {
		// 服务器地id、名称
		env.put(MQConstants.HOST_NAME_PROPERTY, hostName);
		// 连接通道
		env.put(MQConstants.CHANNEL_PROPERTY, channel);
		// 服务器MQ服务使用的编1381代表GBK,1208代表UTF(Coded Character Set Identifier:CCSID)
		env.put(MQConstants.CCSID_PROPERTY, 1208);
		// 端口号
		env.put(MQConstants.PORT_PROPERTY, port);
		// 传输类型
		env.put(MQConstants.TRANSPORT_PROPERTY, MQConstants.TRANSPORT_MQSERIES);
		
		//用户标识
//		env.put(CMQC.USER_ID_PROPERTY, userId);

		// 设置目标队列管理器
		queueManagerName = "GWO.QM";
		// 设置目标队列 --HCA_CIM_TO_HL7_IN
		queueName = "OUT.S040.HJW.LQ";

		// 建立队列管理器连接
		connectQM();
	}
	
	/**
	 * 程序结束时释放队列管理连接资源
	 * 
	 * @throws Exception
	 */
	public static void destroyEnvironment() throws Exception {
		disconnectQM();
	}

	
	public ResultHeader getMessage(String url, String logname)throws Exception {
		ResultHeader rb = new ResultHeader();
		try {
			String ip = url.split("&")[0];
			int port = Integer.valueOf(url.split("&")[1]);

			initEnvironment(ip, port);
			// 设置将要连接的队列属性
			int openOptions = MQConstants.MQOO_INPUT_AS_Q_DEF | MQConstants.MQOO_OUTPUT
					| MQConstants.MQOO_FAIL_IF_QUIESCING;

			// 设置取消息参数属性
			MQGetMessageOptions gmo = new MQGetMessageOptions();
			gmo.options = MQConstants.MQGMO_SYNCPOINT | MQConstants.MQGMO_WAIT;

			// 设置等待时间-1 为无限等待）
			gmo.waitInterval = 1000; // 毫秒
			// 打开目标队列
			queue = queueManager.accessQueue(queueName, openOptions);

			while (true) {
				// 从队列中取出消息
				MQMessage msg = new MQMessage();
				queue.get(msg, gmo);
				int dataLength = msg.getDataLength();
				// 取的自定义消息头
				// System.out.println("service_id:"
				// + msg.getObjectProperty("service_id"));
				// System.out.println("order_dispatch_type_code:"
				// + msg.getObjectProperty("order_dispatch_type_code"));
				// System.out.println("domain_id:"
				// + msg.getObjectProperty("domain_id"));
				// System.out.println("exec_unit_id:"
				// + msg.getObjectProperty("exec_unit_id"));
				// 取得消息内容
				String xmlmessage = msg.readStringOfByteLength(dataLength);
				// 打印消息内容
				TranLogTxt.liswriteEror_to_txt(logname, "res:" + xmlmessage);
				queueManager.commit();

				if ((xmlmessage != null) && (xmlmessage.trim().length() > 0)) {
					/*MQMessageBean callRecord = new MQMessageBean();
					callRecord.setCreatetime(DateTimeUtil.getDateTime());
					callRecord.setMessages(xmlmessage);
					saveMQMessage(callRecord);*/
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
			    		}    		
			    		
			    	}

			}
		} catch (MQException e) {
			// 回滚事务
			queueManager.backout();
			if (e.reasonCode == MQConstants.MQRC_CHANNEL_NOT_AVAILABLE
					|| e.reasonCode == MQConstants.MQRC_CONNECTION_BROKEN
					|| e.reasonCode == MQConstants.MQRC_CONNECTION_ERROR
					|| e.reasonCode == MQConstants.MQRC_CONNECTION_STOPPED
					|| e.reasonCode == MQConstants.MQRC_Q_MGR_QUIESCING
					|| e.reasonCode == MQConstants.MQRC_GET_INHIBITED) {
				// 设定重新连接（接入厂商考虑重新连接设定）
				connectQM();
				System.out.println("队列管理器连接不可用，需要重新创建队列管理器连接! 原因：" + e.reasonCode);
			} else
				throw e;
		} finally {
			// 关闭队列
			if (queue != null) {
				queue.close();
			}
			// 断开连接（此为Demo，厂商接入时要用长连接，不要发一次消息建立一个连接）
			disconnectQM();
		}
		return rb;
	}


private static void connectQM() throws Exception {
	queueManager = new MQQueueManager(queueManagerName, env);
}

private static void disconnectQM() throws Exception {
	if (queueManager != null) {
		queueManager.disconnect();
	}
}
    
public void saveMQMessage(MQMessageBean mq) throws Exception{
	Connection tjtmpconnect = null;
	try {
		tjtmpconnect = this.jdbcQueryManager.getConnection();
		PreparedStatement ps = tjtmpconnect.prepareStatement("insert into zl_req_mq(messagetype,conttype,createtime,messages) values(?,?,?,?)");  
        ps.setString(1, "1");        
        ps.setInt(2, 0);  
        ps.setString(3, DateTimeUtil.getDateTime());  
        ps.setString(4, mq.getMessages());
        ps.executeUpdate();  
        ps.close();  
       // in.close();
	} catch (SQLException ex) {
		TranLogTxt.liswriteEror_to_txt("reqmq", "zl_req_mq 操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
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
