package com.hjw.interfaces.util;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
     * @Title:  火箭蛙体检管理系统   
     * @Package com.hjw.util   
     * @Description:  
     * @author: yangm     
     * @date:   2016年7月29日 下午9:11:26   
     * @version V2.0.0.0
 */

public class DateUtil {
	/**
	 * Return current datetime string.
	 * 
	 * @return current datetime, pattern: "yyyy-MM-dd HH:mm:ss".
	 */
	public static String getDateTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dt = sdf.format(new Date());
		return dt;
	}
	
	public static Timestamp setTimestamp(){
		Timestamp timeUseForSearchInDatabase = Timestamp.valueOf(getDateTime());
		return timeUseForSearchInDatabase;
	}
	
	public static String setTimestamp(Timestamp timestamp){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  
		return sdf.format(timestamp); 
	}

	/**
	 * 获得系统时间
	 * 
	 * @author: yangm
	 * @create: Jun 21, 2006
	 * @document:
	 * @return
	 */
	public static String getDateTimes() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		String dt = sdf.format(new Date());
		return dt;
	}

	/**
	 * Return current datetime string.
	 * 
	 * @param pattern
	 *            format pattern
	 * @return current datetime
	 */
	public static String getDateTime(String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		String dt = sdf.format(new Date());
		return dt;
	}

	/**
	 * Return short format datetime string.
	 * 
	 * @param date
	 *            java.util.Date
	 * @return short format datetime
	 */
	public static String shortFmt(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return sdf.format(date);
	}
	
	public static String shortFmt2(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
	
	public static String shortFmt3(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}
	
	public static String shortFmt4(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(date);
	}
	
	public static String shortFmt5(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(date);
	}
	
	public static String shortFmt6(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return sdf.format(date);
	}


	public static String DateDiff(int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)
				- days);// 让日期加1
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dt = sdf.format(calendar.getTime());
		return dt;
	}
	
	/**
	 * 
	     * @Title: DateDiff2   
	     * @Description: 日期相减   
	     * @param: @param days
	     * @param: @return      
	     * @return: String      
	     * @throws
	 */
	public static String DateDiff2(int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)
				- days);// 让日期加1
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dt = sdf.format(calendar.getTime());
		return dt;
	}
	
	public static String DateAdd2(int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)
				+ days);// 让日期加1
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dt = sdf.format(calendar.getTime());
		return dt;
	}
	
	public static String DateAdd(int days) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH)
				+ days);// 让日期加1
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dt = sdf.format(calendar.getTime());
		return dt;
	}

	
	public static String getWeek(String datestr) {
		String dayNames[] = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五","星期六" };
		SimpleDateFormat sdfInput = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		Calendar calendar = Calendar.getInstance();
		Date date = new Date();
		try {
		date = sdfInput.parse(datestr);
		} catch (ParseException e) {
		e.printStackTrace();
		}
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)-1;
		if(dayOfWeek<0) dayOfWeek=0;
		return (dayNames[dayOfWeek]);
		
		}
	
	/**
	 * 
	 * @param date
	 * @return
	 */

	public static String changeFormat(String date) {
		return date.substring(0, 4) + "-" + date.substring(4, 6) + "-"
				+ date.substring(6, 8);
	}

	public static String getDateTimes2() {
		SimpleDateFormat sdf = new SimpleDateFormat("HHmmss");
		String dt = sdf.format(new Date());
		return dt;
	}

	/**
	 * Parse a datetime string.
	 * 
	 * @param param
	 *            datetime string, pattern: "yyyy-MM-dd HH:mm:ss".
	 * @return java.util.Date
	 */
	public static Date parse(String param) {
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date = sdf.parse(param);
		} catch (ParseException ex) {
		}
		return date;
	}
	
	/**
	 * Parse a datetime string.
	 * 
	 * @param param
	 *            datetime string, pattern: "yyyy-MM-dd HH:mm:ss".
	 * @return java.util.Date
	 */
	public static Date parse4(String param) {
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHH:mm:ss");
		try {
			date = sdf.parse(param);
		} catch (ParseException ex) {
		}
		return date;
	}
	/**
	 * Parse a datetime string.
	 * 
	 * @param param
	 *            datetime string, pattern: "yyyy-MM-dd HH:mm".
	 * @return java.util.Date
	 */
	public static Date parse0(String param) {
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = sdf.parse(param);
		} catch (ParseException ex) {
		}
		return date;
	}
	
	/**
	 * Parse a datetime string.
	 * 
	 * @param param
	 *            datetime string, pattern: "yyyy-MM-dd HH:mm:ss".
	 * @return java.util.Date
	 */
	public static Date parse2(String param) {
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			date = sdf.parse(param);
		} catch (ParseException ex) {
		}
		return date;
	}
	
	public static Date parse3(String param) {
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		try {
			date = sdf.parse(param);
		} catch (ParseException ex) {
		}
		return date;
	}
	
	public static Date parse4(Long param){
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		try {
			String d = sdf.format(param);
			System.out.println("Format To String(Date):"+d);
			date = sdf.parse(d);
		} catch (ParseException ex) {
		}
		return date;
	}

	/**
	 * Parse a datetime string.
	 * 
	 * @param param
	 *            datetime string, pattern: "yyyy-MM-dd HH:mm:ss".
	 * @return java.util.Date
	 */
	public static Date parse() {
		Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			date = sdf.parse(getDateTime());
		} catch (ParseException ex) {
		}
		return date;
	}

	/**
	 * 获得系统时间
	 * 
	 * @author: yangm
	 * @create: Jun 21, 2006
	 * @document:
	 * @return
	 */
	public static String getDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		String dt = sdf.format(new Date());
		return dt;
	}

	/**
	 * 获得系统时间
	 * 
	 * @author: yangm
	 * @create: Jun 21, 2006
	 * @document:
	 * @return
	 */
	public static String getDate2() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String dt = sdf.format(new Date());
		return dt;
	}

	/**
	 * 从形如20040320的字符串中得到形如2004年03月20日的字符串
	 * 
	 * @param yyyyMMdd
	 * @throws
	 * @return
	 */
	public static String getDateStrFromyyyyMMdd(String yyyyMMdd) {
		if (yyyyMMdd.length() != 8) {
			throw new java.lang.IllegalArgumentException("输入时间串应该形如：20040320");
		}
		return yyyyMMdd.substring(0, 4) + "年" + yyyyMMdd.substring(4, 6) + "月"
				+ yyyyMMdd.substring(6, 8) + "日";
	}

	public static String getStringMoney(String smoney) {
		if ((smoney != null) && (!smoney.equals(""))) {
			if (smoney.length() > 2) {
				smoney = smoney.substring(0, smoney.length() - 2)
						+ "."
						+ smoney.substring(smoney.length() - 2, smoney.length());
			} else if (smoney.length() == 2) {
				smoney = "0." + smoney;
			} else if (smoney.length() == 1) {
				smoney = "0.0" + smoney;
			}
		}
		return smoney;
	}

	
	  /**  
     * 得到本月的第一天  
     */ 
    public static String getMonthFirstDay(Date date) {   
        Calendar calendar = Calendar.getInstance(); 
        calendar.setTime(date);
        
        calendar.set(Calendar.DAY_OF_MONTH, calendar   
                .getActualMinimum(Calendar.DAY_OF_MONTH)); 
//        calendar.set( Calendar.DATE, 1);
        SimpleDateFormat simpleFormate = new SimpleDateFormat("yyyy-MM-dd");
        return simpleFormate.format(calendar.getTime());   
    }   
       
    /**  
     * 得到本月的最后一天   
     */ 
    public static String getMonthLastDay(Date date) {   
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        
        calendar.set(Calendar.DAY_OF_MONTH, calendar   
                .getActualMaximum(Calendar.DAY_OF_MONTH));
//        calendar.set( Calendar.DATE, 1);
//        calendar.roll(Calendar.DATE, - 1);
        SimpleDateFormat simpleFormate = new SimpleDateFormat("yyyy-MM-dd");
        return simpleFormate.format(calendar.getTime());   
    }
    
    /**
     */
    public static int compare_date(String DATE1, String DATE2,String datetype) { 
    	//"yyyy-MM-dd HH:mm:ss"
        DateFormat df = new SimpleDateFormat(datetype);
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() > dt2.getTime()) {
                System.out.println("dt1 在dt2前");
                return 1;
            } else if (dt1.getTime() < dt2.getTime()) {
                System.out.println("dt1在dt2后");
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
    
    /** 
     * 获取过去第几天的日期 
     * 
     * @param past 
     * @return 
     */  
    public static String getPastDate(int past) {  
        Calendar calendar = Calendar.getInstance();  
        calendar.set(Calendar.DAY_OF_YEAR, calendar.get(Calendar.DAY_OF_YEAR) - past);  
        Date today = calendar.getTime();  
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
        String result = format.format(today);  
        return result;  
    }
    
    /**
     * 根据年龄计算出生日期
         * @Title: getDateByAge   
         * @Description: TODO(这里用一句话描述这个方法的作用)   
         * @param: @param age
         * @param: @return      
         * @return: String      
         * @throws
     */
    public static String getDateByAge(int age) {
    	Calendar calendar = Calendar.getInstance();  
        calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR) - age);  
        Date today = calendar.getTime();  
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");  
        String result = format.format(today);  
        return result; 
    }
    
    /**
     * 获取某年后或某年前的日期
     */
    public static Date getDateAddyear(Date date,int year){
    	Calendar calendar = Calendar.getInstance();
    	calendar.setTime(date);
    	calendar.add(Calendar.YEAR, year);
    	calendar.add(Calendar.DATE, -1);
    	return calendar.getTime();
    }
	/**
     * 通过时间秒毫秒数判断两个时间的间隔
     * @param date1
     * @param date2
     * @return
     */
