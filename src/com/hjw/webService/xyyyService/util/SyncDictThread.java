package com.hjw.webService.xyyyService.util;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;

public class SyncDictThread extends TimerTask{
	  @Override
	    public void run()
	    {
	        SimpleDateFormat sdf = null;
	        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
	        System.out.println("当前时间：" + sdf.format(new Date()));
	        
	        List<String> tableList=new ArrayList<String>();
	        tableList.add("xy_zd_charge_item");
	        tableList.add("xy_zd_jy_apply_item");
	        tableList.add("xy_zd_jy_apply_detail");
	        tableList.add("xy_yz_order_charge");
	        tableList.add("xy_yz_order_item");
	        tableList.add("xy_jc_zd_exam_sub_type");
	        
	        for(int i=0;i<tableList.size();i++){
	        	switch(tableList.get(i)){
	        	case "xy_zd_charge_item":
	        		SyncDictStart.xy_zd_charge_item();
	        		break;
	        	case "xy_zd_jy_apply_item":
	        		SyncDictStart.xy_zd_jy_apply_item();
	        		break;
	        	case "xy_zd_jy_apply_detail":
	        		SyncDictStart.xy_zd_jy_apply_detail();
	        		break;
	        	case "xy_yz_order_charge":
	        		SyncDictStart.xy_yz_order_charge();
	        		break;
	        	case "xy_yz_order_item":
	        		SyncDictStart.xy_yz_order_item();
	        		break;
	        	case "xy_jc_zd_exam_sub_type":
	        		SyncDictStart.xy_jc_zd_exam_sub_type();
	        		break;
	        	default:
					break;
	        	}
	        }
	    }

}
