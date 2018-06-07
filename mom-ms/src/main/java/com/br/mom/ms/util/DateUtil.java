package com.br.mom.ms.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author caoxin
 */
public class DateUtil {

	private static DateUtil dateUtil;

	private DateUtil() {
	}

	public static DateUtil getInstance() {
		if (DateUtil.dateUtil == null) {
			DateUtil.dateUtil = new DateUtil();
		}
		return DateUtil.dateUtil;
	}

	public static String formatForDate(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(date);
	}

	public static String formatForDate(long timestamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		return sdf.format(new Date(timestamp));
	}

	public static String formatForDate(String date) {
		if (date != null) {
			return String.format(date.substring(0, date.lastIndexOf(" ")));
		}
		return "";
	}

	public static String formatForDateTime(long timestamp) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date(timestamp));
	}

	public static String formatForDateTime(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}

	public static String formatForDateTime(String time) {
		if (time != null) {
			return String.format(time.substring(0, time.lastIndexOf(".")));
		}
		return "";
	}

	/**
	 *
	 * @param dateStr
	 *            "YYYY-MM-DD HH:MM:SS"
	 * @return
	 */
	public static Date getDate(String dateStr) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try {
			return sdf.parse(dateStr);
		} catch (ParseException ex) {
		}
		return null;
	}

}
