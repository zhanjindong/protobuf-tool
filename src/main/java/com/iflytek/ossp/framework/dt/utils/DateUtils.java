package com.iflytek.ossp.framework.dt.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.FastDateFormat;

/**
 * 日期和时间相关操作工具类。
 * <p>
 * <b>NOTE：</b>时区固定为 <code>Etc/GMT-8</code>
 * 
 * @author jdzhan,2014-7-31
 */
public class DateUtils {

	private static final TimeZone TIME_ZONE = TimeZone.getTimeZone("Etc/GMT-8");
	public final static String DATE_FORMAT = "yyyy-MM-dd";
	public final static String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

	/**
	 * 判断两个日期是否为一天
	 * 
	 * @param c1
	 * @param c2
	 * @return
	 */
	public static boolean isSameDay(Calendar c1, Calendar c2) {
		return (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)) && (c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH))
				&& (c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH));
	}

	/**
	 * 判断两个日期是否为一天
	 * <p>
	 * 
	 * @author mgwang, 2014年1月18日
	 * @param d1
	 * @param d2
	 * @return true or false
	 */
	public static boolean isSameDay(Date d1, Date d2) {
		Calendar c1 = Calendar.getInstance(TIME_ZONE);
		c1.setTime(d1);
		Calendar c2 = Calendar.getInstance(TIME_ZONE);
		c2.setTime(d2);
		return isSameDay(c1, c2);
	}

	/**
	 * 获取格式化的日期字符串
	 * 
	 * @param calendar
	 * @param formatString
	 * @return
	 */
	public static String format(Calendar calendar, String formatString) {
		return format(calendar.getTime(), formatString);
	}

	/**
	 * 获取格式化的日期字符串
	 * 
	 * @param date
	 * @param formatString
	 * @return
	 */
	public static String format(Date date, String formatString) {
		FastDateFormat format = FastDateFormat.getInstance(formatString, TIME_ZONE);
		return format.format(date);
	}

	/**
	 * 获取系统当前时间的字符串形式:yyyy-MM-dd HH:mm:ss.SSS，精确到毫秒
	 * 
	 * @return
	 */
	public static String now() {
		Calendar now = Calendar.getInstance(TIME_ZONE);
		return format(now, "yyyy-MM-dd HH:mm:ss.SSS");
	}

	public static String now(String format) {
		Calendar now = Calendar.getInstance(TIME_ZONE);
		return format(now, format);
	}

	/**
	 * 
	 * Etc/GMT-8
	 * 
	 * @author jdzhan,2014-10-16
	 * 
	 * @return
	 */
	public static Date getCurrentTime() {
		Calendar calendar = Calendar.getInstance(TIME_ZONE);
		return calendar.getTime();
	}

	public static Date getCurrentTime(TimeZone timeZone) {
		Calendar calendar = Calendar.getInstance(timeZone);
		return calendar.getTime();
	}

	/**
	 * 将字符串格式化为日期
	 * 
	 * @param string
	 * @param formatString
	 * @return
	 * @throws ParseException
	 */
	public static Date convertToDate(String string, String formatString) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(formatString);
		return sdf.parse(string);
	}

	/**
	 * 获取当前的日期
	 * 
	 * @author dsfan, 2013-9-16
	 * @return
	 * @throws ParseException
	 */
	public static Calendar getToday() throws ParseException {
		Calendar today = Calendar.getInstance(TIME_ZONE);
		today.setTime(convertToDate(now("yyyy-MM-dd"), "yyyy-MM-dd"));

		return today;
	}

	/**
	 * 时间增加一定天数
	 * <p>
	 * 
	 * @author mgwang, 2013年10月28日
	 * @param c
	 * @param day
	 * @return
	 */
	public static Calendar addDay(Calendar c, int day) {
		c.add(Calendar.DAY_OF_MONTH, day);
		return c;
	}

	/**
	 * Date增加一定天数
	 * <p>
	 * 
	 * @author mgwang, 2014年1月6日
	 * @param d
	 *            原始日期
	 * @param day
	 *            增加天数
	 * @return 增加后的日期
	 */
	public static Date addDay(Date d, int day) {
		Calendar calendar = Calendar.getInstance(TIME_ZONE);
		calendar.setTime(d);
		calendar = addDay(calendar, day);
		return calendar.getTime();
	}

	/**
	 * 获取日期的星期几的字符串
	 * <p>
	 * 
	 * @author dsfan, 2013-10-14
	 * @param calendar
	 * @return
	 */
	public static String getWeekDayString(Calendar calendar) {
		String weekstr = "";
		int week = calendar.get(Calendar.DAY_OF_WEEK);
		switch (week) {
		// 注意：Calendar中1~7分别表示周日、周一、...、周六
		case 2:
			weekstr = "周一";
			break;
		case 3:
			weekstr = "周二";
			break;
		case 4:
			weekstr = "周三";
			break;
		case 5:
			weekstr = "周四";
			break;
		case 6:
			weekstr = "周五";
			break;
		case 7:
			weekstr = "周六";
			break;
		case 1:
			weekstr = "周日";
			break;
		default:
			weekstr = "周日";
		}
		return weekstr;
	}

	/**
	 * 计算两个日期之间相差的天数：2014-01-01 23:59:59和2014-01-02 00:00:01相差的天数应该是1。
	 * <p>
	 * 
	 * @author jdzhan,2014-09-16
	 * @param date1
	 * @param date2
	 * @return 返回相差天数，如果date1早于date2返回一个正整数，同一天返回0，否则返回一个负整数。
	 * @throws ParseException
	 */
	public static int diffDays(Date date1, Date date2) throws ParseException {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date1);
		long time1 = cal.getTimeInMillis();
		cal.setTime(date2);
		long time2 = cal.getTimeInMillis();
		float between_days = (time2 - time1) * 1.0F / (1000 * 3600 * 24);

		if (between_days < 0 && between_days > -1) {
			if (isSameDay(date1, date2)) {
				return 0;
			}
			return -1;
		}
		if (between_days > 0 && between_days < 1) {
			if (isSameDay(date1, date2)) {
				return 0;
			}
			return 1;
		}
		if (between_days == 0) {
			return 0;
		} else if (between_days < 0) {
			return (int) Math.floor(between_days);
		} else {
			return (int) Math.ceil(between_days);
		}

	}

	/**
	 * 计算两个日期相差的天数：2014-01-01 23:59:59和2014-01-02 00:00:01相差的天数应该是1。
	 * <p>
	 * 默认date1和date2格式：yyyy-MM-dd HH:mm:ss
	 * 
	 * @author jdzhan,2014-10-16
	 * 
	 * @param date1
	 * @param date2
	 * @return 返回相差天数，如果date1早于date2返回一个正整数，同一天返回0，否则返回一个负整数。
	 * @throws ParseException
	 */
	public static int diffDays(String date1, String date2) throws ParseException {
		return diffDays(date1, date2, "yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 
	 * 计算两个日期相差的天数:2014-01-01 23:59:59和2014-01-02 00:00:01相差的天数应该是1。
	 * 
	 * @author jdzhan,2014-10-16
	 * 
	 * @param date1
	 * @param date2
	 * @param format
	 *            指定日期字符串的格式，如：yyyyMMddHHmmss,只支持到秒
	 * @return 返回相差天数，如果date1早于date2返回一个正整数，同一天返回0，否则返回一个负整数。
	 * @throws ParseException
	 */
	public static int diffDays(String date1, String date2, String format) throws ParseException {
		SimpleDateFormat sf = new SimpleDateFormat(format);
		Date d1 = sf.parse(date1);
		Date d2 = sf.parse(date2);
		return diffDays(d1, d2);
	}

	/**
	 * 
	 * 计算两个日期之间相差的天数：2014-01-01 23:59:59和2014-01-02 00:00:01相差的天数应该是1。
	 * <p>
	 * 
	 * @author jdzhan,2014-9-16
	 * 
	 * @param c1
	 * @param c2
	 * @return 返回相差天数，如果c1早于c2返回一个正整数，同一天返回0，否则返回一个负整数。
	 * @throws ParseException
	 */
	public static int diffDays(Calendar c1, Calendar c2) throws ParseException {
		return diffDays(c1.getTime(), c2.getTime());
	}

	/**
	 * 转换字符串日期格式。
	 * <p>
	 * 
	 * @author mgwang, 2014年3月11日
	 * @param date
	 *            需要转换格式的日期的字符串。
	 * @param preFormat
	 *            转换前的格式：如yyyyMMddHHmmss
	 * @param transFormat
	 *            转换后的格式：如yyyy-MM-dd HH:mm:ss
	 * @return
	 * @throws ParseException
	 */
	public static String transformFormat(String date, String preFormat, String transFormat) throws ParseException {
		return format(convertToDate(date, preFormat), transFormat);
	}

	/**
	 * 判断yyyy-MM-dd HH:mm:ss.SSS 格式的字符串时间先后。
	 * <p>
	 * 
	 * @author mgwang, 2014-5-27
	 * @param startTime
	 * @param startTime2
	 * @return
	 */
	public static boolean after(String date1, String date2) {
		if (StringUtils.contains(date1, "-") && StringUtils.contains(date1, ":") && StringUtils.contains(date2, "-")
				&& StringUtils.contains(date2, ":")) {
			return after(date1, date2, DATETIME_FORMAT);
		}
		return false;
	}

	/**
	 * 判断的字符串时间先后,指定时间格式。
	 * <p>
	 * 
	 * @author mgwang, 2014-5-29
	 * @param date1
	 * @param date2
	 * @param format
	 * @return
	 */
	public static boolean after(String date1, String date2, String format) {
		try {
			return convertToDate(date1, format).after(convertToDate(date2, format));
		} catch (ParseException e) {
			return false;
		}
	}

	/**
	 * 判断yyyy-MM-dd 字符串日期是否是今天。
	 * <p>
	 * 
	 * @author mgwang, 2014-5-29
	 * @param dateStr
	 * @return
	 */
	public static boolean isToday(String dateStr) {
		Date date;
		try {
			date = convertToDate(dateStr, DATE_FORMAT);
		} catch (ParseException e) {
			return false;
		}
		Calendar c1 = new GregorianCalendar();
		c1.setTime(date);
		Calendar c2 = new GregorianCalendar();
		c2.setTime(new Date());
		if (DateUtils.isSameDay(c1, c2)) {
			return true;
		}
		return false;
	}
}
