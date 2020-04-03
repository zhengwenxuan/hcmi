package com.hjw.test;

import java.io.IOException;

import com.hjw.interfaces.util.PdfToJpg;
import com.itextpdf.text.Document;
import com.itextpdf.text.pdf.PdfReader;

public class TestPDF {

	 /**  
     * 利用itext打开pdf文档  
     */  
    private static boolean check(String file) {  
        boolean flag1 = false;  
        int n = 0;  
        try {  
            Document document = new Document(new PdfReader(file).getPageSize(1));  
            document.open();  
            PdfReader reader = new PdfReader(file);  
            n = reader.getNumberOfPages();  
            if (n != 0)  
                flag1 = true;  
            document.close();  
        } catch (IOException e) {  
            e.printStackTrace();  
  
        }  
        return flag1;  
  
    }  
	public static void main(String[] args) {
		String dmd="D:\\health\\16414028.pdf";
		System.out.println(check(dmd));
		PdfToJpg pjpg = new PdfToJpg();
		pjpg.pdf2jpg(dmd, "D:\\health",200);
	}

}
