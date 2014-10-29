/** 
 * jar名 :  eagleye-core.jar
 * 文件名 ：  DateUtils.java
 *       (C) Copyright eagleye Corporation 2010
 *           All Rights Reserved.
 * *****************************************************************************
 *    注意： 本内容仅限于优购公司内部使用，禁止转发
 ******************************************************************************/
package com.yougou.eagleye.core.utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <PRE>
 * 作用:
 *       日期操作工具类
 * 限制:
 *       无.
 * 注意事项:
 *       无.
 * 修改历史:
 * -----------------------------------------------------------------------------
 *         VERSION       DATE                BY              CHANGE/COMMENT
 * -----------------------------------------------------------------------------
 *          1.0        2011-07-14           null              create
 * -----------------------------------------------------------------------------
 * </PRE>
 */
public class DateUtils {

	/**
	 * 根据当前日期及增加天数得到相应日期
	 * 
	 * @param nowDay
	 *            指定的初始日期,格式为: 2011-07-10
	 * @param addDays
	 *            需要增加的天数,如果为负数则是减少的天数
	 * @return
	 * @throws Exception
	 */
	public static String addDay(String nowDay, int addDays) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cd = Calendar.getInstance();
		try {
			cd.setTime(sdf.parse(nowDay));
			cd.add(Calendar.DATE, addDays);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return sdf.format(cd.getTime());
	}
	

	/**
	 * 得到当前日期yyyy-MM-dd；
	 * @return String
	 */
	public static String getSystemDate() {
		java.util.Date date = new java.util.Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
		String mDateTime1 = formatter.format(date);
		return mDateTime1;
	}
	

	/**
	 * 得到当前日期和时间yyyy-MM-dd HH:mm:ss
	 * @return String
	 */
	public static String getSystemDateAndTime() {
		java.util.Date date = new java.util.Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String mDateTime1 = formatter.format(date);
		return mDateTime1;
	}
	
	/**
	 * 得到当前年月yyyy-MM
	 * @return String
	 */
	public static String getSystemYearAndMonth() {
		java.util.Date date = new java.util.Date();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
		String mDateTime1 = formatter.format(date);
		return mDateTime1;
	}

	
	/**
	 * 得到当前系统时间HH:mm:ss；
	 * @return String
	 */
	public static String getSystemTime() {
		java.util.Date date = new java.util.Date();
		SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss");
		String mDateTime1 = formatter.format(date);
		return mDateTime1;
	}
	
	
	/**
	 * 获取两个日期之间的天数
	 * sDate -- 起始日期，eDate -- 结束日期
	 * @return int--天数
	 * @throws Exception
	 * */
	public static int subtractDate(String sDate, String eDate){
		   DateFormat df1 = new SimpleDateFormat("yyyy-MM-dd") ;
		   DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd") ;
		   Date date1 =null;
		   Date date2 =null;
		try {
			date1 = df1.parse(sDate);
			date2 = df2.parse(eDate);
		} catch (Exception e) {
			// TODO: handle exception
		}
	       if (null == date1 || null == date2) {
	           return -1;
	       }
	       long intervalMilli = date2.getTime() - date1.getTime();
	       return (int)(intervalMilli / (24 * 60 * 60 * 1000));
	 }
	
	
	 /**   
	  * 方法说明：取得本月的第一天
	  * @return   
	  */    
	 public static String getMonthFirstDay() {     
		 Calendar calendar = new GregorianCalendar();
		 calendar.set(Calendar.DATE, 1 );
		 SimpleDateFormat simpleFormate = new SimpleDateFormat( "yyyy-MM-dd" );
	     
	     return simpleFormate.format(calendar.getTime());     
	 }     
	     
	 /**   
	  * 方法说明：取得本月的最后一天
	  * @return   
	  */  
	 public static String getMonthLastDay() {     
		 Calendar calendar = new GregorianCalendar();
		 calendar.set(Calendar.DATE, 1 );
		 calendar.roll(Calendar.DATE, - 1 );
		 SimpleDateFormat simpleFormate = new SimpleDateFormat( "yyyy-MM-dd" );
	     return simpleFormate.format(calendar.getTime());     
	 }  
	 
	 /**
	  * 验证是否是日期格式
	  * @param dateStr
	  * @return
	  */
	 public static boolean verifyDateFormat(String dateStr){
		 boolean flag = false;
		 if(dateStr==null){
			 dateStr = "";
		 }
		 String regexStr= "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]? ((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][12356789])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";   
		 Pattern p = Pattern.compile(regexStr);   
		 Matcher m = p.matcher(dateStr);   
		 flag = m.matches();   
		 return flag;
	 }
	 
	 /**
	  * 方法说明：判断某一天是否是周未
	  *         返回true是周未，否则不是周未
	  * @param dateString
	  */
     public static boolean getIfIsWeekEndByDate(String dateString) {  
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		Date date;
		
		try {
			date = df.parse(dateString);
			// df.parse()返回的是一个Date类
			calendar.setTime(date);
			if (calendar.get(Calendar.DAY_OF_WEEK) == 1
					|| calendar.get(Calendar.DAY_OF_WEEK) == 7) {
				return true;
			} else {
				return false;
			}
		} catch (java.text.ParseException e) {
			e.printStackTrace();
		}

		return false;
     }  
     
	 /**
	  * 方法说明：判断某一天是是星期几
	  * @param dateString
	  */
     public static int getWeeyTypeByDate(String dateString) {  
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Calendar calendar = Calendar.getInstance();
		Date date;
		
		try {
			date = df.parse(dateString);
			// df.parse()返回的是一个Date类
			calendar.setTime(date);
				return calendar.get(Calendar.DAY_OF_WEEK);
		} catch (java.text.ParseException e) {
			e.printStackTrace();
			return 0;
		}
     } 
     
 	
	 /**
	  * 方法说明：比较两个时间的相差分钟数
	  */
	public static double getTimeDiff(String startDate,String endDate){
		Date d1 = null;
		Date d2 = null;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		try {
			d1 = sdf.parse(startDate);
			d2 = sdf.parse(endDate);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		long dd1 = d1.getTime();
		long dd2 = d2.getTime();
		double minute = (double) (dd2 - dd1) / 3600 / 1000 * 60;
		return minute;
	}
     
	
	/**
	 * 字符串转换为日期
	 * @param dateStr
	 * @param format yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static Date getDateFromString(String dateStr,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);//"yyyy-MM-dd HH:mm:ss"
		Date date = new Date();
		try {
			date = sdf.parse(dateStr);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return date;
	}
	
	
	/**
	 * 日期转换为字符串
	 * @param dateStr
	 * @param format yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getStringFromDate(Date date,String format){
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		String dateStr = formatter.format(date);
		return dateStr;
	}
	
	
	
	public static void main(String[] args){
		System.out.println(System.currentTimeMillis());
	}
	
}
