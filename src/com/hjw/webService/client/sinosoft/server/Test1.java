package com.hjw.webService.client.sinosoft.server;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.dom4j.Document;
import org.dom4j.io.SAXReader;

import com.hjw.wst.DTO.ProcPacsResult;

public class Test1 {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		// TODO Auto-generated method stub
		StringBuffer sb = new StringBuffer();		
		sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");

		sb.append("<exchange>");
		sb.append("  <funcid>SD08.00.003.12</funcid>");
		sb.append("  <errcode>1</errcode>");
		sb.append("  <group>");
		sb.append("    <HRC00.02>");
		sb.append("      <row>");
		sb.append("        <DE02.99.401.99>");
		sb.append("        </DE02.99.401.99>");
		sb.append("        <DE04.30.019.00>");
		sb.append("        </DE04.30.019.00>");
		sb.append("        <DE02.01.031.00>");
		sb.append("        </DE02.01.031.00>");
		sb.append("        <DE01.00.084.00>肺部低剂量  1.00  Br40  S3</DE01.00.084.00>");
		sb.append("        <DE01.00.078.00>5292</DE01.00.078.00>");
		sb.append("        <DE02.01.025.00>");
		sb.append("        </DE02.01.025.00>");
		sb.append("        <DE01.03.049.00>李杰</DE01.03.049.00>");
		sb.append("        <DE01.00.080.00>5292</DE01.00.080.00>");
		sb.append("        <DE06.00.220.00>");
		sb.append("        </DE06.00.220.00>");
		sb.append("        <DE01.00.026.00>");
		sb.append("        </DE01.00.026.00>");
		sb.append("        <DE02.01.060.00>");
		sb.append("        </DE02.01.060.00>");
		sb.append("        <DE04.30.021.00>胸部低剂量CT</DE04.30.021.00>");
		sb.append("        <DE04.50.134.00>2019/6/12 8:14:07</DE04.50.134.00>");
		sb.append("        <DE08.10.054.00>");
		sb.append("        </DE08.10.054.00>");
		sb.append("        <DE01.00.010.06>");
		sb.append("        </DE01.00.010.06>");
		sb.append("        <DE01.00.010.04>体检中心</DE01.00.010.04>");
		sb.append("        <DE01.00.010.03>");
		sb.append("        </DE01.00.010.03>");
		sb.append("        <DE55.02.099.00>胸部CT扫描未见明显异常。</DE55.02.099.00>");
		sb.append("        <DE01.00.010.01>1906110048</DE01.00.010.01>");
		sb.append("        <DE09.00.000.11>");
		sb.append("        </DE09.00.000.11>");
		sb.append("        <DE08.50.019.00>CT</DE08.50.019.00>");
		sb.append("        <DE09.00.000.12>2019-06-12 00:00:00</DE09.00.000.12>");
		sb.append("        <DE04.30.018.00>肺窗：双肺肺纹理清晰，其分布未见明显异常，双肺实质内未见异常密度影。气管居中，主气管、支气管开口通畅。纵隔窗：双侧胸廓对称，心影形态正常，心包未见积液，纵隔内及两肺门未见明显肿大淋巴结影。双侧胸膜光整、双侧胸腔未见积液。</DE04.30.018.00>");
		sb.append("        <DE02.01.009.01>");
		sb.append("        </DE02.01.009.01>");
		sb.append("        <DE02.01.005.01>");
		sb.append("        </DE02.01.005.01>");
		sb.append("        <DE01.00.079.00>");
		sb.append("        </DE01.00.079.00>");
		sb.append("        <DE01.00.083.00>CT</DE01.00.083.00>");
		sb.append("        <DE21.01.001.02>体检中心</DE21.01.001.02>");
		sb.append("        <DE51.05.097.00>胸部CT扫描未见明显异常。</DE51.05.097.00>");
		sb.append("        <DE02.01.026.00>43</DE02.01.026.00>");
		sb.append("        <DE21.01.001.01>");
		sb.append("        </DE21.01.001.01>");
		sb.append("        <DE02.01.040.00>男</DE02.01.040.00>");
		sb.append("        <DE53.99.403.00>http://10.10.10.21:8081/WEBSEARCH.ASPX?Reqno='01906110369'</DE53.99.403.00>");
		sb.append("        <DE08.50.006.00>肺部低剂量  1.00  Br40  S3</DE08.50.006.00>");
		sb.append("        <DE04.30.022.00>");
		sb.append("        </DE04.30.022.00>");
		sb.append("        <DE51.05.087.00>");
		sb.append("        </DE51.05.087.00>");
		sb.append("        <DE06.00.187.02>");
		sb.append("        </DE06.00.187.02>");
		sb.append("        <DE01.00.010.16>");
		sb.append("        </DE01.00.010.16>");
		sb.append("        <DE04.50.139.00>");
		sb.append("        </DE04.50.139.00>");
		sb.append("        <DE01.00.010.15>");
		sb.append("        </DE01.00.010.15>");
		sb.append("        <DE01.00.010.11>");
		sb.append("        </DE01.00.010.11>");
		sb.append("        <DE04.50.131.02>");
		sb.append("        </DE04.50.131.02>");
		sb.append("        <DE04.30.015.00>");
		sb.append("        </DE04.30.015.00>");
		sb.append("        <DE04.50.131.01>");
		sb.append("        </DE04.50.131.01>");
		sb.append("        <DE01.00.082.00>");
		sb.append("        </DE01.00.082.00>");
		sb.append("        <DE02.01.010.00>13806145996</DE02.01.010.00>");
		sb.append("        <DE51.05.098.00>肺窗：双肺肺纹理清晰，其分布未见明显异常，双肺实质内未见异常密度影。气管居中，主气管、支气管开口通畅。纵隔窗：双侧胸廓对称，心影形态正常，心包未见积液，纵隔内及两肺门未见明显肿大淋巴结影。双侧胸膜光整、双侧胸腔未见积液。</DE51.05.098.00>");
		sb.append("        <DE02.01.010.01>");
		sb.append("        </DE02.01.010.01>");
		sb.append("        <DE04.50.141.00>胸部低剂量CT</DE04.50.141.00>");
		sb.append("        <DE01.03.024.00>");
		sb.append("        </DE01.03.024.00>");
		sb.append("        <DE01.00.021.00>");
		sb.append("        </DE01.00.021.00>");
		sb.append("        <DE08.10.052.00>");
		sb.append("        </DE08.10.052.00>");
		sb.append("        <DE01.00.018.01>2119</DE01.00.018.01>");
		sb.append("        <DE01.00.014.00>");
		sb.append("        </DE01.00.014.00>");
		sb.append("        <DE42.01.099.00>普通</DE42.01.099.00>");
		sb.append("        <DE04.50.132.00>2019/6/12 20:53:37</DE04.50.132.00>");
		sb.append("        <DE01.00.014.01>");
		sb.append("        </DE01.00.014.01>");
		sb.append("        <DE02.99.401.00>辛国华</DE02.99.401.00>");
		sb.append("        <DE04.30.020.00>A000335</DE04.30.020.00>");
		sb.append("        <DE51.05.099.00>肺窗：双肺肺纹理清晰，其分布未见明显异常，双肺实质内未见异常密度影。气管居中，主气管、支气管开口通畅。纵隔窗：双侧胸廓对称，心影形态正常，心包未见积液，纵隔内及两肺门未见明显肿大淋巴结影。双侧胸膜光整、双侧胸腔未见积液。</DE51.05.099.00>");
		sb.append("        <DE01.00.081.00>1.3.12.2.1107.5.99.3.106853.30000019061207221290000000265</DE01.00.081.00>");
		sb.append("        <DE04.50.140.00>胸部低剂量CT</DE04.50.140.00>");
		sb.append("        <DE01.03.048.00>");
		sb.append("        </DE01.03.048.00>");
		sb.append("        <DE01.00.008.00>");
		sb.append("        </DE01.00.008.00>");
		sb.append("        <DE01.00.008.01>01906110369</DE01.00.008.01>");
		sb.append("        <DE01.03.023.00>曾凡荣</DE01.03.023.00>");
		sb.append("        <DE01.00.001.02>");
		sb.append("        </DE01.00.001.02>");
		sb.append("        <DE01.00.099.04>2019-06-12 00:00:00</DE01.00.099.04>");
		sb.append("        <DE01.00.099.05>5292</DE01.00.099.05>");
		sb.append("        <DE01.00.099.00>体检中心</DE01.00.099.00>");
		sb.append("        <DE01.00.099.01>体检中心</DE01.00.099.01>");
		sb.append("        <DE01.00.099.02>070001</DE01.00.099.02>");
		sb.append("        <DE01.00.099.03>李杰</DE01.00.099.03>");
		sb.append("        <DE04.50.133.03>2019/6/12 20:53:37</DE04.50.133.03>");
		sb.append("        <DE06.00.290.01>");
		sb.append("        </DE06.00.290.01>");
		sb.append("        <DE04.30.017.00>");
		sb.append("        </DE04.30.017.00>");
		sb.append("        <DE02.01.052.00>");
		sb.append("        </DE02.01.052.00>");
		sb.append("        <DE01.00.015.00>");
		sb.append("        </DE01.00.015.00>");
		sb.append("        <DE02.01.039.00>左冬胜</DE02.01.039.00>");
		sb.append("      </row>");
		sb.append("    </HRC00.02>");
		sb.append("    <head>");
		sb.append("      <row>");
		sb.append("        <DE02.01.060.00>体检</DE02.01.060.00>");
		sb.append("      </row>");
		sb.append("    </head>");
		sb.append("  </group>");
		sb.append("</exchange>");
		try {
			InputStream is = new ByteArrayInputStream(sb.toString().getBytes("utf-8"));
			SAXReader sax = new SAXReader();
			ProcPacsResult plr = new ProcPacsResult();
			Document document = sax.read(is);// 获取document对象,如果文档无节点，则会抛出Exception提前结束
			plr.setPacs_req_code(document.selectSingleNode("/exchange/group/HRC00.02/row/DE01.00.008.01").getText());// 申请单号
			plr.setAudit_date(document.selectSingleNode("/exchange/group/HRC00.02/row/DE04.50.133.03").getText());
			plr.setAudit_doct(document.selectSingleNode("/exchange/group/HRC00.02/row/DE01.00.099.03").getText());
			plr.setCheck_date(document.selectSingleNode("/exchange/group/HRC00.02/row/DE04.50.133.03").getText());
			plr.setCheck_doct(document.selectSingleNode("/exchange/group/HRC00.02/row/DE01.03.023.00").getText());
			plr.setExamMethod(document.selectSingleNode("/exchange/group/HRC00.02/row/DE04.50.139.00").getText());
			plr.setBodyPart(document.selectSingleNode("/exchange/group/HRC00.02/row/DE04.50.140.00").getText());
			plr.setExam_desc(document.selectSingleNode("/exchange/group/HRC00.02/row/DE51.05.099.00").getText());
			plr.setExam_num(document.selectSingleNode("/exchange/group/HRC00.02/row/DE01.00.008.01").getText());
			plr.setExam_result(document.selectSingleNode("/exchange/group/HRC00.02/row/DE51.05.097.00").getText());
			 String imgurl=document.selectSingleNode("/exchange/group/HRC00.02/row/DE53.99.403.01").getText();//图片url地址
		}catch(Exception ex){
			ex.printStackTrace();
		}
	
	}

}
