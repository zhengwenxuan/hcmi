package com.hjw.webService.client.xintong.pacs;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.hjw.util.TranLogTxt;
import com.hjw.webService.client.xintong.QHResolveXML;


public class ResPacsMessageQH {
	private RetPacsCustomeQH rc= new RetPacsCustomeQH();
	private Document document;
	private String xmlmessagess="";
	public ResPacsMessageQH(String xmlmessage,boolean flags) throws Exception{
		 if(flags){
			 xmlmessagess=xmlmessage;
			}else{
				xmlmessagess=getXml_Test();
			}
		InputStream is = new ByteArrayInputStream(xmlmessagess.getBytes("utf-8"));
		SAXReader sax = new SAXReader();// 创建一个SAXReader对象
		this.document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
		
		this.getdoctor(); 
		this.getdoctor_effectiveTime();//获取报告时间
//		this.getdoctor_orderid();//关联医嘱号或者清单号				
		this.getdoctor_Item();//关联检查项目	
		
	}

	public RetPacsCustomeQH getRetPacsCustome(){
		return this.rc;
	}
	
	
	
	private String getXml_Test() {
		
		StringBuffer buffer = new StringBuffer();
		 
		buffer.append("	<?xml version=\"1.0\" encoding=\"UTF-8\"?>   ");
		buffer.append("	<ClinicalDocument xmlns=\"urn:hl7-org:v3\" xmlns:mif=\"urn:hl7-org:v3/mif\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:hl7-org:v3 ..\\sdschemas\\SDA.xsd\">  ");
		buffer.append("		<realmCode code=\"CN\"/>                                                                                                                                                 ");
		buffer.append("		<typeId root=\"2.16.840.1.113883.1.3\" extension=\"POCD_MT000040\"/>                                                                                                       ");
		buffer.append("		<templateId root=\"2.16.156.10011.2.1.1.26\"/>                                                                                                                           ");
		buffer.append("		<!-- 文档流水号 -->                                                                                                                                                    ");
		buffer.append("		<id root=\"2.16.156.10011.1.1\" extension=\"RN001\"/>                                                                                                                      ");
		buffer.append("		<code code=\"C0006\" codeSystem=\"2.16.156.10011.2.4\" codeSystemName=\"卫生信息共享文档规范编码体系\"/>                                                                     ");
		buffer.append("		<title>检查报告</title>                                                                                                                                                ");
		buffer.append("		<!-- 文档机器生成时间 -->                                                                                                                                              ");
		buffer.append("		<effectiveTime value=\"20121024154823\"/>                                                                                                                                ");
		buffer.append("		<confidentialityCode code=\"N\" codeSystem=\"2.16.840.1.113883.5.25\" codeSystemName=\"Confidentiality\" displayName=\"正常访问保密级别\"/>                                    ");
		buffer.append("		<languageCode code=\"zh-CN\"/>                                                                                                                                           ");
		buffer.append("		<setId/>                                                                                                                                                               ");
		buffer.append("		<versionNumber/>                                                                                                                                                       ");
		buffer.append("                                                                                                                                                                            ");
		buffer.append("		<!--文档记录对象（患者）-->                                                                                                                                            ");
		buffer.append("		<recordTarget typeCode=\"RCT\" contextControlCode=\"OP\">                                                                                                                  ");
		buffer.append("			<patientRole classCode=\"PAT\">                                                                                                                                      ");
		buffer.append("				<!--门（急）诊号标识 -->                                                                                                                                       ");
		buffer.append("				<id root=\"2.16.156.10011.1.11\" extension=\"E10000000\"/>                                                                                                         ");
		buffer.append("				<!--住院号标识-->                                                                                                                                              ");
		buffer.append("				<id root=\"2.16.156.10011.1.12\" extension=\"HA201102113366666\"/>                                                                                                 ");
		buffer.append("				<!--检查报告单号标识-->                                                                                                                                        ");
		buffer.append("				<id root=\"2.16.156.10011.1.32\" extension=\"HA201102113366666\"/>                                                                                                 ");
		buffer.append("				<!--电子申请单编号-->                                                                                                                                          ");
		buffer.append("				<id root=\"2.16.156.10011.1.24\" extension=\"HA201102113366666\"/>                                                                                                 ");
		buffer.append("				<!-- 标本编号标识 -->                                                                                                                                          ");
		buffer.append("				<id root=\"2.16.156.10011.1.14\" extension=\"213\"/>                                                                                                               ");
		buffer.append("				<!-- 患者类别代码 -->                                                                                                                                          ");
		buffer.append("				<patientType>                                                                                                                                                  ");
		buffer.append("					<patienttypeCode code=\"1\" codeSystem=\"2.16.156.10011.2.3.1.271\" codeSystemName=\"患者类型代码表\" displayName=\"门诊\"></patienttypeCode>                      ");
		buffer.append("				</patientType>                                                                                                                                                 ");
		buffer.append("				<telecom value=\"020-87815102\"/>                                                                                                                                ");
		buffer.append("				<patient classCode=\"PSN\" determinerCode=\"INSTANCE\">                                                                                                            ");
		buffer.append("					<!--患者身份证号标识-->                                                                                                                                    ");
		buffer.append("					<id root=\"2.16.156.10011.1.3\" extension=\"420106201101011919\"/>                                                                                             ");
		buffer.append("					<name>贾小明</name>                                                                                                                                        ");
		buffer.append("					<administrativeGenderCode code=\"1\"  displayName=\"男性\" codeSystem=\"2.16.156.10011.2.3.3.4\" codeSystemName=\"生理性别代码表(GB/T 2261.1)\"/>                  ");
		buffer.append("					<!-- 年龄 -->                                                                                                                                              ");
		buffer.append("					<age unit=\"岁\" value=\"24\"></age>                                                                                                                           ");
		buffer.append("				</patient>                                                                                                                                                     ");
		buffer.append("			</patientRole>                                                                                                                                                     ");
		buffer.append("		</recordTarget>                                                                                                                                                        ");
		buffer.append("		                                                                                                                                                                       ");
		buffer.append("		<!-- 检查报告医师（文档创作者） -->                                                                                                                                    ");
		buffer.append("		<author typeCode=\"AUT\" contextControlCode=\"OP\">                                                                                                                        ");
		buffer.append("			                                                                                                                                                                   ");
		buffer.append("			<!-- 检查报告日期 -->                                                                                                                                              ");
		buffer.append("			<time value=\"20110404\"/>                                                                                                                                           ");
		buffer.append("			<assignedAuthor classCode=\"ASSIGNED\">                                                                                                                              ");
		buffer.append("				<id root=\"2.16.156.10011.1.7\" extension=\"234234234\"/>                                                                                                          ");
		buffer.append("				<!-- 医师姓名 -->                                                                                                                                              ");
		buffer.append("				<assignedPerson>                                                                                                                                               ");
		buffer.append("					<name>李医生</name>                                                                                                                                        ");
		buffer.append("				</assignedPerson>                                                                                                                                              ");
		buffer.append("			</assignedAuthor>                                                                                                                                                  ");
		buffer.append("		</author>                                                                                                                                                              ");
		buffer.append("		                                                                                                                                                                       ");
		buffer.append("		<!-- 保管机构 -->                                                                                                                                                      ");
		buffer.append("		<custodian typeCode=\"CST\">                                                                                                                                             ");
		buffer.append("		    <assignedCustodian classCode=\"ASSIGNED\">                                                                                                                           ");
		buffer.append("				<representedCustodianOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">                                                                                   ");
		buffer.append("					<id root=\"2.16.156.10011.1.5\" extension=\"12345678-9\"/>                                                                                                     ");
		buffer.append("					<name>xx医院</name>                                                                                                                                        ");
		buffer.append("			    </representedCustodianOrganization>                                                                                                                            ");
		buffer.append("			</assignedCustodian>                                                                                                                                               ");
		buffer.append("		</custodian>                                                                                                                                                           ");
		buffer.append("                                                                                                                                                                            ");
		buffer.append("		<!-- 审核医师签名 -->                                                                                                                                                  ");
		buffer.append("		<legalAuthenticator>                                                                                                                                                   ");
		buffer.append("			<time/>                                                                                                                                                            ");
		buffer.append("			<signatureCode/>                                                                                                                                                   ");
		buffer.append("			<assignedEntity>                                                                                                                                                   ");
		buffer.append("				<id root=\"2.16.156.10011.1.4\" extension=\"医务人员编号\"/>                                                                                                       ");
		buffer.append("				<code displayName=\"审核医师\"></code>                                                                                                                           ");
		buffer.append("				<assignedPerson classCode=\"PSN\" determinerCode=\"INSTANCE\">                                                                                                     ");
		buffer.append("					<name>刘医生</name>                                                                                                                                        ");
		buffer.append("				</assignedPerson>                                                                                                                                              ");
		buffer.append("			</assignedEntity>                                                                                                                                                  ");
		buffer.append("		</legalAuthenticator>                                                                                                                                                  ");
		buffer.append("		                                                                                                                                                                       ");
		buffer.append("		<!-- 检查技师签名 -->                                                                                                                                                  ");
		buffer.append("		<authenticator>                                                                                                                                                        ");
		buffer.append("			<time/>                                                                                                                                                            ");
		buffer.append("			<signatureCode/>                                                                                                                                                   ");
		buffer.append("			<assignedEntity>                                                                                                                                                   ");
		buffer.append("				<id root=\"2.16.156.10011.1.4\" extension=\"医务人员编号\"/>                                                                                                       ");
		buffer.append("				<code displayName=\"检查技师\"></code>                                                                                                                           ");
		buffer.append("				<assignedPerson classCode=\"PSN\" determinerCode=\"INSTANCE\">                                                                                                     ");
		buffer.append("					<name>钱医生</name>                                                                                                                                        ");
		buffer.append("				</assignedPerson>                                                                                                                                              ");
		buffer.append("			</assignedEntity>                                                                                                                                                  ");
		buffer.append("		</authenticator>                                                                                                                                                       ");
		buffer.append("		                                                                                                                                                                       ");
		buffer.append("		<!-- 检查医师签名 -->                                                                                                                                                  ");
		buffer.append("		<authenticator>                                                                                                                                                        ");
		buffer.append("			<time/>                                                                                                                                                            ");
		buffer.append("			<signatureCode/>                                                                                                                                                   ");
		buffer.append("			<assignedEntity>                                                                                                                                                   ");
		buffer.append("				<id root=\"2.16.156.10011.1.4\" extension=\"医务人员编号\"/>                                                                                                       ");
		buffer.append("				<code displayName=\"检查医师\"></code>                                                                                                                           ");
		buffer.append("				<assignedPerson classCode=\"PSN\" determinerCode=\"INSTANCE\">                                                                                                     ");
		buffer.append("					<name>孙医生</name>                                                                                                                                        ");
		buffer.append("				</assignedPerson>                                                                                                                                              ");
		buffer.append("			</assignedEntity>                                                                                                                                                  ");
		buffer.append("		</authenticator>                                                                                                                                                       ");
		buffer.append("		                                                                                                                                                                       ");
		buffer.append("		<!-- 检查申请机构及科室 -->                                                                                                                                            ");
		buffer.append("		<participant typeCode=\"PRF\">                                                                                                                                           ");
		buffer.append("		<time/>                                                                                                                                                                ");
		buffer.append("			<associatedEntity classCode=\"ASSIGNED\">                                                                                                                            ");
		buffer.append("				<scopingOrganization>                                                                                                                                          ");
		buffer.append("					<id root=\"2.16.156.10011.1.26\"/>                                                                                                                           ");
		buffer.append("					<name>检查申请科室</name>                                                                                                                                  ");
		buffer.append("					<asOrganizationPartOf>                                                                                                                                     ");
		buffer.append("						<wholeOrganization>                                                                                                                                    ");
		buffer.append("							<id root=\"2.16.156.10011.1.5\" extension=\"12345678-9\"/>                                                                                             ");
		buffer.append("							<name>检查申请机构</name>                                                                                                                          ");
		buffer.append("						</wholeOrganization>                                                                                                                                   ");
		buffer.append("					</asOrganizationPartOf>                                                                                                                                    ");
		buffer.append("				</scopingOrganization>                                                                                                                                         ");
		buffer.append("			</associatedEntity>                                                                                                                                                ");
		buffer.append("		</participant>                                                                                                                                                         ");
		buffer.append("		                                                                                                                                                                       ");
		buffer.append("		<relatedDocument typeCode=\"RPLC\">                                                                                                                                      ");
		buffer.append("			<parentDocument>                                                                                                                                                   ");
		buffer.append("				<id/>                                                                                                                                                          ");
		buffer.append("				<setId/>                                                                                                                                                       ");
		buffer.append("				<versionNumber/>                                                                                                                                               ");
		buffer.append("			</parentDocument>                                                                                                                                                  ");
		buffer.append("		</relatedDocument>                                                                                                                                                     ");
		buffer.append("                                                                                                                                                                            ");
		buffer.append("		<!-- 病床号、病房、病区、科室和医院的关联 -->                                                                                                                          ");
		buffer.append("		<componentOf>                                                                                                                                                          ");
		buffer.append("			<encompassingEncounter>                                                                                                                                            ");
		buffer.append("				<effectiveTime/>                                                                                                                                               ");
		buffer.append("				<location>                                                                                                                                                     ");
		buffer.append("					<healthCareFacility>                                                                                                                                       ");
		buffer.append("						<serviceProviderOrganization>                                                                                                                          ");
		buffer.append("							<asOrganizationPartOf classCode=\"PART\">                                                                                                            ");
		buffer.append("								<!-- DE01.00.026.00	病床号 -->                                                                                                                 ");
		buffer.append("								<wholeOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">                                                                                  ");
		buffer.append("									<id root=\"2.16.156.10011.1.22\" extension=\"001\"/>                                                                                           ");
		buffer.append("									<!-- DE01.00.019.00	病房号 -->                                                                                                             ");
		buffer.append("									<asOrganizationPartOf classCode=\"PART\">                                                                                                    ");
		buffer.append("										<wholeOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">                                                                          ");
		buffer.append("											<id root=\"2.16.156.10011.1.21\" extension=\"001\"/>                                                                                   ");
		buffer.append("											<!-- DE08.10.026.00	科室名称 -->                                                                                                   ");
		buffer.append("											<asOrganizationPartOf classCode=\"PART\">                                                                                            ");
		buffer.append("												<wholeOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">                                                                  ");
		buffer.append("													<id root=\"2.16.156.10011.1.26\"/>                                                                                           ");
		buffer.append("													<name>呼吸内科</name>                                                                                                      ");
		buffer.append("													<!-- DE08.10.054.00	病区名称 -->                                                                                           ");
		buffer.append("													<asOrganizationPartOf classCode=\"PART\">                                                                                    ");
		buffer.append("														<wholeOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">                                                          ");
		buffer.append("															<id root=\"2.16.156.10011.1.27\"/>                                                                                   ");
		buffer.append("															<name>1病区</name>                                                                                                 ");
		buffer.append("															<!--XXX医院 -->                                                                                                    ");
		buffer.append("															<asOrganizationPartOf classCode=\"PART\">                                                                            ");
		buffer.append("																<wholeOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">                                                  ");
		buffer.append("																	<id root=\"2.16.156.10011.1.5\" extension=\"001\"/>                                                            ");
		buffer.append("																	<name>XXX医院</name>                                                                                       ");
		buffer.append("																</wholeOrganization>                                                                                           ");
		buffer.append("															</asOrganizationPartOf>                                                                                            ");
		buffer.append("														</wholeOrganization>                                                                                                   ");
		buffer.append("													</asOrganizationPartOf>                                                                                                    ");
		buffer.append("												</wholeOrganization>                                                                                                           ");
		buffer.append("											</asOrganizationPartOf>                                                                                                            ");
		buffer.append("										</wholeOrganization>                                                                                                                   ");
		buffer.append("									</asOrganizationPartOf>                                                                                                                    ");
		buffer.append("								</wholeOrganization>                                                                                                                           ");
		buffer.append("							</asOrganizationPartOf>                                                                                                                            ");
		buffer.append("						</serviceProviderOrganization>                                                                                                                         ");
		buffer.append("					</healthCareFacility>                                                                                                                                      ");
		buffer.append("				</location>                                                                                                                                                    ");
		buffer.append("			</encompassingEncounter>                                                                                                                                           ");
		buffer.append("		</componentOf>                                                                                                                                                         ");
		buffer.append("		                                                                                                                                                                       ");
		buffer.append("		<component>                                                                                                                                                            ");
		buffer.append("			<structuredBody>                                                                                                                                                   ");
		buffer.append("                                                                                                                                                                            ");
		buffer.append("	            <!-- 诊断记录章节-->                                                                                                                                           ");
		buffer.append("				<component>                                                                                                                                                    ");
		buffer.append("					<section>                                                                                                                                                  ");
		buffer.append("	                    <code code=\"29548-5\" displayName=\"Diagnosis\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\"/>                                               ");
		buffer.append("	                    <text/>                                                                                                                                                ");
		buffer.append("	                    <!--条目：诊断-->                                                                                                                                      ");
		buffer.append("	                    <entry>                                                                                                                                                ");
		buffer.append("	                        <observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                                       ");
		buffer.append("	                            <code code=\"DE05.01.024.00\" displayName=\"诊断代码\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\"/>                     ");
		buffer.append("	                            <!--诊断日期-->                                                                                                                                ");
		buffer.append("	                            <effectiveTime value=\"20120201\"></effectiveTime>                                                                                               ");
		buffer.append("	                            <value xsi:type=\"CD\" code=\"N84.100\" displayName=\"宫颈息肉\" codeSystem=\"2.16.156.10011.2.3.3.11\" codeSystemName=\"ICD-10\"></value>               ");
		buffer.append("	                            <performer>                                                                                                                                    ");
		buffer.append("									<assignedEntity>                                                                                                                           ");
		buffer.append("										<id/>                                                                                                                                  ");
		buffer.append("										<representedOrganization>                                                                                                              ");
		buffer.append("											<name>诊断机构</name>                                                                                                              ");
		buffer.append("										</representedOrganization>                                                                                                             ");
		buffer.append("									</assignedEntity>                                                                                                                          ");
		buffer.append("								</performer>                                                                                                                                   ");
		buffer.append("	                        </observation>                                                                                                                                     ");
		buffer.append("	                    </entry>                                                                                                                                               ");
		buffer.append("	               </section>                                                                                                                                                  ");
		buffer.append("	            </component>                                                                                                                                                   ");
		buffer.append("	                                                                                                                                                                           ");
		buffer.append("				<!-- 主诉章节 -->                                                                                                                                              ");
		buffer.append("				<component>                                                                                                                                                    ");
		buffer.append("					<section>                                                                                                                                                  ");
		buffer.append("						<code code=\"10154-3\" displayName=\"CHIEF COMPLAINT\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\"/>                                         ");
		buffer.append("						<text/>                                                                                                                                                ");
		buffer.append("						<!--主诉条目-->                                                                                                                                        ");
		buffer.append("						<entry>                                                                                                                                                ");
		buffer.append("							<observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                                       ");
		buffer.append("								<code code=\"DE04.01.119.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"主诉\"/>                         ");
		buffer.append("								<value xsi:type=\"ST\">对患者本次疾病相关的主要症状及其持续时间的描述，一般由患者本人或监护人描述</value>                                        ");
		buffer.append("							</observation>                                                                                                                                     ");
		buffer.append("						</entry>                                                                                                                                               ");
		buffer.append("					</section>                                                                                                                                                 ");
		buffer.append("				</component>		                                                                                                                                           ");
		buffer.append("                                                                                                                                                                            ");
		buffer.append("				<!-- 症状章节 -->                                                                                                                                              ");
		buffer.append("				<component>                                                                                                                                                    ");
		buffer.append("					<section>                                                                                                                                                  ");
		buffer.append("						<code code=\"11450-4\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"PROBLEM LIST\"/>                                            ");
		buffer.append("						<text/>                                                                                                                                                ");
		buffer.append("						<!-- 症状描述条目 -->                                                                                                                                     ");
		buffer.append("						<entry>                                                                                                                                                   ");
		buffer.append("							<observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                                          ");
		buffer.append("								<code code=\"DE04.01.117.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"症状描述\"/>                        ");
		buffer.append("								<!-- 症状开始时间与停止时间 -->                                                                                                                   ");
		buffer.append("								<effectiveTime>                                                                                                                                   ");
		buffer.append("									<low value=\"20120202\"></low>                                                                                                                  ");
		buffer.append("									<high value=\"20120208\"></high>                                                                                                                ");
		buffer.append("								</effectiveTime>                                                                                                                                  ");
		buffer.append("								<value xsi:type=\"ST\">症状描述</value>                                                                                                             ");
		buffer.append("							</observation>                                                                                                                                        ");
		buffer.append("						</entry>                                                                                                                                                  ");
		buffer.append("					</section>                                                                                                                                                    ");
		buffer.append("				</component>                                                                                                                                                      ");
		buffer.append("                                                                                                                                                                               ");
		buffer.append("				<!-- 手术操作章节 -->                                                                                                                                             ");
		buffer.append("				<component>                                                                                                                                                       ");
		buffer.append("					<section>                                                                                                                                                     ");
		buffer.append("						<code code=\"47519-4\" displayName=\"HISTORY OF PROCEDURES\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\"/>                                      ");
		buffer.append("						<text/>                                                                                                                                                   ");
		buffer.append("						<entry>                                                                                                                                                   ");
		buffer.append("							<!-- 1..1 手术记录 -->                                                                                                                                ");
		buffer.append("							<procedure classCode=\"PROC\" moodCode=\"EVN\">                                                                                                           ");
		buffer.append("								<code code=\"1\"  displayName=\"操作名称\" codeSystem=\"2.16.156.10011.2.3.3.12\" codeSystemName=\"ICD-9-CM-3\"/>                                         ");
		buffer.append("								<statusCode/>                                                                                                                                     ");
		buffer.append("								<!--操作日期/时间-->                                                                                                                              ");
		buffer.append("								<effectiveTime value=\"200004071430\"/>                                                                                                             ");
		buffer.append("								<!-- 操作部位代码 -->                                                                                                                             ");
		buffer.append("								<targetSiteCode code=\"操作部位编码\" displayName=\"操作部位名称\" codeSystem=\"2.16.156.10011.2.3.1.266\" codeSystemName=\"操作部位代码表\"/>            ");
		buffer.append("                                                                                                                                                                               ");
		buffer.append("								<entryRelationship typeCode=\"COMP\">                                                                                                               ");
		buffer.append("									<observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                                  ");
		buffer.append("										<code code=\"DE06.00.094.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"手术（操作）名称\"/>        ");
		buffer.append("										<value xsi:type=\"ST\">名称描述</value>                                                                                                     ");
		buffer.append("									</observation>                                                                                                                                ");
		buffer.append("								</entryRelationship>                                                                                                                              ");
		buffer.append("								<entryRelationship typeCode=\"COMP\">                                                                                                               ");
		buffer.append("									<observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                                  ");
		buffer.append("										<code code=\"DE08.50.037.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"介入物名称\"/>              ");
		buffer.append("										<value xsi:type=\"ST\">介入物名称</value>                                                                                                   ");
		buffer.append("									</observation>                                                                                                                                ");
		buffer.append("								</entryRelationship>                                                                                                                              ");
		buffer.append("								<entryRelationship typeCode=\"COMP\">                                                                                                               ");
		buffer.append("									<observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                                  ");
		buffer.append("										<code code=\"DE06.00.251.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"操作方法描述\"/>            ");
		buffer.append("										<value xsi:type=\"ST\">操作方法描述</value>                                                                                                 ");
		buffer.append("									</observation>                                                                                                                                ");
		buffer.append("								</entryRelationship>                                                                                                                              ");
		buffer.append("								                                                                                                                                                  ");
		buffer.append("								<entryRelationship typeCode=\"COMP\">                                                                                                               ");
		buffer.append("									<observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                                  ");
		buffer.append("										<code code=\"DE06.00.250.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"操作次数\"/>                ");
		buffer.append("										<value xsi:type=\"ST\">12</value>                                                                                                           ");
		buffer.append("									</observation>                                                                                                                                ");
		buffer.append("								</entryRelationship>                                                                                                                              ");
		buffer.append("															                                                                                                                      ");
		buffer.append("								<!-- 0..1 麻醉信息 -->                                                                                                                            ");
		buffer.append("								<entryRelationship typeCode=\"COMP\">                                                                                                               ");
		buffer.append("									<observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                                  ");
		buffer.append("										<code code=\"DE06.00.073.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"麻醉方式代码\"/>            ");
		buffer.append("										<value code=\"1\" codeSystem=\"2.16.156.10011.2.3.1.159\"  displayName=\"全身麻醉\" codeSystemName=\"麻醉方法代码表\" xsi:type=\"CD\"></value>      ");
		buffer.append("										<!-- 麻醉医师签名 -->                                                                                                                     ");
		buffer.append("										<performer>                                                                                                                               ");
		buffer.append("											<assignedEntity>                                                                                                                      ");
		buffer.append("												<id></id>                                                                                                                         ");
		buffer.append("												<assignedPerson>                                                                                                                  ");
		buffer.append("													<name>小明</name>                                                                                                             ");
		buffer.append("												</assignedPerson>                                                                                                                 ");
		buffer.append("											</assignedEntity>                                                                                                                     ");
		buffer.append("										</performer>                                                                                                                              ");
		buffer.append("									</observation>                                                                                                                                ");
		buffer.append("								</entryRelationship>                                                                                                                              ");
		buffer.append("								<entryRelationship typeCode=\"COMP\">                                                                                                               ");
		buffer.append("									<observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                                  ");
		buffer.append("										<code code=\"DE02.10.028.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"麻醉观察结果\"/>            ");
		buffer.append("										<value xsi:type=\"ST\">麻醉观察结果</value>                                                                                                 ");
		buffer.append("									</observation>                                                                                                                                ");
		buffer.append("								</entryRelationship>                                                                                                                              ");
		buffer.append("                                                                                                                                                                               ");
		buffer.append("								<entryRelationship typeCode=\"COMP\">                                                                                                               ");
		buffer.append("									<observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                                  ");
		buffer.append("										<code code=\"DE06.00.307.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"麻醉中西医标识代码\"/>      ");
		buffer.append("										<value code=\"1\" codeSystem=\"2.16.156.10011.2.3.2.41\" displayName='西医麻醉' codeSystemName=\"麻醉中西医标识代码表\" xsi:type=\"CD\"></value>  ");
		buffer.append("									</observation>                                                                                                                                ");
		buffer.append("								</entryRelationship>                                                                                                                              ");
		buffer.append("							</procedure>                                                                                                                                          ");
		buffer.append("						</entry>                                                                                                                                                  ");
		buffer.append("					</section>                                                                                                                                                    ");
		buffer.append("				</component>                                                                                                                                                      ");
		buffer.append("                                                                                                                                                                               ");
		buffer.append("				<!-- 体格检查章节-->                                                                                                                                              ");
		buffer.append("				<component>                                                                                                                                                       ");
		buffer.append("					<section>                                                                                                                                                     ");
		buffer.append("						<code code=\"29545-1\" displayName=\"PHYSICAL EXAMINATION\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\"/>                                       ");
		buffer.append("						<text/>                                                                                                                                                   ");
		buffer.append("						<!--特殊检查条目-->                                                                                                                                       ");
		buffer.append("						<entry>                                                                                                                                                   ");
		buffer.append("							<observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                                          ");
		buffer.append("								<code code=\"DE02.01.079.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"特殊检查标志\"/>                    ");
		buffer.append("								<value xsi:type=\"ST\">F</value>                                                                                                                    ");
		buffer.append("							</observation>                                                                                                                                        ");
		buffer.append("						</entry>                                                                                                                                                  ");
		buffer.append("                                                                                                                                                                               ");
		buffer.append("						<entry>                                                                                                                                                   ");
		buffer.append("							<observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                                          ");
		buffer.append("								<code code=\"DE02.10.027.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"检查方法名称\"></code>              ");
		buffer.append("								<value xsi:type=\"ST\">患者接受医学检查项目所采用的检查方法名称</value>                                                                             ");
		buffer.append("							</observation>                                                                                                                                        ");
		buffer.append("						</entry>                                                                                                                                                  ");
		buffer.append("						<entry>                                                                                                                                                   ");
		buffer.append("							<observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                                          ");
		buffer.append("								<code code=\"DE04.30.018.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"检查类别\"></code>                  ");
		buffer.append("								<value xsi:type=\"ST\">患者检查项目所属的类别详细描述</value>                                                                                       ");
		buffer.append("							</observation>                                                                                                                                        ");
		buffer.append("						</entry>                                                                                                                                                  ");
		buffer.append("						<entry>                                                                                                                                                   ");
		buffer.append("							<organizer classCode=\"CLUSTER\" moodCode=\"EVN\">                                                                                                        ");
		buffer.append("								<statusCode></statusCode>                                                                                                                         ");
		buffer.append("								<component>                                                                                                                                       ");
		buffer.append("									<observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                                  ");
		buffer.append("										<code code=\"DE04.30.019.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"检查项目代码\"></code>      ");
		buffer.append("										<!-- 检查日期 -->                                                                                                                         ");
		buffer.append("										<effectiveTime value=\"20120405\"/>                                                                                                         ");
		buffer.append("										<value xsi:type=\"ST\">患者检查项目在特定编码体系中的代码</value>                                                                           ");
		buffer.append("										<entryRelationship typeCode=\"COMP\">                                                                                                       ");
		buffer.append("											<observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                          ");
		buffer.append("												<code code=\"DE04.50.134.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"标本类别\"></code>  ");
		buffer.append("												<!-- DE04.50.137.00	标本采样日期时间                                                                                              ");
		buffer.append("			DE04.50.141.00	接收标本日期时间 -->                                                                                                                                  ");
		buffer.append("												<effectiveTime>                                                                                                                   ");
		buffer.append("													<low value=\"20130101110103\"></low>                                                                                            ");
		buffer.append("													<high value=\"20130102110212\"></high>                                                                                          ");
		buffer.append("												</effectiveTime>                                                                                                                  ");
		buffer.append("												<value xsi:type=\"ST\">标本类别</value>                                                                                             ");
		buffer.append("											</observation>                                                                                                                        ");
		buffer.append("										</entryRelationship>                                                                                                                      ");
		buffer.append("				                                                                                                                                                                  ");
		buffer.append("										<entryRelationship typeCode=\"COMP\">                                                                                                       ");
		buffer.append("											<observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                          ");
		buffer.append("												<code code=\"DE04.50.135.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"标本状态\"></code>  ");
		buffer.append("												<value xsi:type=\"ST\">标本状态</value>                                                                                             ");
		buffer.append("											</observation>                                                                                                                        ");
		buffer.append("										</entryRelationship>                                                                                                                      ");
		buffer.append("										<entryRelationship typeCode=\"COMP\">                                                                                                       ");
		buffer.append("											<observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                          ");
		buffer.append("												<code code=\"DE08.50.027.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"标本固定液名称\"></code>  ");
		buffer.append("												<value xsi:type=\"ST\">标本固定液名称</value>                                                                                       ");
		buffer.append("											</observation>                                                                                                                        ");
		buffer.append("										</entryRelationship>						                                                                                              ");
		buffer.append("									</observation>                                                                                                                                ");
		buffer.append("								</component>                                                                                                                                      ");
		buffer.append("								<component>                                                                                                                                       ");
		buffer.append("									<observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                                  ");
		buffer.append("										<code code=\"DE04.30.017.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"检查结果代码\"></code>      ");
		buffer.append("										<value xsi:type=\"CD\" code=\"1\" displayName=\"异常\" codeSystem=\"2.16.156.10011.2.3.2.38\" codeSystemName=\"检查/检验结果代码表\"></value>       ");
		buffer.append("									</observation>                                                                                                                                ");
		buffer.append("								</component>							                                                                                                          ");
		buffer.append("								<component>                                                                                                                                       ");
		buffer.append("									<observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                                  ");
		buffer.append("										<code code=\"DE04.30.015.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"检查定量结果\"></code>      ");
		buffer.append("										<value xsi:type=\"REAL\" value=\"1.7777\"></value>                                                                                            ");
		buffer.append("										<entryRelationship typeCode=\"COMP\">                                                                                                       ");
		buffer.append("											<observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                          ");
		buffer.append("												<code code=\"DE04.30.016.00\" displayName=\"检查定量结果计量单位\" codeSystemName=\"卫生信息数据元目录\" codeSystem=\"2.16.156.10011.2.2.1\"  />  ");
		buffer.append("												<value xsi:type=\"ST\">ml</value>                                                                                                   ");
		buffer.append("											</observation>                                                                                                                        ");
		buffer.append("										</entryRelationship>                                                                                                                      ");
		buffer.append("									</observation>                                                                                                                                ");
		buffer.append("								</component>							                                                                                                          ");
		buffer.append("							</organizer>                                                                                                                                          ");
		buffer.append("						</entry>                                                                                                                                                  ");
		buffer.append("					</section>                                                                                                                                                    ");
		buffer.append("				</component>                                                                                                                                                      ");
		buffer.append("                                                                                                                                                                               ");
		buffer.append("				<!--  其他处置章节 -->                                                                                                                                            ");
		buffer.append("				<component>                                                                                                                                                       ");
		buffer.append("					<section>                                                                                                                                                     ");
		buffer.append("						<code displayName=\"其他处置章节\"/>                                                                                                                        ");
		buffer.append("						<text/>                                                                                                                                                   ");
		buffer.append("						<!-- 诊疗过程描述　-->                                                                                                                                    ");
		buffer.append("						<entry typeCode=\"COMP\">                                                                                                                                   ");
		buffer.append("							<observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                                          ");
		buffer.append("								<code code=\"DE06.00.296.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"诊疗过程描述\"/>                    ");
		buffer.append("								<value xsi:type=\"ST\">对患者诊疗过程的详细描述</value>                                                                                             ");
		buffer.append("							</observation>                                                                                                                                        ");
		buffer.append("						</entry>                                                                                                                                                  ");
		buffer.append("					</section>                                                                                                                                                    ");
		buffer.append("				</component>	                                                                                                                                                  ");
		buffer.append("                                                                                                                                                                               ");
		buffer.append("				<!-- 检查报告章节 -->                                                                                                                                             ");
		buffer.append("				<component>                                                                                                                                                       ");
		buffer.append("					<section>                                                                                                                                                     ");
		buffer.append("						<code displayName=\"检查报告\"/>                                                                                                                            ");
		buffer.append("						<text/>                                                                                                                                                   ");
		buffer.append("						<entry>                                                                                                                                                   ");
		buffer.append("							<observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                                          ");
		buffer.append("								<code code=\"DE04.50.131.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"检查报告结果-客观所见\"/>           ");
		buffer.append("								<value xsi:type=\"ST\">检查报告结果-客观所见</value>                                                                                                ");
		buffer.append("							</observation>                                                                                                                                        ");
		buffer.append("						</entry>                                                                                                                                                  ");
		buffer.append("						<entry>                                                                                                                                                   ");
		buffer.append("							<observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                                          ");
		buffer.append("								<code code=\"DE04.50.132.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"检查报告结果-主观提示\"/>           ");
		buffer.append("								<value xsi:type=\"ST\">检查报告结果-主观提示</value>                                                                                                ");
		buffer.append("							</observation>                                                                                                                                        ");
		buffer.append("						</entry>                                                                                                                                                  ");
		buffer.append("						<entry>                                                                                                                                                   ");
		buffer.append("							<observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                                          ");
		buffer.append("								<code code=\"DE08.10.026.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"检查报告科室\"/>                    ");
		buffer.append("								<value xsi:type=\"ST\">检查报告科室</value>                                                                                                         ");
		buffer.append("							</observation>                                                                                                                                        ");
		buffer.append("						</entry>                                                                                                                                                  ");
		buffer.append("						<entry>                                                                                                                                                   ");
		buffer.append("							<observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                                          ");
		buffer.append("								<code code=\"DE08.10.013.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"检查报告机构名称\"/>                ");
		buffer.append("								<value xsi:type=\"ST\">检查报告机构名称</value>                                                                                                     ");
		buffer.append("							</observation>                                                                                                                                        ");
		buffer.append("						</entry>                                                                                                                                                  ");
		buffer.append("						<entry>                                                                                                                                                   ");
		buffer.append("							<observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                                          ");
		buffer.append("								<code code=\"DE06.00.179.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"检查报告备注\"/>                    ");
		buffer.append("								<value xsi:type=\"ST\">检查报告备注信息</value>                                                                                                     ");
		buffer.append("							</observation>                                                                                                                                        ");
		buffer.append("						</entry>	                                                                                                                                              ");
		buffer.append("					</section>                                                                                                                                                    ");
		buffer.append("				</component>                                                                                                                                                      ");
		buffer.append("			</structuredBody>                                                                                                                                                     ");
		buffer.append("		</component>                                                                                                                                                              ");
		buffer.append("	</ClinicalDocument>                                                                                                                                                           ");
			
		
		//返回加密数据
		return buffer.toString().trim();                                                                                                                                                                                              
	}
	
	public void getdoctor() throws Exception {
		// 检查报告医师
		String reportName = document.selectSingleNode("abc:ClinicalDocument/abc:author/abc:assignedAuthor/abc:assignedPerson/abc:name").getText();// 获取根节点
		if(reportName != null) {
			
			this.rc.setCheck_doc(reportName);
			this.rc.setReg_doc(reportName);
		}
		String checkName = document.selectSingleNode("abc:ClinicalDocument/abc:legalAuthenticator/abc:assignedEntity/abc:assignedPerson/abc:name").getText();// 获取根节点
		if(checkName != null) {
			this.rc.setReport_doc(checkName);
			this.rc.setAudit_doc(reportName);
		}
		
		String Report_date = document.selectSingleNode("abc:ClinicalDocument/abc:author/abc:time/@value").getText();// 获取根节点
		String Check_date = document.selectSingleNode("abc:ClinicalDocument/abc:legalAuthenticator/abc:time/@value").getText();// 获取根节点
		if(Report_date != null) {
			Report_date = Report_date.substring(0, 4)+"-"+Report_date.substring(4,6)+"-"+Report_date.substring(6,8)+" 00:00:00";
			Check_date = Check_date.substring(0, 4)+"-"+Check_date.substring(4,6)+"-"+Check_date.substring(6,8)+" 00:00:00";
        	this.rc.setReport_date(Report_date);
        	this.rc.setCheck_date(Check_date);
        	//this.rc.setAudit_date(time);
        }
        
	}
	
	public void getdoctor_effectiveTime() throws Exception {
		String time = document.selectSingleNode("abc:ClinicalDocument/abc:effectiveTime/@value").getText();// 获取根节点
		if(time != null) {
			this.rc.setEffectiveTime(time);
		}
	}
	
	public void getdoctor_orderid() throws Exception {
		String report_id = document.selectSingleNode("abc:ClinicalDocument/abc:id/@extension").getText();// 获取根节点
        this.rc.setPacs_checkno(report_id);
	}
	
	private void getdoctor_Item() throws Exception {
		String chargingItem_code = QHResolveXML.pacsRportInfoNode(xmlmessagess,"BBDM");
		String[] str1 = chargingItem_code.split("\\|");
		if(chargingItem_code != null) {
			this.rc.getPacsItem().setChargingItem_num(str1[0].trim());
			this.rc.getPacsItem().setChargingItem_name(str1[1].trim());
		}
		
		/*String chargingItem_name = QHResolveXML.pacsRportInfoNode(xmlmessagess,"BBDM");
		String[] str2 = chargingItem_name.split("\\|");
		if(chargingItem_name != null) {
			this.rc.getPacsItem().setChargingItem_name(str2[1].trim());
		}*/
		
		//结论
		String chargingItem_jl = QHResolveXML.pacsRportInfoNode(xmlmessagess,"BGJG");
		this.rc.getPacsItem().setChargingItem_jl(chargingItem_jl);
		
		//描述
		String chargingItem_ms = QHResolveXML.pacsRportInfoNode(xmlmessagess,"BGMS");
		//String chargingItem_ms = document.selectSingleNode("abc:ClinicalDocument/abc:component/abc:structuredBody/abc:component/abc:section/abc:entry/abc:observation/abc:value").getText();// 获取根节点
		this.rc.getPacsItem().setChargingItem_ms(chargingItem_ms);
		
		/*Node node = document.selectSingleNode("/List/RISREPORT/BODYPARTS");// 获取根节点
		if(node != null) {
			this.rc.getPacsItem().setBodyPart(node.getText().trim());
		}*/
		
		String check_fun = QHResolveXML.pacsRportInfoNode(xmlmessagess,"JCFF");
		if(check_fun != null) {
			this.rc.getPacsItem().setExamMethod(check_fun);
		}
		
		//64位编码图片
		//node = document.selectSingleNode("/List/RISREPORT/IMAGELIST/IMAGE");  //abc:value[@code='02']
		String img = "123"; 
		 //img = document.selectSingleNode("abc:ClinicalDocument/abc:component/abc:structuredBody/abc:component/abc:section/abc:entry/abc:observation/abc:value").getText();// 获取根节点
		 img = QHResolveXML.pacsRportInfoNode(xmlmessagess,"PACSIMG");
		 this.rc.getPacsItem().setBase64_bg(img);
		/* if(img != null) {
			this.rc.getPacsItem().setBase64_bg(img);
		}*/
	}

	public static void main(String[] args)throws Exception  {
		try{
			new ResPacsMessageQH("",false);
			System.out.print("12");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}	
}
