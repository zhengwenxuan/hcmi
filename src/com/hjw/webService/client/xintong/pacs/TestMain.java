package com.hjw.webService.client.xintong.pacs;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import chrriis.dj.nativeswing.swtimpl.NativeComponent;
import chrriis.dj.nativeswing.swtimpl.NativeInterface;
import chrriis.dj.nativeswing.swtimpl.components.JWebBrowser;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserAdapter;
import chrriis.dj.nativeswing.swtimpl.components.WebBrowserEvent;


public class TestMain extends JPanel {  
    /** 
     *  DJNativeSwing-SWT.jar
     */  
    private static final long serialVersionUID = 1L;  
    // 行分隔符  
    final static public String LS = System.getProperty("line.separator", "/n");  
    // 文件分割符  
    final static public String FS = System.getProperty("file.separator", "//");  
    //以javascript脚本获得网页全屏后大小  
    final static StringBuffer jsDimension;  
    
    private String srcurl="";

   
    static {  
        jsDimension = new StringBuffer();  
        jsDimension.append("var width = 0;").append(LS);  
        jsDimension.append("var height = 0;").append(LS);  
        jsDimension.append("if(document.documentElement) {").append(LS);  
        jsDimension.append(  
                        "  width = Math.max(width, document.documentElement.scrollWidth);")  
                .append(LS);  
        jsDimension.append(  
                        "  height = Math.max(height, document.documentElement.scrollHeight);")  
                .append(LS);  
        jsDimension.append("}").append(LS);  
        jsDimension.append("if(self.innerWidth) {").append(LS);  
        jsDimension.append("  width = Math.max(width, self.innerWidth);")  
                .append(LS);  
        jsDimension.append("  height = Math.max(height, self.innerHeight);")  
                .append(LS);  
        jsDimension.append("}").append(LS);  
        jsDimension.append("if(document.body.scrollWidth) {").append(LS);  
        jsDimension.append(  
                "  width = Math.max(width, document.body.scrollWidth);")  
                .append(LS);  
        jsDimension.append(  
                "  height = Math.max(height, document.body.scrollHeight);")  
                .append(LS);  
        jsDimension.append("}").append(LS);  
        jsDimension.append("return width + ':' + height;");  
    }  
    
    /*public   TestMain() {
    }*/
  //DJNativeSwing组件请于http://djproject.sourceforge<a href="http://lib.csdn.net/base/dotnet" class='replace_word' title=".NET知识库" target='_blank' style='color:#df3434; font-weight:bold;'>.NET</a>/main/index.html下载  
    public  TestMain(final String url, final int maxWidth, final int maxHeight, final String datetime, final String exam_num,String req_no) { 
        super(new BorderLayout());  
        BorderLayout borderLayout = new BorderLayout();
        JPanel webBrowserPanel = new JPanel(new BorderLayout());  
        final String filsname="";
        final String fileName = req_no + ".jpg";  
        this.srcurl="d:\\picture\\pacs_img\\"+datetime+"\\"+exam_num+"\\"+fileName;
        final JWebBrowser webBrowser = new JWebBrowser(null);  
        webBrowser.setBarsVisible(false);  
        webBrowser.navigate(url);  
        webBrowserPanel.add(webBrowser, BorderLayout.CENTER);  
        add(webBrowserPanel, BorderLayout.CENTER);  
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 4));  
        webBrowser.addWebBrowserListener(new WebBrowserAdapter() {  
            // 监听加载进度  
            public void loadingProgressChanged(WebBrowserEvent e) {  
                // 当加载完毕时  
                if (e.getWebBrowser().getLoadingProgress() == 100) {  
                    String result = (String) webBrowser  
                            .executeJavascriptWithResult(jsDimension.toString());  
                    int index = result == null ? -1 : result.indexOf(":");  
                    NativeComponent nativeComponent = webBrowser  
                            .getNativeComponent();  
                    Dimension originalSize = nativeComponent.getSize();  
                    Dimension imageSize = new Dimension(Integer.parseInt(result  
                            .substring(0, index)), Integer.parseInt(result  
                            .substring(index + 1)));  
                    imageSize.width = Math.max(originalSize.width,  
                            imageSize.width + 50);  
                    imageSize.height = Math.max(originalSize.height,  
                            imageSize.height + 50);  
                    nativeComponent.setSize(imageSize);  
                    BufferedImage image = new BufferedImage(imageSize.width,  
                            imageSize.height, BufferedImage.TYPE_INT_RGB);  
                    nativeComponent.paintComponent(image);  
                    nativeComponent.setSize(originalSize); 
                    System.err.println(image);
                    System.err.println(imageSize.width);
                    System.err.println(maxWidth);
                    System.err.println(imageSize.height);
                    System.err.println(maxHeight);
                    System.err.println(nativeComponent.getSize());
                  //  System.err.println(image);
                    
                    // 当网页超出目标大小时  
                    if (imageSize.width > maxWidth  
                            || imageSize.height > maxHeight) {  
                        //截图部分图形  
                        image = image.getSubimage(0, 0, maxWidth, maxHeight);  
                        /*此部分为使用缩略图 
                        int width = image.getWidth(), height = image 
                            .getHeight(); 
                         AffineTransform tx = new AffineTransform(); 
                        tx.scale((double) maxWidth / width, (double) maxHeight 
                                / height); 
                        AffineTransformOp op = new AffineTransformOp(tx, 
                                AffineTransformOp.TYPE_NEAREST_NEIGHBOR); 
                        //缩小 
                        image = op.filter(image, null);*/  
                    }  
                    try {  
                        // 输出图像  
                    	String filsname="d:\\picture\\pacs_img\\"+datetime+"\\"+exam_num+"\\"+fileName;
                    	File file = new File("d:\\picture\\pacs_img\\"+datetime+"\\"+exam_num+"\\");
                    	file.mkdirs();
                        ImageIO.write(image, "jpg", new File(filsname));  
                        System.out.println("生成文件："+filsname);
                       
                    } catch (IOException ex) {  
                        ex.printStackTrace();  
                    }  
                    // 退出操作  
                   // System.exit(0);  
                }
            }

			
            
            
        }  
        );  
        
        add(panel, BorderLayout.SOUTH);
       
    }

	public String getSrcurl() {
		return srcurl;
	}

	public void setSrcurl(String srcurl) {
		this.srcurl = srcurl;
	}  
 
    
   
 
}  


