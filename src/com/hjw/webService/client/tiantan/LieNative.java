package com.hjw.webService.client.tiantan;

import java.util.Arrays;

import org.xvolks.jnative.JNative;  
import org.xvolks.jnative.Type;  
import org.xvolks.jnative.pointers.Pointer;

import com.hjw.util.TranLogTxt;

public class LieNative {  
 
	private String logname = "LieNative";
	private String dllName = "Lie.dll";
	
//	public static final Object LOCK = new Object();
	
	private LieNative() {}
	
	public LieNative(String dllName,String logname, boolean debug) {
		this.logname = logname;
		this.dllName = dllName;
		call("LIEInit");
		if(!debug) {
			call("LIELogin","tj","111111","799");
		}
	}

	public boolean call(String functionName, Object... args) {
	   JNative jNative = null;
  		try {
  			jNative = new JNative(dllName, functionName);
  			jNative.setRetVal(Type.INT);
  			for(int i=0; args!=null&&i<args.length; i++) {
  				Object arg = args[i];
  				if(arg == null) {
  					System.out.println("参数不支持null");
  					TranLogTxt.liswriteEror_to_txt(logname,"参数不支持null");
  				} else if(arg.getClass() == String.class) {
  					jNative.setParameter(i, Type.STRING, arg.toString());
  				} else if(arg.getClass() == Integer.class) {
  					jNative.setParameter(i, (int)arg);
  				} else if(arg.getClass() == org.xvolks.jnative.misc.basicStructures.LONG.class) {
  					jNative.setParameter(i, Type.LONG, arg.toString());
  				} else if(arg.getClass() == Pointer.class) {
//  				Pointer  dd=new Pointer(new HeapMemoryBlock(50));
//  	  			Pointer  pointer=new Pointer(MemoryBlockFactory.createMemoryBlock(4*10));
//  	  			String sd=lieNative.getMemoryAsString(2);
  					jNative.setParameter(i, (Pointer)arg);
  				}
  			}
  			TranLogTxt.liswriteEror_to_txt(logname, "准备调用方法" +functionName+ "()，参数:"+Arrays.toString(args));
  			jNative.invoke();
  			TranLogTxt.liswriteEror_to_txt(logname, "调用方法" +jNative.getFunctionName()+ "()成功，返回：" + jNative.getRetVal());
  			return jNative.getRetValAsInt() == 1;	//1:表示成功          0:表示失败
  		} catch (Throwable e) {
  			TranLogTxt.liswriteEror_to_txt(logname, "Exception:"+com.hjw.interfaces.util.StringUtil.formatException(e));
  			TranLogTxt.liswriteEror_to_txt(logname, "LIE.dll调用失败,方法名:"+functionName+"()");
  		}
  		return false;  
   }  
 
//   @Override
//	这段代码必需注释起来，否则dll一出问题就会导致tomcat闪退
//	protected void finalize() throws Throwable {
//		call("LIERelease");
//		super.finalize();
//	}
	
	public static void main(String[] args) {
		final LieNative lieNative = new LieNative("C:\\labking\\Lie.dll", "LieNative", true);
		
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					Thread.sleep(1 * 1000L);
					boolean success = lieNative.call("LIERelease");//这个不能关掉窗口，而是会将tomcat直接关闭掉
					System.out.println("LIERelease:"+success);
					
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}).start();
		
		boolean success = lieNative.call("LIEConfig");//
		System.out.println("LIEConfig:"+success);
	}
}  