//    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
//    long to = df.parse("2017-11-03").getTime();
//    long from = df.parse("2017-09-03").getTime();
 //   (to - from) / (1000 * 60 * 60 * 24)
//    System.out.println();
    
    public static int riqixiangjianhuoqutianshu(Date date1,Date date2){
        int days = (int) ((date2.getTime() - date1.getTime()) / (1000*3600*24));
        return days;
    }
    /**
     * 两个 日期相减获取天数，周日，周天除外
         * @Title: getDutyDays   
         * @Description: TODO(这里用一句话描述这个方法的作用)   
         * @param: @param startDate
         * @param: @param endDate
         * @param: @return      
         * @return: int      
         * @throws
     */
    public static int getDutyDays(java.util.Date startDate,java.util.Date endDate) {  
    	int result = 0;  
    	java.text.SimpleDateFormat df = new java.text.SimpleDateFormat("yyyy-MM-dd");  
    	while (startDate.compareTo(endDate) <= 0) {  
    	if (startDate.getDay() != 6 && startDate.getDay() != 0)  
    	result++;  
    	startDate.setDate(startDate.getDate() + 1);  
    	}  
    	return result;  
    }
    /** 
     * 判断当前日期是否是周末 
     * @return 
     */  
    public static boolean isWeekend(Calendar cal){  
        int week=cal.get(Calendar.DAY_OF_WEEK)-1;  
        if(week ==6 || week==0){//0代表周日，6代表周六  
            return true;  
        }  
        return false;  
    }
    
    /**
     * 根据用户生日计算年龄
     */
    public static int getAgeByBirthday(Date birthday) {
    	Calendar cal = Calendar.getInstance();

    	if (cal.before(birthday)) {
    		throw new IllegalArgumentException(
    				"The birthDay is before Now.It's unbelievable!");
    	}

    	int yearNow = cal.get(Calendar.YEAR);
    	int monthNow = cal.get(Calendar.MONTH) + 1;
    	int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

    	cal.setTime(birthday);
    	int yearBirth = cal.get(Calendar.YEAR);
    	int monthBirth = cal.get(Calendar.MONTH) + 1;
    	int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

    	int age = yearNow - yearBirth;

    	if (monthNow <= monthBirth) {
    		if (monthNow == monthBirth) {
    			// monthNow==monthBirth 
    			if (dayOfMonthNow < dayOfMonthBirth) {
    				age--;
    			}
    		} else {
    			// monthNow>monthBirth 
    			age--;
    		}
    	}
    	
    	return age;
    }
    
    public static String getBirthdayFromIdNum(String idNum) {
		String birthday = "";
		try {
			if(idNum.length() == 18) {
				birthday = idNum.substring(6, 14);
			}else if(idNum.length() == 15){
				birthday ="19" + idNum.substring(6,12);
			}
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			Date date = sdf.parse(birthday);
			SimpleDateFormat sdfs = new SimpleDateFormat("yyyy-MM-dd");
			birthday = sdfs.format(date);
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }
		return birthday;
    }
    
    public static Date strToDateLong2(String strDate)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
        ParsePosition pos = new ParsePosition(0);
        Date strtodate = formatter.parse(strDate, pos);
        return strtodate;
    }
    
    public static void main(String[] args) {
    	Date date = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		try {
			date = sdf.parse("20181212");
		} catch (ParseException ex) {
		}
		sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	System.out.println(sdf.format(date));
	}
}
