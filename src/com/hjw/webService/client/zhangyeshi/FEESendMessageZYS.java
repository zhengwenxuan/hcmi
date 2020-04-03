package com.hjw.webService.client.zhangyeshi;

import java.net.InetAddress;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.interfaces.util.HttpUtil;
import com.hjw.interfaces.util.JaxbUtil;
import com.hjw.service.ConfigService;
import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.Bean.Fee;
import com.hjw.webService.client.body.FeeMessage;
import com.hjw.webService.client.body.FeeResultBody;
import com.hjw.webService.client.zhonglian.bean.ZLReqPatInfoBean;
import com.hjw.wst.DTO.ExamInfoDTO;
import com.hjw.wst.DTO.UserDTO;
import com.synjones.framework.exception.ServiceException;
import com.synjones.framework.persistence.JdbcQueryManager;
import com.synjones.framework.support.PageSupport;

import net.sf.json.JSONObject;

public class FEESendMessageZYS {
	
	private FeeMessage feeMessage;
	private static JdbcQueryManager jdbcQueryManager;
	static {
		init();
	}

	public static void init() {
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		jdbcQueryManager = (JdbcQueryManager) wac.getBean("jdbcQueryManager");
	}

	public FEESendMessageZYS(FeeMessage feeMessage) {
		this.feeMessage = feeMessage;
	}
	
	//操作员ID
	private static final String userId = "2"; 
	
