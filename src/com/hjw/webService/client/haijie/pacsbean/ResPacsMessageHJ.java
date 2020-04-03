package com.hjw.webService.client.haijie.pacsbean;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.hjw.util.DateTimeUtil;


public class ResPacsMessageHJ {
	private RetPacsCustomeHJ rc= new RetPacsCustomeHJ();
	private Document document;
	public ResPacsMessageHJ(String xmlmessage,boolean flags) throws Exception{
		 String xmlmessagess="";
		 if(flags){
			 xmlmessagess=xmlmessage;
		}
		InputStream is = new ByteArrayInputStream(xmlmessagess.getBytes("utf-8"));
		SAXReader sax = new SAXReader();// 创建一个SAXReader对象
		this.document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
		
		this.getdoctor_orderid();//关联医嘱号或者清单号				
		this.getdoctor();//获取报告医生
		this.getdoctor_Item();//关联检查项目	
	}

	public RetPacsCustomeHJ getRetPacsCustome(){
		return this.rc;
	}
	
	
	
	
	
	public void getdoctor_orderid() throws Exception {		
		String pacs_summary_id = document.selectSingleNode("/ddd/body/ddd.servlet.entity.testResults.TestResults/testItemResults/ddd.servlet.entity.testResults.TestItemResult/barcode").getText();  
		this.rc.setPacs_summary_id(pacs_summary_id);
	}
	
	public void getdoctor() throws Exception {
		String doctor_time_bg = document.selectSingleNode("/ddd/body/ddd.servlet.entity.testResults.TestResults/completedDate").getText();
		this.rc.setDoctor_time_bg(doctor_time_bg);
		String reservedField = document.selectSingleNode("/ddd/body/ddd.servlet.entity.testResults.TestResults/testItemResults/ddd.servlet.entity.testResults.TestItemResult/reservedField").getText();
		for(String msg : reservedField.split(";")) {
			if(msg.startsWith("doctor:")) {
				this.rc.setDoctor_name_bg(msg.split(":")[1]);
			} else if(msg.startsWith("auditDoctorCode:")) {
				this.rc.setDoctor_name_sh(msg.split(":")[1]);
			} else if(msg.startsWith("auditDate:")) {
				this.rc.setDoctor_time_sh(msg.split(":")[1]);
			}
		}
	}
	
	public void getdoctor_Item() throws Exception {
		Node Items = document.selectSingleNode("/ddd/body/ddd.servlet.entity.testResults.TestResults/testItemResults/ddd.servlet.entity.testResults.TestItemResult");// 获取根节点
		List<Element> listElement = Items.getParent().elements();// 所有一级子节点的list
		getNodes_doctor_Item(listElement);
	}

	@SuppressWarnings("unchecked")
	private void getNodes_doctor_Item(List<Element> listElement) throws Exception {		
		RetPacsItemHJ pacsItem= new RetPacsItemHJ();
		List<RetPacsPicHJ> listRetPacsPic =new ArrayList<RetPacsPicHJ>();
		for(Element e:listElement){
			InputStream isitem = new ByteArrayInputStream(e.asXML().getBytes("utf-8"));
			SAXReader saxitem = new SAXReader();// 创建一个SAXReader对象
			Document documentItem = saxitem.read(isitem);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
			String dataType = documentItem.selectSingleNode("/ddd.servlet.entity.testResults.TestItemResult/testItemCode").getText();
			String resultValue = documentItem.selectSingleNode("/ddd.servlet.entity.testResults.TestItemResult/resultValue").getText();
			if("cslj".equals(dataType)) {
				RetPacsPicHJ pacsPic = new RetPacsPicHJ();
				pacsPic.setPicURL(resultValue);
				listRetPacsPic.add(pacsPic);
			} else if("Summary".equals(dataType)) {
				pacsItem.setChargingItem_jl(resultValue);
			} else {
				pacsItem.setChargingItem_num(dataType);
				pacsItem.setChargingItem_ms(resultValue);
			}
			pacsItem.setListRetPacsPic(listRetPacsPic);
		}
		this.rc.setPacsItem(pacsItem);
	}

