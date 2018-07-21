package com.hitler.core.utils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.joda.time.DateTime;

/*
 * 日期时间格式转换
 * @author onsoul by JT 2015-6-24 下午4:32:23
 * */
public class DateUtil {

	public final static String YYYYMMDDHH24MISS = "yyyyMMddHHmmss";
	
	public final static String YYYYMMDD = "yyyyMMdd";

	/**
	 * 将长时间格式字符串转换为时间 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param strDate
	 * @return
	 */
	public static Date strToDate(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}

	/**
	 * 将长时间格式时间转换为字符串 yyyy-MM-dd HH:mm:ss
	 * 
	 * @param dateDate
	 * @return
	 */
	public static String dateToStr(java.util.Date dateDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String dateString = formatter.format(dateDate);
		return dateString;
	}
	
	public static String dateToStrOnRi(java.util.Date dateDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		String dateString = formatter.format(dateDate);
		return dateString;
	}


	/**
	 * 将时间格式为YYYYMMDDHH24MISS的字符串转化为Date
	 * 
	 * @param dateStr
	 *            时间格式为YYYYMMDDHH24MISS的字符串
	 * @return Date
	 */
	public static Date str2Date(String dateStr) {
		return str2Date(dateStr, YYYYMMDDHH24MISS);
	}
	
	

	/**
	 * 时间串转化为Date
	 * 
	 * @param dateStr
	 *            dateFormat时间格式的字符串
	 * @param dateFormat
	 *            时间格式
	 * @return Date
	 */
	public static Date str2Date(String dateStr, String dateFormat) {
		if (StringUtils.isEmpty(dateStr)) {
			return null;
		}

		SimpleDateFormat df = new SimpleDateFormat(dateFormat);
		try {
			return df.parse(dateStr);
		} catch (Exception ex) {
			return null;
		}
	}

	/**
	 * Date转化为YYYYMMDDHH24MISS格式的字符串
	 * 
	 * @param date
	 *            Date
	 * @return YYYYMMDDHH24MISS格式的字符串
	 */
	public static String date2Str(Date date) {
		return date2Str(date, YYYYMMDDHH24MISS);
	}

	/**
	 * Date转化为dateFormat时间格式的字符串
	 * 
	 * @param date
	 *            Date
	 * @param dateFormat
	 *            时间格式
	 * @return dateFormat时间格式的字符串
	 */
	public static String date2Str(Date date, String dateFormat) {
		if (date == null) {
			return null;
		}
		SimpleDateFormat df = new SimpleDateFormat(dateFormat);
		return df.format(date);
	}