	/**
	 * 张掖收费接口
	 * @param url
	 * @param logname
	 * @return
	 */
	public FeeResultBody getMessage(String url, String logname) {
		//读取数据配置信息
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		ConfigService configService = (ConfigService) wac.getBean("configService");
		
		FeeResultBody rb = new FeeResultBody();
		String xml = "";
		try {
			xml = JaxbUtil.convertToXml(this.feeMessage, true);			
			TranLogTxt.liswriteEror_to_txt(logname,"传入参数:"+feeMessage.getREQ_NO()+":"+xml);
			
			StringBuffer bufferXml = new StringBuffer();
			String exam_num=this.feeMessage.getPROJECTS().getPROJECT().get(0).getEXAM_NUM();
			
			double charge = 0.0; //实收总费
			String company = "";
			for(Fee f:this.feeMessage.getPROJECTS().getPROJECT()){
				charge += Double.parseDouble(f.getCHARGES());
				company = f.getUNITS();
			}
			TranLogTxt.liswriteEror_to_txt(logname," ==exam_num==:"+exam_num);
			//拼接xml文件
			bufferXml.append("<Root>");
				//体检者有一条信息
				bufferXml.append("<VAA>");
					bufferXml.append("<Ie ");
						bufferXml.append("VAA01 = \""+this.feeMessage.getPROJECTS().getPROJECT().get(0).getPATIENT_ID()+"\" ");
						bufferXml.append("VAA07 = \""+this.feeMessage.getPROJECTS().getPROJECT().get(0).getVISIT_NO()+"\" ");
						
						TranLogTxt.liswriteEror_to_txt(logname,"==VAA07==:"+this.feeMessage.getPROJECTS().getPROJECT().get(0).getVISIT_NO());
						
						bufferXml.append("ACF01 = \"4\" ");
						//取数据库配置
						bufferXml.append("EmpId = \""+userId+"\" "); //操作员Id
						UserDTO userDto = FEESendMessageZYS.getUserInfoForId(userId,logname);
						
						TranLogTxt.liswriteEror_to_txt(logname,"查询SQL结果 ==userDto.getWork_num()==:"+userDto.getWork_num()+"====userDto.getName()==="+userDto.getName());
						
						bufferXml.append("EmpNo = \""+userDto.getWork_num()+"\" "); //操作员编码
						bufferXml.append("EmpName = \""+userDto.getName()+"\" "); //操作员名称
						InetAddress ia=null;
				        try {
				            ia=ia.getLocalHost();
				            TranLogTxt.liswriteEror_to_txt(logname,"IP地址 ==ia.getHostName()==:"+ia.getHostName()+"====ia.getHostAddress()==="+ia.getHostAddress());
				            bufferXml.append("HostName=\""+ia.getHostName()+"\" ");
				            bufferXml.append("HostIP=\""+ia.getHostAddress()+"\" ");
				        } catch (Exception e) {
				            e.printStackTrace();
				        }
				        bufferXml.append("WorkName = \"医技记账\" ");
				        bufferXml.append("WorkType = \"5\" ");
				        bufferXml.append("WorkFrom = \"3\" ");
				        
				        String deptId = configService.getCenterconfigByKey("IS_HIS_DEPT_ID").getConfig_value().trim();//开单科室ID
				        TranLogTxt.liswriteEror_to_txt(logname," ==deptId==:"+deptId);
				        bufferXml.append("BCK01A = \""+deptId+"\" "); //开单科室ID 
				        bufferXml.append("BCK01B = \"0\" ");
				        String doctorid = configService.getCenterconfigByKey("IS_HIS_DOCTOR_ID").getConfig_value().trim();//开单医生id
				        TranLogTxt.liswriteEror_to_txt(logname," ==doctorid==:"+doctorid);
				        bufferXml.append("BCE01A = \""+doctorid+"\" ");  //开单医生ID
				        String doctorCode = configService.getCenterconfigByKey("IS_HIS_DOCTOR_CODE").getConfig_value().trim();//开单医生id
				        TranLogTxt.liswriteEror_to_txt(logname," ==doctorCode==:"+doctorCode);
				        bufferXml.append("BCE02A = \""+doctorCode+"\" ");  //开单医生编码
				        String doctorName = configService.getCenterconfigByKey("IS_HIS_DOCTOR_NAME").getConfig_value().trim();//开单医生姓名
				        TranLogTxt.liswriteEror_to_txt(logname," ==doctorName==:"+doctorName);
				        bufferXml.append("BCE03A = \""+doctorName+"\" "); //开单医生姓名
				        
				        bufferXml.append("IsHyType = \"0\" ");
				        bufferXml.append("BDP02 =\"体检病人\" ");
				        bufferXml.append("ABC02 =\"体检费\" ");
				        
				        bufferXml.append("/>");
				bufferXml.append("</VAA>");
				//每个病人每次结帐只有一条记录
				bufferXml.append("<VAI>");
					bufferXml.append("<Ie ");
						bufferXml.append("VAI00 = \""+feeMessage.getREQ_NO()+"\" "); //申请单号
						
						bufferXml.append("VAI01 = \"0\" ");
						bufferXml.append("VAI04 = \"\" ");
						bufferXml.append("VAI05 = \"体检记账\" ");
						
						bufferXml.append("BCK01B =\""+deptId+"\" "); //开单科室ID
						
						bufferXml.append("VAI14 =\"\" "); //团检姓名  个人为空
						
				        bufferXml.append("BCE02A = \""+doctorCode+"\" ");  //开单医生编码
				        bufferXml.append("BCE03A = \""+doctorName+"\" "); //开单医生姓名
				        
				        bufferXml.append("Enabled =\"0\" ");
				        bufferXml.append("VAI17 =\"1\" ");
				        bufferXml.append("VAI22 =\"0\" ");
				        bufferXml.append("VAI23 =\"0\" ");
						
					bufferXml.append("/>");
				bufferXml.append("</VAI>");
				//可以有多条收费记录
				bufferXml.append("<VAJ>");
					bufferXml.append("<Ie ");
						bufferXml.append("VAI00 = \""+feeMessage.getREQ_NO()+"\" "); //申请单号
						bufferXml.append("VAJ01=\"0\" ");
						bufferXml.append("ROWNR =\"1\" ");
						bufferXml.append("VAI01=\"0\" ");
						bufferXml.append("BDN01=\"E\" "); 
						bufferXml.append("VAF01=\"0\" ");
						
						//String SFXMID = configService.getCenterconfigByKey("IS_HIS_SFXM_ID").getConfig_value().trim();//收费项目ID
						bufferXml.append("BBY01=\"13021\" "); //收费项目ID
						bufferXml.append("VAJ22=\"0\" ");
						bufferXml.append("VAJ23=\"1\" ");
						
						bufferXml.append("VAJ24=\"0\" "); //数量
						TranLogTxt.liswriteEror_to_txt(logname," ==费用charge==:"+charge);
						bufferXml.append("VAJ25=\"1\" ");  
						bufferXml.append("VAJ26=\"0\" ");
						bufferXml.append("VAJ27=\"0\" ");
						
						//String ZXKSID = configService.getCenterconfigByKey("IS_HIS_ZXKS_ID").getConfig_value().trim();//执行科室ID
						bufferXml.append("BCK01D =\"10\" "); //执行科室ID  本次退费是全退的  默认为10
						bufferXml.append("VAJ30=\"100\" ");
						bufferXml.append("VAJ31=\"100\" ");
						bufferXml.append("VAJ32=\"0\" ");
						bufferXml.append("VAJ59=\"0\" ");
						bufferXml.append("VAJ33=\""+charge+"\" ");
						bufferXml.append("VAJ34=\"1\" ");
						
						bufferXml.append("VAJ35=\""+company+"\" ");
						bufferXml.append("VAJ36=\""+charge+"\" ");  
						bufferXml.append("VAJ37=\""+charge+"\" ");  
						bufferXml.append("VAJ38=\""+charge+"\" ");  
						
						bufferXml.append("VAJ39=\"0\" ");
						bufferXml.append("BCK01E=\"0\" ");
						bufferXml.append("BCK01C =\""+deptId+"\" "); //开单科室ID
						bufferXml.append("BCE02B = \""+doctorCode+"\" ");  //开单医生编码
				        bufferXml.append("BCE03B = \""+doctorName+"\" "); //开单医生姓名
						
					bufferXml.append("/>");
					
				bufferXml.append("</VAJ>");
			
			bufferXml.append("</Root>");
			
			TranLogTxt.liswriteEror_to_txt(logname,"拼接字符串信息:" + bufferXml.toString());
			
			//http://127.0.0.1:3336/getinfo.html
			//url = url + "?&paramStr="+bufferXml.toString()+" &Refuse=0&IsCheck=0&InterfaceID=1";
			
			String param = "?&paramStr="+bufferXml.toString()+" &Refuse=0&IsCheck=0&InterfaceID=1";
			
			TranLogTxt.liswriteEror_to_txt(logname,"url路径打印=== :" +url+"===param==="+param);
			
			Map<String, Object> para = new HashMap<String, Object>();
			
			para.put("paramStr", bufferXml.toString());
			para.put("Refuse", 0);
			para.put("IsCheck", 0);
			para.put("InterfaceID", 1);
			
			String result = HttpUtil.doPost(url,para, "UTF-8");
			
			if ((result != null) && (result.trim().length() > 0)) {
				result = result.trim();
				//解析XML
				ZYSResolveXML ssl = new ZYSResolveXML();
				Map<String, String> mapXML = ssl.resolveXML(result, true);
				
				TranLogTxt.liswriteEror_to_txt(logname,"请求后返回结果=== :" +result);
				
				if ("1".equals(mapXML.get("resultCode"))) {
					//插入数据库表 zl_req_patInfo
					boolean resultIns =  this.insertReq(exam_num, result, feeMessage.getREQ_NO(), logname);
					if(resultIns) {
						rb.getResultHeader().setTypeCode("AA");
						rb.getResultHeader().setText("发送收费申请成功！");
					}else {
						rb.getResultHeader().setTypeCode("AE");
						rb.getResultHeader().setText("发送收费申请成功！写入数据库失败");
						TranLogTxt.liswriteEror_to_txt(logname, "req===:发送收费申请成功！写入数据库失败");
					}
				}else if("0".equals(mapXML.get("resultCode"))) { 
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText(mapXML.get("resultMsg"));
				}else{
					rb.getResultHeader().setTypeCode("AE");
					rb.getResultHeader().setText("不识别的错误代码"+mapXML.get("resultMsg"));
				}
			} else {
				rb.getResultHeader().setTypeCode("AE");
				rb.getResultHeader().setText("返回结果错误："+result);
			}
		
		} catch (Throwable ex){
			TranLogTxt.liswriteEror_to_txt(logname,"Exception:" + com.hjw.interfaces.util.StringUtil.formatException(ex));
		    rb.getResultHeader().setTypeCode("AE");
			rb.getResultHeader().setText("费用信息 xml格式文件错误");
		}
		
		TranLogTxt.liswriteEror_to_txt(logname,"===getResultHeader().getTypeCode()==="+rb.getResultHeader().getTypeCode());
		
		xml = JaxbUtil.convertToXml(rb, true);
		TranLogTxt.liswriteEror_to_txt(logname, "req:" + feeMessage.getREQ_NO() + ":" + xml);
		
		return rb;
	}
	