	public static void main(String[] args)throws Exception  {
		String str = "<ddd>"
				+"<head><messageid>1</messageid>"       // 保留，固定不变
				+"<requestDate>2018-04-13 12:46:01</requestDate>"    //请求时间
				+"<userName>sa</userName>"			   //接口授权的用户名，默认为sa
				+"<passsword>sa</passsword>"		    //接口授权的用户名密码，默认为sa
				+"<serviceName>saveTestResultsService</serviceName>"	   // 保留固定不变
				+"<requestMethod>savePathology()</requestMethod>"		   // 保留固定不变
				+"<requestDescription></requestDescription>"          // 保留固定不变
				+"</head>"						    //通讯信息开始部分，requestDate为请求日期，其他为固定值
				+"<body><ddd.servlet.entity.testResults.TestResults>"
				+"<completedDate>2018-04-13 12:46:01</completedDate>"	   //完成日期
				+"<testGroupCode></testGroupCode>"				   //项目组合编码，当后续为小项结果时，可为空
				+"<apparatusCode>tct</apparatusCode>"			    //项目大类
				+"<testItemResults><ddd.servlet.entity.testResults.TestItemResult>"
				+"<barcode>0005474</barcode>"				 //体检号
				+"<testRequisitionCode>345</testRequisitionCode>"		   //请求号，可忽略，
				+"<testItemCode>B10018</testItemCode>"			    //小项编码
				+"<resultValue>因受检者肠气干扰严重，所测数值及结果仅供参考。\n"
				+"肝脏：轮廓清晰，形态规则，肝包膜完整光滑，肝内实质光点分布均匀，肝内管道结构显示清晰。\n"
				+"胆囊：囊壁光滑，胆囊腔内液性暗区清晰，未见异常回声。\n"
				+"胰腺：大小、形态正常，边界清晰，实质光点分布均匀。\n"
				+"脾脏：轮廓清晰，形态规则，实质光点分布均匀。\n"
				+"双肾：大小正常，轮廓清晰，形态规则，实质光点分布均匀，肾盂光带未见分离。\n"
				+"输尿管：双侧输尿管无明显扩张。\n"
				+"膀胱：充盈良好，内壁光滑，腔内透声好，未见明显异常回声。\n"
				+"前列腺：大小3.6cmx4.5cmx3.8cm，轮廓增大，形态饱满，内部回声欠均匀。</resultValue>	"//项目结果
				+"<resultState></resultState>"	     //检验结果状态	，如偏高、偏低等，字符串，默认为““，如果从设备传输过来有值则以设备传输过来的值为准。如传输的为默认值，则为通过公式计算出的值。
				+"<resultRange></resultRange>"	     //检验结果上下限，字符串，默认为““，如果从设备传输过来有值则以设备传输过来的值为准。如传输的为默认值，则为通过公式计算出的值。
				+"<resultUnit></resultUnit>"	      //检验项目单位，字符串，默认为““，如果从设备传输过来有值则以设备传输过来的值为准。如传输的为默认值，则为检验项目的单位。
				+"<acuracy>-1</acuracy>"		      //精度，数值型，默认为-1，如果从设备传输过来有值则以设备传输过来的值为准。如传输的为默认值，则为检验项目的精度
				+"<testMethod></testMethod>"	       //检验方法，字符串，默认为““，如果从设备传输过来有值则以设备传输过来的值为准。如传输的为默认值，则为检验项目的检验方法
				+"<testType>-1</testType>"		       	//检验方法，字符串，默认为““，如果从设备传输过来有值则以设备传输过来的值为准。如传输的为默认值，则为检验项目的检验方法
				+"<displayIndex>-1</displayIndex>"		//孔位号，数值型，默认-1，如果从设备传输过来有值则以设备传输过来的值为准。如传输的为默认值，则会自动生成一个当天的顺序号。
				+"<todayIndex></todayIndex>"		// 当日序号，字符串，默认“-1“，如果从设备传输过来有值则以设备传输过来的值为准。如传输的为默认值，则会自动生成一个当天的顺序号。
				+"<reservedField>doctor:yq2;division:B超室;auditDoctorCode:lp;auditDate:2018-04-13;isCoverDivisionResult:true</reservedField>"	//其他信息汇总：doctor:yq2检查医生编码为yq2;division:B超室 科室为B超室；auditDoctorCode:lp 审核医生编码为lp;auditDate:2018-04-13 审核时间为2018-04-13；isCoverDivisionResult:true，当前结果是否覆盖以前结果，是
				+"</ddd.servlet.entity.testResults.TestItemResult>"
				+"<ddd.servlet.entity.testResults.TestItemResult>"
				+"<barcode>0005474</barcode>"
				+"<testRequisitionCode>345</testRequisitionCode>"
				+"<testItemCode>Summary</testItemCode>"		//小项编码，Summary代表科室小结
				+"<resultValue>双侧甲状腺;右侧锁骨下动脉粥样硬化斑块形成;双侧颈总动脉内径及血流量正常;双侧椎动脉内径及血流量正常;双侧颈内外动脉未见异常;前列腺增生;肝、胆、胰、脾、双肾、输尿管、膀胱声像图未见明显异常;二维超声心动图未见异常;多普勒心动图大致正常;左室舒张功能减低</resultValue>"
				+"<resultState></resultState>"
				+"<resultRange></resultRange>"
				+"<resultUnit></resultUnit>"
				+"<acuracy>-1</acuracy>"
				+"<testMethod></testMethod>"
				+"<testType>-1</testType>"
				+"<displayIndex>-1</displayIndex>"
				+"<todayIndex></todayIndex>"
				+"<reservedField>doctor:yq2;division:B超室;auditDoctorCode:lp;auditDate:2018-04-13;isCoverDivisionResult:true</reservedField>"
				+"</ddd.servlet.entity.testResults.TestItemResult>"
				+"<ddd.servlet.entity.testResults.TestItemResult>"
				+"<barcode>0005474</barcode>"
				+"<testRequisitionCode>345</testRequisitionCode>"
				+"<testItemCode>cslj</testItemCode>"		       //小项编码，cslj代表图像路径
				+"<resultValue>test.jpg</resultValue>"
				+"<resultState></resultState>"
				+"<resultRange></resultRange>"
				+"<resultUnit></resultUnit>"
				+"<acuracy>-1</acuracy>"
				+"<testMethod></testMethod>"
				+"<testType>-1</testType>"
				+"<displayIndex>-1</displayIndex>"
				+"<todayIndex></todayIndex>"
				+"<reservedField>doctor:yq2;division:B超室;auditDoctorCode:lp;auditDate:2018-04-13;isCoverDivisionResult:true</reservedField>"
				+"</ddd.servlet.entity.testResults.TestItemResult>"
				+"<ddd.servlet.entity.testResults.TestItemResult>"
				+"<barcode>0005474</barcode>"
				+"<testRequisitionCode>345</testRequisitionCode>"
				+"<testItemCode>cslj</testItemCode>"		       //小项编码，cslj代表图像路径
				+"<resultValue>test222.jpg</resultValue>"
				+"<resultState></resultState>"
				+"<resultRange></resultRange>"
				+"<resultUnit></resultUnit>"
				+"<acuracy>-1</acuracy>"
				+"<testMethod></testMethod>"
				+"<testType>-1</testType>"
				+"<displayIndex>-1</displayIndex>"
				+"<todayIndex></todayIndex>"
				+"<reservedField>doctor:yq2;division:B超室;auditDoctorCode:lp;auditDate:2018-04-13;isCoverDivisionResult:true</reservedField>"
				+"</ddd.servlet.entity.testResults.TestItemResult>"
				+"</testItemResults>"
				+"</ddd.servlet.entity.testResults.TestResults></body>"
				+"</ddd>";
		
		try{
			new ResPacsMessageHJ(str,true);
			System.out.print("12");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}	
}
