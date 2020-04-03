package com.hjw.webService.client.xintong.lis;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.hjw.webService.client.xintong.QHResolveXML;

public class ResLisMessageQH {
	public RetLisCustomeQH rc = new RetLisCustomeQH();
	private Document document;
	private String xmlmess = "";
	public ResLisMessageQH(String xmlmessage,boolean flags) throws Exception{
		if(flags){
			xmlmess=xmlmessage;
		}else{
			xmlmess=getXml_test();
		}
		 InputStream is = new ByteArrayInputStream(xmlmess.getBytes("utf-8"));
         SAXReader sax = new SAXReader();
		 this.document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
		 this.getdoctor();
		 //this.getdoctor_Item();// 关联检查项目
		 this.getNodes_doctor_coms();
		 this.getNodes_doctor_items();
	}

	private String getXml_test() {
		
		StringBuffer buffer = new StringBuffer();
		
		buffer.append("	<?xml version=\"1.0\" encoding=\"utf-8\"?>  ");
		
		buffer.append("	<ClinicalDocument xmlns=\"urn:hl7-org:v3\" xmlns:mif=\"urn:hl7-org:v3/mif\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xsi:schemaLocation=\"urn:hl7-org:v3 ..\\sdschemas\\SDA.xsd\">  ");  
		buffer.append("	  <realmCode code=\"CN\"/>                                                                                                                        ");
		buffer.append("	  <typeId root=\"2.16.840.1.113883.1.3\" extension=\"POCD_MT000040\"/>                                                                              ");
		buffer.append("	  <templateId root=\"2.16.156.10011.2.1.1.27\"/>                                                                                                  ");
		buffer.append("	  <!-- 文档流水号 -->                                                                                                                           ");
		buffer.append("	  <id root=\"2.16.156.10011.1.1\" extension=\"RN001\"/>                                                                                             ");
		buffer.append("	  <code code=\"C0007\" codeSystem=\"2.16.156.10011.2.4\" codeSystemName=\"卫生信息共享文档规范编码体系\"/>                                            ");
		buffer.append("	  <title>检验记录</title>                                                                                                                       ");
		buffer.append("	  <!-- 文档机器生成时间 -->                                                                                                                     ");
		buffer.append("	  <effectiveTime value=\"20121024154823\"/>                                                                                                       ");
		buffer.append("	  <confidentialityCode code=\"N\" codeSystem=\"2.16.840.1.113883.5.25\" codeSystemName=\"Confidentiality\" displayName=\"正常访问保密级别\"/>           ");
		buffer.append("	  <languageCode code=\"zh-CN\"/>                                                                                                                  ");
		buffer.append("	  <setId/>                                                                                                                                      ");
		buffer.append("	  <versionNumber/>                                                                                                                              ");
		buffer.append("	  <!--文档记录对象（患者）-->                                                                                                                   ");
		buffer.append("	  <recordTarget typeCode=\"RCT\" contextControlCode=\"OP\">                                                                                         ");
		buffer.append("	    <patientRole classCode=\"PAT\">                                                                                                               ");
		buffer.append("	      <!--门（急）诊号标识 -->                                                                                                                  ");
		buffer.append("	      <id root=\"2.16.156.10011.1.11\" extension=\"E10000000\"/>                                                                                    ");
		buffer.append("	      <!--住院号标识-->                                                                                                                         ");
		buffer.append("	      <id root=\"2.16.156.10011.1.12\" extension=\"HA201102113366666\"/>                                                                            ");
		buffer.append("	      <!--检验报告单号标识-->                                                                                                                   ");
		buffer.append("	      <id root=\"2.16.156.10011.1.33\" extension=\"HA201102113366666\"/>                                                                            ");
		buffer.append("	      <!--电子申请单编号-->                                                                                                                     ");
		buffer.append("	      <id root=\"2.16.156.10011.1.24\" extension=\"HA201102113366666\"/>                                                                            ");
		buffer.append("	      <!-- 检验标本编号标识 -->                                                                                                                 ");
		buffer.append("	      <id root=\"2.16.156.10011.1.14\" extension=\"213\"/>                                                                                          ");
		buffer.append("	      <!-- 患者类别代码 -->                                                                                                                     ");
		buffer.append("	      <patientType>                                                                                                                             ");
		buffer.append("	        <patienttypeCode code=\"1\" codeSystem=\"2.16.156.10011.2.3.1.271\" codeSystemName=\"患者类型代码表\" displayName=\"门诊\"/>                    ");
		buffer.append("	      </patientType>                                                                                                                            ");
		buffer.append("	      <telecom value=\"020-87815102\"/>                                                                                                           ");
		buffer.append("	      <patient classCode=\"PSN\" determinerCode=\"INSTANCE\">                                                                                       ");
		buffer.append("	        <!--患者身份证号标识-->                                                                                                                 ");
		buffer.append("	        <id root=\"2.16.156.10011.1.3\" extension=\"420106201101011919\"/>                                                                          ");
		buffer.append("	        <name>贾小明</name>                                                                                                                     ");
		buffer.append("	        <administrativeGenderCode code=\"1\" displayName=\"男性\" codeSystem=\"2.16.156.10011.2.3.3.4\" codeSystemName=\"生理性别代码表(GB/T 2261.1)\"/>   ");
		buffer.append("	        <!-- 年龄 -->                                                                                                                           ");
		buffer.append("	        <age unit=\"岁\" value=\"24\"/>                                                                                                             ");
		buffer.append("	      </patient>                                                                                                                                ");
		buffer.append("	    </patientRole>                                                                                                                              ");
		buffer.append("	  </recordTarget>                                                                                                                               ");
		buffer.append("	  <!-- 检验报告医师（文档创作者） -->                                                                                                           ");
		buffer.append("	  <author typeCode=\"AUT\" contextControlCode=\"OP\">                                                                                               ");
		buffer.append("	    <!-- 检验报告日期 -->                                                                                                                       ");
		buffer.append("	    <time value=\"20110404\"/>                                                                                                                    ");
		buffer.append("	    <assignedAuthor classCode=\"ASSIGNED\">                                                                                                       ");
		buffer.append("	      <id root=\"2.16.156.10011.1.7\" extension=\"234234234\"/>                                                                                     ");
		buffer.append("	      <!-- 医师姓名 -->                                                                                                                         ");
		buffer.append("	      <assignedPerson>                                                                                                                          ");
		buffer.append("	        <name>李医生</name>                                                                                                                     ");
		buffer.append("	      </assignedPerson>                                                                                                                         ");
		buffer.append("	    </assignedAuthor>                                                                                                                           ");
		buffer.append("	  </author>                                                                                                                                     ");
		buffer.append("	  <!-- 保管机构 -->                                                                                                                             ");
		buffer.append("	  <custodian typeCode=\"CST\">                                                                                                                    ");
		buffer.append("	    <assignedCustodian classCode=\"ASSIGNED\">                                                                                                    ");
		buffer.append("	      <representedCustodianOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">                                                              ");
		buffer.append("	        <id root=\"2.16.156.10011.1.5\" extension=\"12345678-1\"/>                                                                                  ");
		buffer.append("	        <name>xx医院</name>                                                                                                                     ");
		buffer.append("	      </representedCustodianOrganization>                                                                                                       ");
		buffer.append("	    </assignedCustodian>                                                                                                                        ");
		buffer.append("	  </custodian>                                                                                                                                  ");
		buffer.append("	  <!-- 审核医师签名 -->                                                                                                                         ");
		buffer.append("	  <legalAuthenticator>                                                                                                                          ");
		buffer.append("	    <time/>                                                                                                                                     ");
		buffer.append("	    <signatureCode/>                                                                                                                            ");
		buffer.append("	    <assignedEntity>                                                                                                                            ");
		buffer.append("	      <id root=\"2.16.156.10011.1.4\" extension=\"医务人员编号\"/>                                                                                  ");
		buffer.append("	      <code displayName=\"审核医师\"/>                                                                                                            ");
		buffer.append("	      <assignedPerson classCode=\"PSN\" determinerCode=\"INSTANCE\">                                                                                ");
		buffer.append("	        <name>刘医生</name>                                                                                                                     ");
		buffer.append("	      </assignedPerson>                                                                                                                         ");
		buffer.append("	    </assignedEntity>                                                                                                                           ");
		buffer.append("	  </legalAuthenticator>                                                                                                                         ");
		buffer.append("	  <!-- 检验技师签名 -->                                                                                                                         ");
		buffer.append("	  <authenticator>                                                                                                                               ");
		buffer.append("	    <time/>                                                                                                                                     ");
		buffer.append("	    <signatureCode/>                                                                                                                            ");
		buffer.append("	    <assignedEntity>                                                                                                                            ");
		buffer.append("	      <id root=\"2.16.156.10011.1.4\" extension=\"医务人员编号\"/>                                                                                  ");
		buffer.append("	      <code displayName=\"检验技师\"/>                                                                                                            ");
		buffer.append("	      <assignedPerson classCode=\"PSN\" determinerCode=\"INSTANCE\">                                                                                ");
		buffer.append("	        <name>钱医生</name>                                                                                                                     ");
		buffer.append("	      </assignedPerson>                                                                                                                         ");
		buffer.append("	    </assignedEntity>                                                                                                                           ");
		buffer.append("	  </authenticator>                                                                                                                              ");
		buffer.append("	  <!-- 检验医师签名 -->                                                                                                                         ");
		buffer.append("	  <authenticator>                                                                                                                               ");
		buffer.append("	    <time/>                                                                                                                                     ");
		buffer.append("	    <signatureCode/>                                                                                                                            ");
		buffer.append("	    <assignedEntity>                                                                                                                            ");
		buffer.append("	      <id root=\"2.16.156.10011.1.4\" extension=\"医务人员编号\"/>                                                                                  ");
		buffer.append("	      <code displayName=\"检查验医师\"/>                                                                                                          ");
		buffer.append("	      <assignedPerson classCode=\"PSN\" determinerCode=\"INSTANCE\">                                                                                ");
		buffer.append("	        <name>孙医生</name>                                                                                                                     ");
		buffer.append("	      </assignedPerson>                                                                                                                         ");
		buffer.append("	    </assignedEntity>                                                                                                                           ");
		buffer.append("	  </authenticator>                                                                                                                              ");
		buffer.append("	  <!-- 检验申请机构及科室 -->                                                                                                                   ");
		buffer.append("	  <participant typeCode=\"PRF\">                                                                                                                  ");
		buffer.append("	    <time/>                                                                                                                                     ");
		buffer.append("	    <associatedEntity classCode=\"ASSIGNED\">                                                                                                     ");
		buffer.append("	      <scopingOrganization>                                                                                                                     ");
		buffer.append("	        <id root=\"2.16.156.10011.1.26\" extension=\"001\"/>                                                                                        ");
		buffer.append("	        <name>检验申请科室</name>                                                                                                               ");
		buffer.append("	        <asOrganizationPartOf>                                                                                                                  ");
		buffer.append("	          <wholeOrganization>                                                                                                                   ");
		buffer.append("	            <id root=\"2.16.156.10011.1.5\" extension=\"12345678-1\"/>                                                                              ");
		buffer.append("	            <name>检验申请机构</name>                                                                                                           ");
		buffer.append("	          </wholeOrganization>                                                                                                                  ");
		buffer.append("	        </asOrganizationPartOf>                                                                                                                 ");
		buffer.append("	      </scopingOrganization>                                                                                                                    ");
		buffer.append("	    </associatedEntity>                                                                                                                         ");
		buffer.append("	  </participant>                                                                                                                                ");
		buffer.append("	  <relatedDocument typeCode=\"RPLC\">                                                                                                             ");
		buffer.append("	    <parentDocument>                                                                                                                            ");
		buffer.append("	      <id/>                                                                                                                                     ");
		buffer.append("	      <setId/>                                                                                                                                  ");
		buffer.append("	      <versionNumber/>                                                                                                                          ");
		buffer.append("	    </parentDocument>                                                                                                                           ");
		buffer.append("	  </relatedDocument>                                                                                                                            ");
		buffer.append("	  <!-- 病床号、病房、病区、科室和医院的关联 -->                                                                                                 ");
		buffer.append("	  <componentOf>                                                                                                                                 ");
		buffer.append("	    <encompassingEncounter>                                                                                                                     ");
		buffer.append("	      <effectiveTime/>                                                                                                                          ");
		buffer.append("	      <location>                                                                                                                                ");
		buffer.append("	        <healthCareFacility>                                                                                                                    ");
		buffer.append("	          <serviceProviderOrganization>                                                                                                         ");
		buffer.append("	            <asOrganizationPartOf classCode=\"PART\">                                                                                             ");
		buffer.append("	              <!-- DE01.00.026.00	病床号 -->                                                                                                  ");
		buffer.append("	              <wholeOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">                                                                     ");
		buffer.append("	                <id root=\"2.16.156.10011.1.22\" extension=\"001\"/>                                                                                ");
		buffer.append("	                <!-- DE01.00.019.00	病房号 -->                                                                                                  ");
		buffer.append("	                <asOrganizationPartOf classCode=\"PART\">                                                                                         ");
		buffer.append("	                  <wholeOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">                                                                 ");
		buffer.append("	                    <id root=\"2.16.156.10011.1.21\" extension=\"001\"/>                                                                            ");
		buffer.append("	                    <!-- DE08.10.026.00	科室名称 -->                                                                                            ");
		buffer.append("	                    <asOrganizationPartOf classCode=\"PART\">                                                                                     ");
		buffer.append("	                      <wholeOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">                                                             ");
		buffer.append("	                        <id root=\"2.16.156.10011.1.26\"/>                                                                                        ");
		buffer.append("	                        <name>呼吸内科</name>                                                                                                   ");
		buffer.append("	                        <!-- DE08.10.054.00	病区名称 -->                                                                                        ");
		buffer.append("	                        <asOrganizationPartOf classCode=\"PART\">                                                                                 ");
		buffer.append("	                          <wholeOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">                                                         ");
		buffer.append("	                            <id root=\"2.16.156.10011.1.27\"/>                                                                                    ");
		buffer.append("	                            <name>1病区</name>                                                                                                  ");
		buffer.append("	                            <!--XXX医院 -->                                                                                                     ");
		buffer.append("	                            <asOrganizationPartOf classCode=\"PART\">                                                                             ");
		buffer.append("	                              <wholeOrganization classCode=\"ORG\" determinerCode=\"INSTANCE\">                                                     ");
		buffer.append("	                                <id root=\"2.16.156.10011.1.5\" extension=\"001\"/>                                                                 ");
		buffer.append("	                                <name>XXX医院</name>                                                                                            ");
		buffer.append("	                              </wholeOrganization>                                                                                              ");
		buffer.append("	                            </asOrganizationPartOf>                                                                                             ");
		buffer.append("	                          </wholeOrganization>                                                                                                  ");
		buffer.append("	                        </asOrganizationPartOf>                                                                                                 ");
		buffer.append("	                      </wholeOrganization>                                                                                                      ");
		buffer.append("	                    </asOrganizationPartOf>                                                                                                     ");
		buffer.append("	                  </wholeOrganization>                                                                                                          ");
		buffer.append("	                </asOrganizationPartOf>                                                                                                         ");
		buffer.append("	              </wholeOrganization>                                                                                                              ");
		buffer.append("	            </asOrganizationPartOf>                                                                                                             ");
		buffer.append("	          </serviceProviderOrganization>                                                                                                        ");
		buffer.append("	        </healthCareFacility>                                                                                                                   ");
		buffer.append("	      </location>                                                                                                                               ");
		buffer.append("	    </encompassingEncounter>                                                                                                                    ");
		buffer.append("	  </componentOf>                                                                                                                                ");
		buffer.append("                                                                                                                                                 ");
		buffer.append("	  <component>                                                                                                                                   ");
		buffer.append("	    <structuredBody>                                                                                                                            ");
		buffer.append("	      <!-- 诊断记录章节-->                                                                                                                      ");
		buffer.append("	      <component>                                                                                                                               ");
		buffer.append("	        <section>                                                                                                                               ");
		buffer.append("	          <code code=\"29548-5\" displayName=\"Diagnosis\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\"/>                              ");
		buffer.append("	          <text/>                                                                                                                               ");
		buffer.append("	          <!--条目：诊断-->                                                                                                                     ");
		buffer.append("	          <entry>                                                                                                                               ");
		buffer.append("	            <observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                        ");
		buffer.append("	              <code code=\"DE05.01.024.00\" displayName=\"诊断代码\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\"/>        ");
		buffer.append("	              <!--诊断日期-->                                                                                                                   ");
		buffer.append("	              <effectiveTime value=\"20120201\"/>                                                                                                 ");
		buffer.append("	              <value xsi:type=\"CD\" code=\"M10.903\" displayName=\"痛风结节\" codeSystem=\"2.16.156.10011.2.3.3.11\" codeSystemName=\"ICD-10\"/>         ");
		buffer.append("	              <performer>                                                                                                                       ");
		buffer.append("	                <assignedEntity>                                                                                                                ");
		buffer.append("	                  <id/>                                                                                                                         ");
		buffer.append("	                  <representedOrganization>                                                                                                     ");
		buffer.append("	                    <name>诊断机构</name>                                                                                                       ");
		buffer.append("	                  </representedOrganization>                                                                                                    ");
		buffer.append("	                </assignedEntity>                                                                                                               ");
		buffer.append("	              </performer>                                                                                                                      ");
		buffer.append("	            </observation>                                                                                                                      ");
		buffer.append("	          </entry>                                                                                                                              ");
		buffer.append("	        </section>                                                                                                                              ");
		buffer.append("	      </component>                                                                                                                              ");
		buffer.append("	      <!--实验室检查章节-->                                                                                                                     ");
		buffer.append("	      <component>                                                                                                                               ");
		buffer.append("	        <section>                                                                                                                               ");
		buffer.append("	          <code code=\"30954-2\" codeSystem=\"2.16.840.1.113883.6.1\" codeSystemName=\"LOINC\" displayName=\"STUDIES SUMMARY\"/>                        ");
		buffer.append("	          <text/>                                                                                                                               ");
		buffer.append("	          <entry>                                                                                                                               ");
		buffer.append("	            <observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                        ");
		buffer.append("	              <code code=\"DE02.10.027.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"检验方法名称\"/>    ");
		buffer.append("	              <value xsi:type=\"ST\">患者接受医学检查项目所采用的检验方法名称</value>                                                             ");
		buffer.append("	            </observation>                                                                                                                      ");
		buffer.append("	          </entry>                                                                                                                              ");
		buffer.append("	          <entry>                                                                                                                               ");
		buffer.append("	            <observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                        ");
		buffer.append("	              <code code=\"DE04.30.018.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"检验类别\"/>        ");
		buffer.append("	              <value xsi:type=\"ST\">患者检验项目所属的类别详细描述</value>                                                                       ");
		buffer.append("	            </observation>                                                                                                                      ");
		buffer.append("	          </entry>                                                                                                                              ");
		buffer.append("	          <entry>                                                                                                                               ");
		buffer.append("	            <organizer classCode=\"CLUSTER\" moodCode=\"EVN\">                                                                                      ");
		buffer.append("	              <statusCode/>                                                                                                                     ");
		buffer.append("	              <component>                                                                                                                       ");
		buffer.append("	                <observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                    ");
		buffer.append("	                  <code code=\"DE04.30.019.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"检验项目代码\"/>  ");
		buffer.append("	                  <!-- 检验时间 -->                                                                                                             ");
		buffer.append("	                  <effectiveTime value=\"20120405\"/>                                                                                             ");
		buffer.append("	                  <value xsi:type=\"ST\">患者检验项目在特定编码体系中的代码</value>                                                               ");
		buffer.append("	                  <entryRelationship typeCode=\"COMP\">                                                                                           ");
		buffer.append("	                    <observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                ");
		buffer.append("	                      <code code=\"DE04.50.134.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"标本类别\"/> ");
		buffer.append("	                      <!-- DE04.50.137.00	标本采样日期时间        DE04.50.141.00	接收标本日期时间 -->                                                                               ");
		buffer.append("	                      <effectiveTime>                                                                                                           ");
		buffer.append("	                        <low value=\"20130101110103\"/>                                                                                           ");
		buffer.append("	                        <high value=\"20130102110212\"/>                                                                                          ");
		buffer.append("	                      </effectiveTime>                                                                                                          ");
		buffer.append("	                      <value xsi:type=\"ST\">标本类别</value>                                                                                     ");
		buffer.append("	                    </observation>                                                                                                              ");
		buffer.append("	                  </entryRelationship>                                                                                                          ");
		buffer.append("	                  <entryRelationship typeCode=\"COMP\">                                                                                           ");
		buffer.append("	                    <observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                ");
		buffer.append("	                      <code code=\"DE04.50.135.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"标本状态\"/> ");
		buffer.append("	                      <value xsi:type=\"ST\">标本状态</value>                                                                                     ");
		buffer.append("	                    </observation>                                                                                                              ");
		buffer.append("	                  </entryRelationship>                                                                                                          ");
		buffer.append("	                </observation>                                                                                                                  ");
		buffer.append("	              </component>                                                                                                                      ");
		buffer.append("	              <component>                                                                                                                       ");
		buffer.append("	                <observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                    ");
		buffer.append("	                  <code code=\"DE04.30.017.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"检验结果代码\"/>  ");
		buffer.append("	                  <value xsi:type=\"CD\" code=\"1\" displayName=\"异常\" codeSystem=\"2.16.156.10011.2.3.2.38\" codeSystemName=\"检查/检验结果代码表\"/>   ");
		buffer.append("	                </observation>                                                                                                                  ");
		buffer.append("	              </component>                                                                                                                      ");
		buffer.append("	              <component>                                                                                                                       ");
		buffer.append("	                <observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                    ");
		buffer.append("	                  <code code=\"DE04.30.015.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"检验定量结果\"/>");
		buffer.append("	                  <value xsi:type=\"REAL\" value=\"1.1234\"/>                                                                                       ");
		buffer.append("	                  <entryRelationship typeCode=\"COMP\">                                                                                           ");
		buffer.append("	                    <observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                ");
		buffer.append("	                      <code code=\"DE04.30.016.00\" displayName=\"检查定量结果计量单位\" codeSystemName=\"卫生信息数据元目录\" codeSystem=\"2.16.156.10011.2.2.1\"/>   ");
		buffer.append("	                      <value xsi:type=\"ST\">ml</value>                                                                                           ");
		buffer.append("	                    </observation>                                                                                                              ");
		buffer.append("	                  </entryRelationship>                                                                                                          ");
		buffer.append("	                </observation>                                                                                                                  ");
		buffer.append("	              </component>                                                                                                                      ");
		buffer.append("	            </organizer>                                                                                                                        ");
		buffer.append("	          </entry>                                                                                                                              ");
		buffer.append("	        </section>                                                                                                                              ");
		buffer.append("	      </component>                                                                                                                              ");
		buffer.append("	      <!-- 检验报告章节 -->                                                                                                                     ");
		buffer.append("	      <component>                                                                                                                               ");
		buffer.append("	        <section>                                                                                                                               ");
		buffer.append("	          <code displayName=\"检验报告\"/>                                                                                                        ");
		buffer.append("	          <text/>                                                                                                                               ");
		buffer.append("	          <entry>                                                                                                                               ");
		buffer.append("	            <observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                        ");
		buffer.append("	              <code code=\"DE04.50.130.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"检验报告结果\"/>    ");
		buffer.append("	              <value xsi:type=\"ST\">检查报告结果</value>                                                                                         ");
		buffer.append("	            </observation>                                                                                                                      ");
		buffer.append("	          </entry>                                                                                                                              ");
		buffer.append("	          <entry>                                                                                                                               ");
		buffer.append("	            <observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                        ");
		buffer.append("	              <code code=\"DE08.10.026.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"检验报告科室\"/>    ");
		buffer.append("	              <value xsi:type=\"ST\">检验报告科室</value>                                                                                         ");
		buffer.append("	            </observation>                                                                                                                      ");
		buffer.append("	          </entry>                                                                                                                              ");
		buffer.append("	          <entry>                                                                                                                               ");
		buffer.append("	            <observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                        ");
		buffer.append("	              <code code=\"DE08.10.013.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"检验报告机构名称\"/>");
		buffer.append("	              <value xsi:type=\"ST\">检验报告机构名称</value>                                                                                     ");
		buffer.append("	            </observation>                                                                                                                      ");
		buffer.append("	          </entry>                                                                                                                              ");
		buffer.append("	          <entry>                                                                                                                               ");
		buffer.append("	            <observation classCode=\"OBS\" moodCode=\"EVN\">                                                                                        ");
		buffer.append("	              <code code=\"DE06.00.179.00\" codeSystem=\"2.16.156.10011.2.2.1\" codeSystemName=\"卫生信息数据元目录\" displayName=\"检验报告备注\"/>    ");
		buffer.append("	              <value xsi:type=\"ST\">检验报告备注信息</value>                                                                                     ");
		buffer.append("	            </observation>                                                                                                                      ");
		buffer.append("	          </entry>                                                                                                                              ");
		buffer.append("	        </section>                                                                                                                              ");
		buffer.append("	      </component>                                                                                                                              ");
		buffer.append("	    </structuredBody>                                                                                                                           ");
		buffer.append("	  </component>                                                                                                                                  ");
		buffer.append("	</ClinicalDocument>                                                                                                                             ");
		
	return buffer.toString().trim();
		
}

	public void getdoctor() throws Exception {
		String checkName = document.selectSingleNode("abc:ClinicalDocument/abc:authenticator/abc:assignedEntity/abc:assignedPerson/abc:name").getText();// 获取根节点
		if(checkName != null) {
        	this.rc.setCheck_doc(checkName);
        }
		
		String reportName = document.selectSingleNode("abc:ClinicalDocument/abc:author/abc:assignedAuthor/abc:assignedPerson/abc:name").getText();// 获取根节点
		if(reportName != null) {
			this.rc.setReport_doc(reportName);
			this.rc.setReg_doc(reportName);
			this.rc.setAudit_doc(reportName);
		}
		
		String Report_date = document.selectSingleNode("abc:ClinicalDocument/abc:author/abc:time/@value").getText();// 获取根节点
		if(Report_date != null) {
			Report_date = Report_date.substring(0, 4)+"-"+Report_date.substring(4,6)+"-"+Report_date.substring(6,8)+" 00:00:00";
        	this.rc.setReport_date(Report_date);
        	this.rc.setCheck_date(Report_date);
        	//this.rc.setAudit_date(time);
        }
        
	}

	/**
	 * 
	 * @Title: getdoctor_Item @Description:
	 *         TODO(这里用一句话描述这个方法的作用) @param: @return @param: @throws
	 *         Exception @return: String @throws
	 */
	/*public void getdoctor_Item() throws Exception {
		Node Items = document.selectSingleNode("/List/LISDOCINFO/TESTITEM");// 获取根节点
		List<Element> listElement = Items.getParent().elements();// 所有一级子节点的list
		getNodes_doctor_coms(listElement);
	}*/

	@SuppressWarnings("unchecked")
	private void getNodes_doctor_coms()  throws Exception {
		
		RetLisChargeItemQH  retlisch = new RetLisChargeItemQH();
		try{
			Document documentItem = null;
			//项目编码
			String item_num= QHResolveXML.lisRportInfoNode(xmlmess,"BGJG"); //取结果放名称
			String[] str1 = item_num.split("\\|");
			if(item_num != null) {
				retlisch.setChargingItem_num(str1[1].trim());
			}
			
			//项目名称
			String item_name= QHResolveXML.lisRportInfoNode(xmlmess,"BGJG");
			String[] str2 = item_name.split("\\|");
			if(item_name != null) {
				retlisch.setChargingItem_name(str2[0].trim());
			}
			
			//检查项目结果
			/*String item_result= QHResolveXML.lisRportInfoNode(xmlmess,"BGJG");
			if(item_result != null) {
				retlisch.setItem_result(item_result);
			}*/
			List<RetLisItemQH> listRetLisItem = getNodes_doctor_items();
			retlisch.setListRetLisItem(listRetLisItem);
		}catch(Exception ex){
			ex.printStackTrace();
		}
	
		this.rc.setRetLisChargeItem(retlisch);
	}

	@SuppressWarnings("unchecked")
	private List<RetLisItemQH> getNodes_doctor_items()  throws Exception {	
		List<RetLisItemQH> listRetLisItem = QHResolveXML.lisRportItemNodes(xmlmess);
		if(listRetLisItem.size()>0){
			return listRetLisItem;
		}else{
			return null;
		}
		
	}


}