	/**
	 * 
	 * @param id 根据ID查询用户
	 * @param logname
	 * @return
	 * @throws ServiceException
	 */
	public static UserDTO getUserInfoForId(String id,String logname) throws ServiceException {
		StringBuffer sb = new StringBuffer();
		sb.append(" SELECT uu.id,uu.chi_name as name,uu.work_num from user_usr uu where uu.id='"+id+"' ");	
		TranLogTxt.liswriteEror_to_txt(logname, "根据ID查询用户SQL:" +sb.toString());
		PageSupport map = jdbcQueryManager.getList(sb.toString(), 1, 10000, UserDTO.class);
		UserDTO user = new UserDTO();
		if((map!=null)&&(map.getList().size()>0)){
			user= (UserDTO)map.getList().get(0);			
		}else {
			user.setName("");
			user.setWork_num("");
		}
		return user;
	} 
	
	
	/**
	 * 插入返回结果信息
	 * @param exam_num 体检号
	 * @param reqMsg  返回信息
	 * @param logname
	 * @return
	 */
	private boolean insertReq(String exam_num,String reqMsg,String req_no,String logname){
		Connection zysReq = null;
		boolean tjvip=false;
		try {
			long exam_info_id  = 0; 
			String sql1 = "select id from exam_info where exam_num = '"+exam_num+"'";
			PageSupport map = this.jdbcQueryManager.getList(sql1, 1, 100, ExamInfoDTO.class);
			TranLogTxt.liswriteEror_to_txt(logname, "查询exam_id语句:" +sql1);	
			if((map!=null)&&(map.getList().size()>0)){
				ExamInfoDTO examInfo = (ExamInfoDTO)map.getList().get(0);
				exam_info_id = examInfo.getId();
			}
			ZYSResolveXML ssl = new ZYSResolveXML();
			Map<String, String> mapXML = ssl.resolveXML(reqMsg, true); //为了获取单据ID
			zysReq = this.jdbcQueryManager.getConnection();
			String sb1 = "insert zl_req_patInfo (exam_info_id,zl_pat_id,exam_num,zl_mzh,zl_tjh,flag) values('"
			+exam_info_id+"','0','"+exam_num+"','"+req_no+"','"+mapXML.get("单据ID")+"','0') ";
			TranLogTxt.liswriteEror_to_txt(logname, "插入zl_req_patInfo表SQL:" +sb1);				
			zysReq.createStatement().execute(sb1);
			tjvip=true;
		} catch (SQLException ex) {
			TranLogTxt.liswriteEror_to_txt(logname, "res:  插入数据操作失败" +com.hjw.interfaces.util.StringUtil.formatException(ex));
		} finally {
			try {
				if (zysReq != null) {
					zysReq.close();
				}
			} catch (SQLException sqle4) {
				sqle4.printStackTrace();
			}
		}
		return tjvip;
	}
	
}
