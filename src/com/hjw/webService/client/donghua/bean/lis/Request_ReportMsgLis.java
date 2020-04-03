package com.hjw.webService.client.donghua.bean.lis;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import com.hjw.interfaces.util.JaxbUtil;

@XmlAccessorType(XmlAccessType.FIELD)  
@XmlRootElement(name = "Request")  
@XmlType(propOrder = {})
public class Request_ReportMsgLis {

	@XmlElement
	private ReportMsgs ReportMsgs = new ReportMsgs();
	
	public static void main(String[] args) throws Exception {
		String xml = ""
+"<Request>"
	+"<ReportMsg>"
		+"<OrdID>880353||3</OrdID>"
		+"<LabNo></LabNo>"
		+"<RecUserCode>1398</RecUserCode>"
		+"<RecUser>1398</RecUser>"
		+"<RecDate>2010-08-09</RecDate>"
		+"<RecTime>09:44:00</RecTime>"
		+"<EntryUser>1398</EntryUser>"
		+"<EntryUserCode>1398</EntryUserCode>"
		+"<EntryDate>2010-08-09</EntryDate>"
		+"<EntryTime>09:44:00</EntryTime>"
		+"<AuthUser>2598</AuthUser>"
		+"<AuthUserCode>2598</AuthUserCode>"
		+"<AuthDate>2010-08-09</AuthDate>"
		+"<AuthTime>10:35:46</AuthTime>"
		+"<Notes/>"
		+"<ImageFile></ImageFile>"
		+"<MainWarnDesc></MainWarnDesc>"
		+"<ResultMsgs>"
			+"<ResultMsg>"
				+"<LabNo>123</LabNo>"
				+"<TestCode>INR</TestCode>"
				+"<TestName>国际标准化比值</TestName>"
				+"<TestEngName>INR</TestEngName>"
				+"<Result>2.05</Result>"
				+"<Units/>"
				+"<Notes/>"
				+"<ResultFlag/>"
				+"<Ranges/>"
				+"<Sequence/>"
				+"<MICFlag>N</MICFlag>"
				+"<MICName/>"
				+"<WarnDesc></WarnDesc>"
			+"</ResultMsg>"
			+"<ResultMsg>"
				+"<LabNo></LabNo>"
				+"<TestCode>PT_sec</TestCode>"
				+"<TestName>凝血酶原时间</TestName>"
				+"<TestEngName>PT_sec</TestEngName>"
				+"<Result>23.80</Result>"
				+"<Units>sec</Units>"
				+"<Notes/>"
				+"<ResultFlag/>"
				+"<Ranges/>"
				+"<Sequence/>"
				+"<MICFlag>N</MICFlag>"
				+"<MICName/>"
				+"<WarnDesc></WarnDesc>"
				+"<OrgResultMsgs>"
					+"<OrgResultMsg>"
					+"<BugsCode></BugsCode>"
					+"<AntiCode>465</AntiCode>"
					+"<AntiName></AntiName>"
					+"<AntiEngName></AntiEngName>"
					+"<MICRes></MICRes>"
					+"<KBRes></KBRes>"
					+"<Result></Result>"
					+"<MICRanges></MICRanges>"
					+"<KBRanges></KBRanges>"
					+"<Order></Order>"
					+"</OrgResultMsg>"
				+"</OrgResultMsgs>"
			+"</ResultMsg>"
		+"</ResultMsgs>"
	+"</ReportMsg>"
+"</Request>";
		Request_ReportMsgLis request = JaxbUtil.converyToJavaBean(xml, Request_ReportMsgLis.class);
		System.out.println(request.getReportMsgs().getReportMsg().get(0).getResultMsgs().getResultMsg().get(0).getLabNo());
		System.out.println(request.getReportMsgs().getReportMsg().get(0).getResultMsgs().getResultMsg().get(1).getOrgResultMsgs().getOrgResultMsg().get(0).getAntiCode());
	}

	public ReportMsgs getReportMsgs() {
		return ReportMsgs;
	}
	public void setReportMsgs(ReportMsgs reportMsgs) {
		ReportMsgs = reportMsgs;
	}
}