	// 获取当前时间后n个小时
	public static DateTime afterHourToNowDateTime(int n) {
		Calendar calendar = Calendar.getInstance();
		/* HOUR_OF_DAY 指示一天中的小时 */
		calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + n);
		return new DateTime(calendar);
	}

	public static Date afterHourToNowDate(int n) {
		Calendar calendar = Calendar.getInstance();
		/* HOUR_OF_DAY 指示一天中的小时 */
		calendar.set(Calendar.HOUR_OF_DAY, calendar.get(Calendar.HOUR_OF_DAY) + n);
		return calendar.getTime();
	}

	/**
	 * 将传入的时间格式化成 yyyy:MM:dd HH:mm:ss 格式
	 */
	public static String timeStr(Date _Date) {
		if (_Date == null)
			return "";
		SimpleDateFormat _SimpleDateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE);
		return _SimpleDateFormat.format(_Date);
	}

	/**
	 * 将传入时间格式化成 yyyy-MM-dd 格式字符串
	 */
	public static String dateStr(Date _Date) {
		if (_Date == null)
			return "";
		SimpleDateFormat _SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE);
		return _SimpleDateFormat.format(_Date);
	}

	/**
	 * 将传入 yyyy-MM-dd字符串 转换成Date对象
	 * 
	 * @param _Date
	 * @return
	 */
	public static Date formatDate(String _Date) throws ParseException {
		if (_Date == null)
			return new Date();
		SimpleDateFormat _SimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.SIMPLIFIED_CHINESE);
		return _SimpleDateFormat.parse(_Date);
	}

	/**
	 * 将传入的时间字符串转换成时间格式
	 */
	public static Date formatDate2(String _Date) throws ParseException {
		SimpleDateFormat _SimpleDateFormat = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE);
		return _SimpleDateFormat.parse(_Date);
	}

	/**
	 * 将传入时间格式化成 yyyy/MM/dd HH:mm:ss 格式字符串
	 * 
	 * @param _Date
	 * @return
	 */
	public static String formatStr(Date _Date) {
		if (_Date == null)
			return null;
		SimpleDateFormat _SimpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE);
		return _SimpleDateFormat.format(_Date);
	}
	
	public static String formatYYYYMMDDStr(Date _Date) {
		if (_Date == null)
			return null;
		SimpleDateFormat _SimpleDateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.SIMPLIFIED_CHINESE);
		return _SimpleDateFormat.format(_Date);
	}
	public static String formatYYYYMMDDStr(String dateStr) {
		if (dateStr == null)
			return null;
		SimpleDateFormat _SimpleDateFormat = new SimpleDateFormat("yyyy/MM/dd", Locale.SIMPLIFIED_CHINESE);
		try {
			return _SimpleDateFormat.format(formatDate(dateStr));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static Date getBeforeDay(int n) {
		Calendar calendar = Calendar.getInstance();// 此时打印它获取的是系统当前时间
		calendar.add(Calendar.DATE, n); // 得到前一天
		return calendar.getTime();
	}

	public static String getToday() {
		Calendar calendar = Calendar.getInstance();// 此时打印它获取的是系统当前时间
		String today = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
		return today;
	}
	
	public static String getNow() {
		Calendar calendar = Calendar.getInstance();// 此时打印它获取的是系统当前时间
		String today = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
		return today;
	}

	public static String getAddDay(Date date, int i) {
		Calendar calendar = Calendar.getInstance();// 此时打印它获取的是系统当前时间
		calendar.setTime(date);
		calendar.add(Calendar.DATE, +i); // 得到前一天
		String yestedayDate = new SimpleDateFormat("yyyy-MM-dd").format(calendar.getTime());
		return yestedayDate;
	}

	public static long getTimeSlot(Date date_late, Date date_early) {
		try {
			long hour = (date_late.getTime() - date_early.getTime()) / (1000 * 60 * 60);
			return hour;
		} catch (Exception ex) {
			return 0;
		}
	}

	public static boolean batchTime(Long time, int minute) {
		long millis = (long) minute * 60 * 1000;
		long currentTime = System.currentTimeMillis();
		if ((currentTime - time) > millis) {
			return true;
		}
		return false;
	}

	public static Date addMinute(Date time, int minute) {
		Calendar sl_time = Calendar.getInstance();
		sl_time.setTime(time);
		sl_time.add(Calendar.MINUTE, minute);
		return sl_time.getTime();
	}

	public static Date addSecondes(Date time, int seconds) {
		Calendar sl_time = Calendar.getInstance();
		sl_time.setTime(time);
		sl_time.add(Calendar.SECOND, seconds);
		return sl_time.getTime();
	}
	//获取当前时间后的第n分钟
    public static String getTimeByMinute(int minute) {
     Calendar calendar = Calendar.getInstance();
     calendar.add(Calendar.MINUTE, minute);
     return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
    }
	/**
	 * 获取时间的描述(大概)
	 * 如：61秒或者62秒 返回的结果：1分钟
	 * @param seconds 秒
	 * @return
	 */
	public static String getTimeProbablyFriendlyDesc(int seconds) {
		String intervalDes=null;
		if (seconds<60) {
			intervalDes=seconds+"秒";
		}else if(seconds <3600) {
			intervalDes=seconds/60+"分钟";
		}else {
			intervalDes=seconds/3600+"小时";
		}
		
		return intervalDes;
		
	}
	
	
	/**
	 * 设定日期的某一秒
	 * @param day
	 * @param time_str
	 * @return
	 */
	public static Date timeing(Date day,String time_str){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
		String mode = sdf.format(day) + time_str;
		return strToDate(mode);
	}

	public static int daysBetween(Date date1,Date date2) throws ParseException
	{
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		date1=sdf.parse(sdf.format(date1));
		date2=sdf.parse(sdf.format(date2));
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		long time1 = cal.getTimeInMillis();
		cal.setTime(date2);
		long time2 = cal.getTimeInMillis();
		long between_days=(time2-time1)/(1000*3600*24);

		return Integer.parseInt(String.valueOf(between_days));
	}



	public static boolean isSameDay(String date1, String date2) {
		String d1 = formatYYYYMMDDStr(date1);
		String d2 = formatYYYYMMDDStr(date2);
		return StringUtils.equals(d1, d2);
	}
	
	/**
	 * 得到昨天的日期
	 * @return
	 */
	public static String getYesTeday() {
		Calendar calendar = Calendar.getInstance();// 此时打印它获取的是系统当前时间
		calendar.add(Calendar.DATE, -1); // 得到前一天
		String yestedayDate = new SimpleDateFormat("yyyy-MM-dd")
				.format(calendar.getTime());
		return yestedayDate;
	}
	
	/**
	 * 将传入的时间字符串转换成时间格式
	 */
	public static Date formatDate3(String _Date) throws ParseException {
		SimpleDateFormat _SimpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss", Locale.SIMPLIFIED_CHINESE);
		return _SimpleDateFormat.parse(_Date);
	}
	
	 /**
     * 获取当年的第一天
     * @param year
     * @return
     */
    public static Date getCurrYearFirst(){
        Calendar currCal=Calendar.getInstance();  
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearFirst(currentYear);
    }
     
    /**
     * 获取当年的最后一天
     * @param year
     * @return
     */
    public static Date getCurrYearLast(){
        Calendar currCal=Calendar.getInstance();  
        int currentYear = currCal.get(Calendar.YEAR);
        return getYearLast(currentYear);
    }
    
    /**
     * 获取某年第一天日期
     * @param year 年份
     * @return Date
     */
    public static Date getYearFirst(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        Date currYearFirst = calendar.getTime();
        return currYearFirst;
    }
     
    /**
     * 获取某年最后一天日期
     * @param year 年份
     * @return Date
     */
    public static Date getYearLast(int year){
        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.roll(Calendar.DAY_OF_YEAR, -1);
        Date currYearLast = calendar.getTime();
         
        return currYearLast;
    }
    
    /**
     * 判断是否是一年的最后一天
     * @param date
     * @return
     */
    public static boolean isLastDayOfYear(Date date) { 
        Calendar calendar = Calendar.getInstance(); 
        calendar.setTime(date); 
        calendar.set(Calendar.DATE, (calendar.get(Calendar.DATE) + 1)); 
        if (calendar.get(Calendar.DAY_OF_YEAR) == 1) { 
            return true; 
        }
        return false; 
    } 
    
    public static void main(String[] args) {
    	
    	System.out.println(DateUtil.isLastDayOfYear(DateUtil.getYearFirst(2016)));
    }

}
