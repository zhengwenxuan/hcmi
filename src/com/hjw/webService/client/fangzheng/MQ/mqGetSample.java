package com.hjw.webService.client.fangzheng.MQ;

import java.util.Hashtable;

import com.ibm.mq.MQException;
import com.ibm.mq.MQGetMessageOptions;
import com.ibm.mq.MQMessage;
import com.ibm.mq.MQQueue;
import com.ibm.mq.MQQueueManager;
import com.ibm.mq.constants.MQConstants;

public class mqGetSample {
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

	public String getMsg(String mqstr,String service_id) throws Exception {
        String message="";
		// 设置将要连接的队列属性
		int openOptions = MQConstants.MQOO_INPUT_AS_Q_DEF
				| MQConstants.MQOO_OUTPUT | MQConstants.MQOO_FAIL_IF_QUIESCING;

		// 设置取消息参数属性
		MQGetMessageOptions gmo = new MQGetMessageOptions();
		gmo.options = MQConstants.MQGMO_SYNCPOINT | MQConstants.MQGMO_WAIT;

		// 设置等待时间-1 为无限等待）
		gmo.waitInterval = 1000; // 毫秒
		queueName = "OUT."+service_id+".LQ";
		// 打开目标队列
		queue = queueManager.accessQueue(queueName, openOptions);
		try {
			while (true) {
				// 从队列中取出消息
				MQMessage msg = new MQMessage();
				queue.get(msg, gmo);
				int dataLength = msg.getDataLength();
				// 取的自定义消息头
//				System.out.println("service_id:"
//						+ msg.getObjectProperty("service_id"));
//				System.out.println("order_dispatch_type_code:"
//						+ msg.getObjectProperty("order_dispatch_type_code"));
//				System.out.println("domain_id:"
//						+ msg.getObjectProperty("domain_id"));
//				System.out.println("exec_unit_id:"
//						+ msg.getObjectProperty("exec_unit_id"));
				// 取得消息内容
				message = msg.readStringOfByteLength(dataLength);
				// 打印消息内容
				System.out.println("消息内容：\n" + message + "\n");
				queueManager.commit();
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
				System.out.println("队列管理器连接不可用，需要重新创建队列管理器连接! 原因："+ e.reasonCode);
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
		return message;
	}

	private static void connectQM() throws Exception {
		queueManager = new MQQueueManager(queueManagerName, env);
	}

	private static void disconnectQM() throws Exception {
		if (queueManager != null) {
			queueManager.disconnect();
		}
	}
}
