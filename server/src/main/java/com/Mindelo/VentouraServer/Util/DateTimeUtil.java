package com.Mindelo.VentouraServer.Util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DateTimeUtil {
	
	public static String fromDateToString_GMT(Date dateTime){
		SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
		return format.format(dateTime);
	}
	
	public static Date fromStringToDate_GMT(String dateTime) throws ParseException{
		SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
		return format.parse(dateTime);
	}
	
	String dateString = "Wed, 09 Apr 2008 23:55:38 GMT";
	


	public static Date fromStringToDate(String dateTime) {
		try {
			DateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm",
					Locale.getDefault());
			return format.parse(dateTime);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String fromDateToString(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm",
				Locale.getDefault());
		return format.format(date);

	}

	public static String fromDateToString_DDMMYYYY(Date date) {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy",
				Locale.getDefault());
		return format.format(date);

	}
	
	public static Date fromToStringTODATE_DDMMYYYY(String date) throws ParseException {
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

	public static int daysBetweenDDMMYYYY(String d1, String d2) throws ParseException{
		Date date1 = fromToStringTODATE_DDMMYYYY(d1);
		Date date2 = fromToStringTODATE_DDMMYYYY(d2);
		return (int) ((date1.getTime() - date2.getTime()) / (1000 * 60 * 60 * 24));
	}
	
	public static int daysBetween(Date d1, Date d2) {
		return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
	}

}
