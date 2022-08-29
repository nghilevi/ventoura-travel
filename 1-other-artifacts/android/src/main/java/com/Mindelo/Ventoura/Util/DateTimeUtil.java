package com.Mindelo.Ventoura.Util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.SimpleTimeZone;

public class DateTimeUtil {
	
	public static String fromDateToString_DD_MMM_YYYY(Date date){
		SimpleDateFormat format = new SimpleDateFormat(
				"dd/MMM/yyyy");
		return format.format(date);
	}

	public static String fromDateToString_GMT(Date dateTime) {
		SimpleDateFormat format = new SimpleDateFormat(
				"EEE, dd MMM yyyy HH:mm:ss zzz");
		format.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
		return format.format(dateTime);
	}

	public static Date fromStringToDate_GMT(String dateTime)
			throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat(
				"EEE, dd MMM yyyy HH:mm:ss zzz");
		format.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
		return format.parse(dateTime);
	}

	public static Date fromStringToDate(String dateTime) {
		try {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
					Locale.getDefault());
			return format.parse(dateTime);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String fromDateToString(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.getDefault());
		return format.format(date);

	}

	public static String fromDateToString_DDMMYYYY(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy",
				Locale.getDefault());
		return format.format(date);

	}
	

	public static Date fromStringToDATE_DDMMYYYY(String date)
			throws ParseException {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy",
				Locale.getDefault());
		return format.parse(date);

	}

	public static int computeAge(Date date) {
		if (date == null) {
			return 0;
		}

		Calendar cal = new GregorianCalendar();

		cal.setTime(date);
		Calendar now = new GregorianCalendar();
		int res = now.get(Calendar.YEAR) - cal.get(Calendar.YEAR);
		if ((cal.get(Calendar.MONTH) > now.get(Calendar.MONTH))
				|| (cal.get(Calendar.MONTH) == now.get(Calendar.MONTH) && cal
						.get(Calendar.DAY_OF_MONTH) > now
						.get(Calendar.DAY_OF_MONTH))) {
			res--;
		}

		return res;
	}

	/**
	 * compute the interval dates between two dates, date2 is later and larger
	 */
	public static int daysBetweenDDMMYYYY(String d1, String d2)
			throws ParseException {
		Date date1 = fromStringToDATE_DDMMYYYY(d1);
		Date date2 = fromStringToDATE_DDMMYYYY(d2);
		return (int) ((date2.getTime() - date1.getTime()) / (1000 * 60 * 60 * 24));
	}

	/**
	 * compute the interval dates between two dates, date2 is later and larger
	 */
	public static int daysBetween(Date d1, Date d2) {
		return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
	}

	/**
	 * 
	 * @param dateTime
	 * @return if the datetime was in this day, give the exact time. If it is
	 *         yesterday, return "yesterday", otherwise, return the dd-mm-yyyys
	 */
	public static String dateToHumanBlurSenceTime(Date dateTime) {
		String timeString = "";
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(dateTime);
		if (calendar.get(Calendar.DATE) == Calendar.getInstance().get(
				Calendar.DATE)) {
			int hour = calendar.get(Calendar.HOUR);
			int minute = calendar.get(Calendar.MINUTE);
			if (hour == 0) {
				timeString += "00:";
			} else {
				timeString += hour + ":";
			}

			if (minute < 10) {
				timeString += "0" + minute;
			} else {
				timeString += minute;
			}

			if (calendar.get(Calendar.HOUR_OF_DAY) >= 12) {
				timeString += " pm";
			} else {
				timeString += " am";
			}
		} else if (calendar.get(Calendar.DATE) == Calendar.getInstance().get(
				Calendar.DATE) - 1) {
			timeString = "yesterday";
		} else {
			timeString = DateTimeUtil.fromDateToString_DDMMYYYY(dateTime);
		}
		return timeString;
	}

}
