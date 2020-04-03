package com.hjw.webService.xyyyService.util;

import com.hjw.util.TranLogTxt;

import ca.uhn.hl7v2.model.Message;
import ca.uhn.hl7v2.parser.PipeParser;
import ca.uhn.hl7v2.util.Terser;

public class MessageTypeStart {
	public static String startType(String str,String logname){
		String rest="";
		PipeParser pipeParser = new PipeParser();
		Message message;
		try {
			if(!"".equals(str)){
				
				message=pipeParser.parse(str);
				Terser terser = new Terser(message);
				String adt = terser.get("/.MSH-9-1");
				String adt1 = terser.get("/.MSH-9-2");
				StringBuilder sb=new StringBuilder();
				sb.append(adt);
				sb.append("^");
				sb.append(adt1);
				switch (sb.toString()) {
				case "ADT^A28":
					TranLogTxt.liswriteEror_to_txt(logname,"xyyyCUSTOMADDLog\r\nreq:"+str);
					rest=XYYYHL7Message.ADTA28HL7(str,logname);
					TranLogTxt.liswriteEror_to_txt(logname,"xyyyCUSTOMADDLog\r\nres:"+rest);
					break;
				case "ADT^A31":
					TranLogTxt.liswriteEror_to_txt(logname,"xyyyCUSTOMEDITLog\r\nreq:"+str);
					rest=XYYYHL7Message.ADTA31HL7(str,logname);
					TranLogTxt.liswriteEror_to_txt(logname,"xyyyCUSTOMEDITLog\r\nres:"+rest);
					break;
				case "OMG^O19":
					TranLogTxt.liswriteEror_to_txt(logname,"xyyyPACSAPPLYLog\r\nreq:"+str);
					rest=XYYYHL7Message.OMGO19HL7(str,logname);
					TranLogTxt.liswriteEror_to_txt(logname,"xyyyPACSAPPLYLog\r\nres:"+rest);
					break;
				case "OML^O21":
					TranLogTxt.liswriteEror_to_txt(logname,"xyyyLISAPPLYLog\r\nreq:"+str);
					rest=XYYYHL7Message.OMLO21HL7(str,logname);
					TranLogTxt.liswriteEror_to_txt(logname,"xyyyLISAPPLYLog\r\nres:"+rest);
					break;
				case "DFT^P03":
					TranLogTxt.liswriteEror_to_txt(logname,"xyyyPACSFEELog\r\nreq:"+str);
					rest=XYYYHL7Message.DFTP03HL7(str,logname);
					TranLogTxt.liswriteEror_to_txt(logname,"xyyyPACSFEELog\r\nres:"+rest);
					break;
				case "ORU^R01":
					TranLogTxt.liswriteEror_to_txt(logname,"xyyyPACSREPORTLog\r\nreq:"+str);
					rest=XYYYHL7Message.ORUR01HL7(str,logname);
					TranLogTxt.liswriteEror_to_txt(logname,"xyyyPACSREPORTLog\r\nres:"+rest);
					break;
				case "OUL^R24":
					TranLogTxt.liswriteEror_to_txt(logname,"xyyyLISREPORTLog\r\nreq:"+str);
					rest=XYYYHL7Message.OULR24HL7(str,logname);
					TranLogTxt.liswriteEror_to_txt(logname,"xyyyLISREPORTLog\r\nres:"+rest);
					break;
				case "MFN^M14":
					TranLogTxt.liswriteEror_to_txt(logname,"xyyyZIDIANLog\r\nreq:"+str);
					rest=XYYYHL7Message.MFNM14HL7(str,logname);
					TranLogTxt.liswriteEror_to_txt(logname,"xyyyZIDIANLog\r\nres:"+rest);
					break;
				default:
					break;
				}
			}
		} catch (Exception e) {
			TranLogTxt.liswriteEror_to_txt(logname,"\r\n"+com.hjw.interfaces.util.StringUtil.formatException(e)+"\r\n");
			e.printStackTrace();
		}
		return rest;
	}
}
