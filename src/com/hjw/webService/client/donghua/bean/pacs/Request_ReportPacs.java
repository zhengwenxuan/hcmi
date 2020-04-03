package com.hjw.webService.client.donghua.bean.pacs;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.interfaces.util.JaxbUtil;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "Request")  
@XmlType(propOrder = {})
public class Request_ReportPacs {

	@XmlElement
	private ReturnReports ReturnReports = new ReturnReports();
	
	public static void main(String[] args) throws Exception {
		String xml = ""
+"<Request>"
+"<ReturnReports>"
+"<ReturnReport>"
+"<OrdRowID>15239_10</OrdRowID>"
+"<StudyNo>201106220570</StudyNo>"
+"<GetDocCode>demo</GetDocCode>"
+"<GetDoc>超声系统管理员</GetDoc>"
+"<ReportStatusCode>S</ReportStatusCode>"
+"<ReportStatus>已发布</ReportStatus>"
+"<UnsendCause>不知</UnsendCause>"
+"<ReportDocCode>demo</ReportDocCode>"
+"<ReportDoc>超声系统管理员</ReportDoc>"
+"<AuditDocCode>demo</AuditDocCode>"
+"<AuditDoc>超声系统管理员</AuditDoc>"
+"<ReportDate>2012-05-02</ReportDate>"
+"<AuditDate>2012-05-02</AuditDate>"
+"<ReportTime>18:20:34</ReportTime>"
+"<AuditTime>18:20:34</AuditTime>"
+"<Memo></Memo>"
+"<ImageFile></ImageFile>"
+"<HisArchiveTag></HisArchiveTag>"
+"<EyeSee>肝脏形态规则，大小正常。</EyeSee>"
+"<ExamSee>肝脏形态规则，大小正常。实质近场回声增强，远场衰减，肝内管系结构不清。</ExamSee>"
+"<Diagnose>肝脏、胆囊、胰、脾：未见明显异常。</Diagnose>"
+"</ReturnReport>"
+"</ReturnReports>"
+"</Request>";
		Request_ReportPacs request = JaxbUtil.converyToJavaBean(xml, Request_ReportPacs.class);
		System.out.println(request.getReturnReports().getReturnReport().get(0).getAuditDoc());
	}

	public ReturnReports getReturnReports() {
		return ReturnReports;
	}

	public void setReturnReports(ReturnReports returnReports) {
		ReturnReports = returnReports;
	}
}
