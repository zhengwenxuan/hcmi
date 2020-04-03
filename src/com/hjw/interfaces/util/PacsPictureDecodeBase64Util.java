package com.hjw.interfaces.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.hjw.util.Base64;
import com.hjw.util.Timeutils;
import com.hjw.wst.service.CommService;

public class PacsPictureDecodeBase64Util {

	private static CommService commService;
	private static String TPLJ;
	static{
	   	init();
   	}
	public static void init(){
		WebApplicationContext wac = ContextLoader.getCurrentWebApplicationContext();
		commService = (CommService) wac.getBean("commService");
		TPLJ = commService.getDatadis("TPLJ").getName();
	}
	
	public static String decodeBase64JPG(String exam_num, String req_code, String datetime, String base64Str) throws IOException {
		String depnum = commService.getDepNumForPacs(req_code);
		String picpath = TPLJ;
		File f = new File(picpath);
		if (!f.exists() && !f.isDirectory())
			f.mkdir();
		
		picpath = picpath + "\\pacs_img";
		String picname = "pacs_img";
		f = new File(picpath);
		if (!f.exists() && !f.isDirectory())
			f.mkdir();
		picpath = picpath + "\\" + datetime;
		picname = picname + "/" + datetime;
		f = new File(picpath);
		if (!f.exists() && !f.isDirectory())
			f.mkdir();
		
		picpath = picpath + "\\" + depnum;
		picname = picname + "/" + depnum;
		f = new File(picpath);
		if (!f.exists() && !f.isDirectory())
			f.mkdir();
		
		picpath = picpath + "\\" + exam_num;
		picname = picname + "/" + exam_num;
		f = new File(picpath);
		if (!f.exists() && !f.isDirectory())
			f.mkdir();
		
		picpath = picpath + "\\" + req_code;
		picname = picname + "/" + req_code;
		f = new File(picpath);
		if (!f.exists() && !f.isDirectory())
			f.mkdir();
		String jpgname = req_code + "-" + Timeutils.getFileData() + ".jpg";
		picpath = picpath + "\\" + jpgname;
		picname = picname + "/" + jpgname;
		FileOutputStream fos = null;
		try {
			f = new File(picpath);
			if (f.exists() && f.isFile())
				f.delete();
			fos = new FileOutputStream(picpath);
			// 用FileOutputStream 的write方法写入字节数组
			byte[] bmpfiledata64 = Base64.base64Decode(base64Str);
			fos.write(bmpfiledata64);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
		return picname;
	}
	
	public static String decodeBase64PDF(String exam_num, String req_code, String datetime, String base64Str) throws IOException {
		String depnum = commService.getDepNumForPacs(req_code);
		String picpath = TPLJ;
		File f = new File(picpath);
		if (!f.exists() && !f.isDirectory())
			f.mkdir();
		
		picpath = picpath + "\\pacs_img";
		String picname = "pacs_img";
		f = new File(picpath);
		if (!f.exists() && !f.isDirectory())
			f.mkdir();
		picpath = picpath + "\\" + datetime;
		picname = picname + "/" + datetime;
		f = new File(picpath);
		if (!f.exists() && !f.isDirectory())
			f.mkdir();
		
		picpath = picpath + "\\" + depnum;
		picname = picname + "/" + depnum;
		f = new File(picpath);
		if (!f.exists() && !f.isDirectory())
			f.mkdir();
		
		picpath = picpath + "\\" + exam_num;
		picname = picname + "/" + exam_num;
		f = new File(picpath);
		if (!f.exists() && !f.isDirectory())
			f.mkdir();
		
		picpath = picpath + "\\" + req_code;
		picname = picname + "/" + req_code;
		f = new File(picpath);
		if (!f.exists() && !f.isDirectory())
			f.mkdir();
		String filepath = req_code + "-" + Timeutils.getFileData() + ".pdf";
		String jpgpath = req_code;
		String pdfpath = picpath + "\\" + filepath;
		picpath = picpath + "\\" + jpgpath;
		picname = picname + "/" + jpgpath;
		FileOutputStream fos = null;
		try {
			f = new File(pdfpath);
			if (f.exists() && f.isFile())
				f.delete();
			fos = new FileOutputStream(pdfpath);
			// 用FileOutputStream 的write方法写入字节数组
			byte[] bmpfiledata64 = Base64.base64Decode(base64Str);
			fos.write(bmpfiledata64);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
		PdfToJpg pjpg = new PdfToJpg();
		int picnum = pjpg.pdf2jpg(pdfpath, picpath);
		String file_img = "";
		for (int j = 1; j <= picnum; j++) {
			file_img = file_img + ";" + picname + "_" + j + ".jpg";
		}
		if (file_img.length() > 1) {
			file_img = file_img.substring(1, file_img.length());
		}
		if (f.exists() && f.isFile())
			f.delete();
		return file_img;
	}
	
	public static String decodeBase64PDFByYSB(String exam_num, String req_code, String datetime, String base64Str, int ysb) throws IOException {
		String depnum = commService.getDepNumForPacs(req_code);
		String picpath = TPLJ;
		File f = new File(picpath);
		if (!f.exists() && !f.isDirectory())
			f.mkdir();
		
		picpath = picpath + "\\pacs_img";
		String picname = "pacs_img";
		f = new File(picpath);
		if (!f.exists() && !f.isDirectory())
			f.mkdir();
		picpath = picpath + "\\" + datetime;
		picname = picname + "/" + datetime;
		f = new File(picpath);
		if (!f.exists() && !f.isDirectory())
			f.mkdir();
		
		picpath = picpath + "\\" + depnum;
		picname = picname + "/" + depnum;
		f = new File(picpath);
		if (!f.exists() && !f.isDirectory())
			f.mkdir();
		
		picpath = picpath + "\\" + exam_num;
		picname = picname + "/" + exam_num;
		f = new File(picpath);
		if (!f.exists() && !f.isDirectory())
			f.mkdir();
		
		picpath = picpath + "\\" + req_code;
		picname = picname + "/" + req_code;
		f = new File(picpath);
		if (!f.exists() && !f.isDirectory())
			f.mkdir();
		String filepath = req_code + "-" + Timeutils.getFileData() + ".pdf";
		String jpgpath = req_code;
		String pdfpath = picpath + "\\" + filepath;
		picpath = picpath + "\\" + jpgpath;
		picname = picname + "/" + jpgpath;
		FileOutputStream fos = null;
		try {
			f = new File(pdfpath);
			if (f.exists() && f.isFile())
				f.delete();
			fos = new FileOutputStream(pdfpath);
			// 用FileOutputStream 的write方法写入字节数组
			byte[] bmpfiledata64 = Base64.base64Decode(base64Str);
			fos.write(bmpfiledata64);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
		PdfToJpg pjpg = new PdfToJpg();
		int picnum = pjpg.pdf2jpg1(pdfpath, picpath, ysb);
		String file_img = "";
		for (int j = 1; j <= picnum; j++) {
			file_img = file_img + ";" + picname + "_" + j + ".jpg";
		}
		if (file_img.length() > 1) {
			file_img = file_img.substring(1, file_img.length());
		}
		if (f.exists() && f.isFile())
			f.delete();
		return file_img;
	}
	
	public static String decodeBase64PDFByICE(String exam_num, String req_code, String datetime, String base64Str) throws IOException {
		String depnum = commService.getDepNumForPacs(req_code);
		String picpath = TPLJ;
		File f = new File(picpath);
		if (!f.exists() && !f.isDirectory())
			f.mkdir();
		
		picpath = picpath + "\\pacs_img";
		String picname = "pacs_img";
		f = new File(picpath);
		if (!f.exists() && !f.isDirectory())
			f.mkdir();
		picpath = picpath + "\\" + datetime;
		picname = picname + "/" + datetime;
		f = new File(picpath);
		if (!f.exists() && !f.isDirectory())
			f.mkdir();
		
		picpath = picpath + "\\" + depnum;
		picname = picname + "/" + depnum;
		f = new File(picpath);
		if (!f.exists() && !f.isDirectory())
			f.mkdir();
		
		picpath = picpath + "\\" + exam_num;
		picname = picname + "/" + exam_num;
		f = new File(picpath);
		if (!f.exists() && !f.isDirectory())
			f.mkdir();
		
		picpath = picpath + "\\" + req_code;
		picname = picname + "/" + req_code;
		f = new File(picpath);
		if (!f.exists() && !f.isDirectory())
			f.mkdir();
		String filepath = req_code + "-" + Timeutils.getFileData() + ".pdf";
		String jpgpath = req_code;
		String pdfpath = picpath + "\\" + filepath;
		picpath = picpath + "\\" + jpgpath;
		picname = picname + "/" + jpgpath;
		FileOutputStream fos = null;
		try {
			f = new File(pdfpath);
			if (f.exists() && f.isFile())
				f.delete();
			fos = new FileOutputStream(pdfpath);
			// 用FileOutputStream 的write方法写入字节数组
			byte[] bmpfiledata64 = Base64.base64Decode(base64Str);
			fos.write(bmpfiledata64);
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
		int picnum = PdfToJpg.icePdfToJpg(pdfpath, picpath,2.5f,0);
		String file_img = "";
		for (int j = 1; j <= picnum; j++) {
			file_img = file_img + ";" + picname + "_" + j + ".jpg";
		}
		if (file_img.length() > 1) {
			file_img = file_img.substring(1, file_img.length());
		}
		if (f.exists() && f.isFile())
			f.delete();
		return file_img;
	}
}