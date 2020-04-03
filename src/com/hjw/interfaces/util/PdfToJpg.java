package com.hjw.interfaces.util;

import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;

import com.hjw.util.TranLogTxt;
import com.sun.pdfview.PDFFile;
import com.sun.pdfview.PDFPage;

public class PdfToJpg {
	public static int pdf2jpg(String pdfpathfile, String jpgpath) {
		// load a pdf from a byte buffer
		File file = new File(pdfpathfile);
		RandomAccessFile raf = null;
		FileChannel channel = null;
		ByteBuffer buf = null;
		int imagnum = 0;
		try {
			raf = new RandomAccessFile(file, "rw");
			channel = raf.getChannel();
			buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
			channel.position(file.length());
			channel.write(buf);			
			PDFFile pdffile = new PDFFile(buf);
			System.out.println("页数： " + pdffile.getNumPages());
			imagnum = pdffile.getNumPages();
			for (int i = 1; i <= pdffile.getNumPages(); i++) {
				PDFPage page = pdffile.getPage(i);
				Rectangle rect = new Rectangle(0, 0, (int) page.getBBox().getWidth(), (int) page.getBBox().getHeight());
				int n=3;			
				//** 图片清晰度（n>0且n<7）【pdf放大参数】 *//*
				Image img = page.getImage(rect.width * n, rect.height * n,
						rect, //** 放大pdf到n倍，创建图片。 *//*
						null,//** null for the ImageObserver *//*
						true, //** fill background with white *//*
						true //** block until drawing is done *//*
				);
				String jpgfilepath = jpgpath + "_" + i + ".jpg";
				FileOutputStream out=null;
				
				try{				
					BufferedImage tag = new BufferedImage(rect.width * n, rect.height * n, BufferedImage.TYPE_INT_RGB);
					tag.getGraphics().drawImage(img, 0, 0, rect.width * n, rect.height * n, null);
					out = new FileOutputStream(jpgfilepath);
					//JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
					ImageIO.write(tag ,"jpg",out);
				}catch(Exception imex){
					imex.printStackTrace();
				}finally{
					if(out!=null){
						out.close();
					}					
				}				
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {							
				if (buf != null) {
					buf.clear();
				}
				if (channel != null) {
					channel.close();
				}
				if (raf != null) {
					
					raf.close();
				}
			} catch (Exception exs) {
				exs.printStackTrace();
			}
		}
		return imagnum;
	}
	
	public static int pdf2jpg(String pdfpathfile, String jpgpath,int n) {
		// load a pdf from a byte buffer
		File file = new File(pdfpathfile);
		RandomAccessFile raf = null;
		FileChannel channel = null;
		ByteBuffer buf = null;
		int imagnum = 0;
		try {
			raf = new RandomAccessFile(file, "rw");
			channel = raf.getChannel();
			buf = channel.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
			channel.position(file.length());
			channel.write(buf);			
			PDFFile pdffile = new PDFFile(buf);
			System.out.println("页数： " + pdffile.getNumPages());
			imagnum = pdffile.getNumPages();
			for (int i = 1; i <= pdffile.getNumPages(); i++) {
				PDFPage page = pdffile.getPage(i);
				Rectangle rect = new Rectangle(0, 0, (int) page.getBBox().getWidth(), (int) page.getBBox().getHeight());
				if((n<=0)||(n>=7)){
					n = 3;	
				}				
				//** 图片清晰度（n>0且n<7）【pdf放大参数】 *//*
				Image img = page.getImage(rect.width * n, rect.height * n,
						rect, //** 放大pdf到n倍，创建图片。 *//*
						null,//** null for the ImageObserver *//*
						true, //** fill background with white *//*
						true //** block until drawing is done *//*
				);
				String jpgfilepath = jpgpath + "_" + i + ".jpg";
				FileOutputStream out=null;
				
				try{				
					BufferedImage tag = new BufferedImage(rect.width * n, rect.height * n, BufferedImage.TYPE_INT_RGB);
					tag.getGraphics().drawImage(img, 0, 0, rect.width * n, rect.height * n, null);
					out = new FileOutputStream(jpgfilepath);
					ImageIO.write(tag ,"jpg",out);
				}catch(Exception imex){
					imex.printStackTrace();
				}finally{
					if(out!=null){
						out.close();
					}					
				}				
			}			
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			try {							
				if (buf != null) {
					buf.clear();
				}
				if (channel != null) {
					channel.close();
				}
				if (raf != null) {
					
					raf.close();
				}
			} catch (Exception exs) {
				exs.printStackTrace();
			}
		}
		return imagnum;
	}
	
	public int pdf2jpg1(String pdfpathfile, String jpgpath,int ysb) {		
		int imagnum = 0;
		PDDocument document = new PDDocument();  
	        try {  
	        	File pdfFile = new File(pdfpathfile);  
	            document = PDDocument.load(pdfFile, (String)null);  
	            int size = document.getNumberOfPages();  
	            List<BufferedImage> piclist = new ArrayList();   
	            for(int i=0 ; i < size; i++){  
	                BufferedImage  image = new PDFRenderer(document).renderImageWithDPI(i,ysb,ImageType.RGB);  
	                piclist.add(image); 
	                FileOutputStream out=null;
	                try{		
	                    out = new FileOutputStream(jpgpath+"_"+(imagnum+1)+ ".jpg");
	                    ImageIO.write(image ,"jpg",out);
	    				imagnum++;
	                    }catch(Exception imex){
	    					imex.printStackTrace();
	    				}finally{
	    					if(out!=null){
	    						out.close();
	    					}					
	    				}	
	            }  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }finally{  
	            if(document != null){  
	            	try{
	            		document.close();  
	            	}catch(Exception ex){
	            		
	            	}
	            }  
	        } 
		return imagnum;
	}
		
	/**
	 * 旋转
	 * @param pdfpathfile
	 * @param jpgpath
	 * @param ysb
	 * @return
	 */
	public int pdf2jpgRotate(String pdfpathfile, String jpgpath,int ysb,String logname) {		
		int imagnum = 0;
		PDDocument document = new PDDocument();  
	        try {  
	        	File pdfFile = new File(pdfpathfile);  
	            document = PDDocument.load(pdfFile, (String)null);  
	            int size = document.getNumberOfPages();  
	            List<BufferedImage> piclist = new ArrayList();   
	            for(int i=0 ; i < size; i++){  
	                BufferedImage  image = new PDFRenderer(document).renderImageWithDPI(i,ysb,ImageType.RGB);  
	                piclist.add(image); 
	                FileOutputStream out=null;
	                String filename=jpgpath+"_"+(imagnum+1)+ ".jpg";
	                try{	                	
	                    out = new FileOutputStream(filename);
	    				
	    				 ImageIO.write(image ,"jpg",out);
	    				imagnum++;
	                    }catch(Exception imex){
	    					imex.printStackTrace();
	    				}finally{
	    					if(out!=null){
	    						out.close();
	    					}					
	    				}
	             TranLogTxt.liswriteEror_to_txt(logname, "生成图片：="+filename);
	             BufferedImage src = ImageIO.read(new File(filename));  
	             TranLogTxt.liswriteEror_to_txt(logname, "转换图片：="+filename);
   		         BufferedImage des = RotateImageUtil.Rotate(src, 270);  
   		         TranLogTxt.liswriteEror_to_txt(logname, "转换270度图片：="+filename);
   		         ImageIO.write(des, "jpg", new File(filename));
   		         TranLogTxt.liswriteEror_to_txt(logname, "转换图片完成：="+filename);
	            }  
	        } catch (Exception e) {  
	            e.printStackTrace();  
	        }finally{  
	            if(document != null){  
	            	try{
	            		document.close();  
	            	}catch(Exception ex){
	            		
	            	}
	            }  
	        } 
		return imagnum;
	}
	
	/** 
     * 将宽度相同的图片，竖向追加在一起 ##注意：宽度必须相同 
     *  
     * @param piclist 
     *            文件流数组 
     * @param outPath 
     *            输出路径 
     */  
    public static void yPic(List<BufferedImage> piclist, String outPath) {// 纵向处理图片  
        if (piclist == null || piclist.size() <= 0) {  
            System.out.println("图片数组为空!");  
            return;  
        }  
        try {  
            int height = 0, // 总高度  
            width = 0, // 总宽度  
            _height = 0, // 临时的高度 , 或保存偏移高度  
            __height = 0, // 临时的高度，主要保存每个高度  
            picNum = piclist.size();// 图片的数量  
            File fileImg = null; // 保存读取出的图片  
            int[] heightArray = new int[picNum]; // 保存每个文件的高度  
            BufferedImage buffer = null; // 保存图片流  
            List<int[]> imgRGB = new ArrayList<int[]>(); // 保存所有的图片的RGB  
            int[] _imgRGB; // 保存一张图片中的RGB数据  
            for (int i = 0; i < picNum; i++) {
                buffer = piclist.get(i);  
                heightArray[i] = _height = buffer.getHeight();// 图片高度  
                if (i == 0) {  
                    width = buffer.getWidth();// 图片宽度  
                }  
                height += _height; // 获取总高度  
                _imgRGB = new int[width * _height];// 从图片中读取RGB  
                _imgRGB = buffer  
                        .getRGB(0, 0, width, _height, _imgRGB, 0, width);  
                imgRGB.add(_imgRGB);  
            }  
            _height = 0; // 设置偏移高度为0  
            // 生成新图片  
            BufferedImage imageResult = new BufferedImage(width, height,  
                    BufferedImage.TYPE_INT_BGR);  
            for (int i = 0; i < picNum; i++) {  
                __height = heightArray[i];  
                if (i != 0)  
                    _height += __height; // 计算偏移高度  
                imageResult.setRGB(0, _height, width, __height, imgRGB.get(i),  
                        0, width); // 写入流中  
            }  
            outPath=outPath+".jpg";
            File outFile = new File(outPath);  
            ByteArrayOutputStream out = new ByteArrayOutputStream();  
            ImageIO.write(imageResult, "jpg", out);// 写图片  
            byte[] b = out.toByteArray();  
            FileOutputStream output = new FileOutputStream(outFile);  
            output.write(b);  
            out.close();  
            output.close();  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }
    /**
     * icePdf技术 pdf文件转图片文件
     * @param filePath  pdf文件路径
     * @param jpgpath   保存图片路径
     * @param scale     图片清晰度
     * @param rotation  图片偏移旋转角度
     */
	public static int icePdfToJpg(String filePath, String jpgpath,float scale,float rotation) {
		Document document = new Document();
		try {
			document.setFile(filePath);
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		}
		// save page caputres to file.
		if(scale <= 0f || scale > 7f){
			scale =3f; //设置照片清晰度
		}
//		float rotation = 0f;//设置照片偏移旋转角度
		int imagnum = document.getNumberOfPages();
		// Paint each pages content to an image and write the image to file
		for (int i = 0; i < document.getNumberOfPages(); i++) {
			BufferedImage image = (BufferedImage) document.getPageImage(i, GraphicsRenderingHints.SCREEN,
					Page.BOUNDARY_CROPBOX, rotation, scale);
			RenderedImage rendImage = image;
			// capture the page image to file
			try {
				File file = new File(jpgpath + "_" + (i+1) + ".jpg");
				ImageIO.write(rendImage, "jpg", file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			image.flush();
		}
		// clean up resources
		document.dispose();
		return imagnum;
	}
	
    /**
     * icePdf技术 pdf文件转图片文件
     * @param filePath  pdf文件路径
     * @param jpgpath   保存图片路径
     */
	public static int icePdfToJpg(String filePath, String jpgpath) {
		Document document = new Document();
		try {
			document.setFile(filePath);
		// save page caputres to file.
		float scale = 2.5f; //设置照片清晰度
		float rotation = 0f;//设置照片偏移旋转角度
		int imagnum = document.getNumberOfPages();
		// Paint each pages content to an image and write the image to file
		for (int i = 0; i < document.getNumberOfPages(); i++) {
			BufferedImage image = (BufferedImage) document.getPageImage(i, GraphicsRenderingHints.SCREEN,
					Page.BOUNDARY_CROPBOX, rotation, scale);
			RenderedImage rendImage = image;
			// capture the page image to file
			try {
				File file = new File(jpgpath + "_" + (i+1) + ".jpg");
				ImageIO.write(rendImage, "jpg", file);
			} catch (IOException e) {
				e.printStackTrace();
			}
			image.flush();
		}
			// clean up resources
			document.dispose();
			return imagnum;
		} catch (Exception ex) {
			ex.printStackTrace();
			return 0;
		}
	}
   
	public static void main(String[] args) throws Exception {
		//hjwwx002
		PdfToJpg pjpg = new PdfToJpg();		

		 //读取文件
       /* BufferedReader br = null;
        StringBuffer sb = null;
        try {
        	String fileName = "D:\\tmp\\111\\1111.txt";
            br = new BufferedReader(new InputStreamReader(new FileInputStream(fileName),"GBK")); //这里可以控制编码
            sb = new StringBuffer();
            String line = null;
            while((line = br.readLine()) != null) {
                sb.append(line);
            }
            
            String pdfpath = "D:\\tmp\\111\\222.pdf";
            
            File f  = new File(pdfpath);
			if (f.exists() && f.isFile())
				f.delete();
			FileOutputStream fos = null;
			fos = new FileOutputStream(pdfpath);
			byte[] bmpfiledata64 = Base64.base64Decode(sb.toString());
			fos.write(bmpfiledata64);
			System.out.println("写入成功");
            
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (Exception e) {
                e.printStackTrace();
            }   
        }
         
        String data = new String(sb); //StringBuffer ==> String
        System.out.println("数据为==> " + data);*/
		
		
//		String pdfpath="d:\\tmp\\4444.pdf";
		String pdfpath="D:\\tmp\\111\\7777.pdf";
		String picpath="D:\\tmp\\111\\444";
		pjpg.icePdfToJpg(pdfpath,picpath,6,0);
		//pjpg.pdf2jpg1(pdfpath, picpath,100);
//		pjpg.pdf2jpg(pdfpath, picpath,7);

		//pjpg.pdf2jpg(pdfpath,picpath,7);
		//PdfToJpg.icePdfToJpg(pdfpath,picpath);
	}
}
