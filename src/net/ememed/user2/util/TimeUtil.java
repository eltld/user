package net.ememed.user2.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.TimeZone;

import android.os.SystemClock;

public class TimeUtil {


	public static int replace(String str) {
		return Integer.parseInt(str.replace(":", ""));
	}

	public static String parseTime(int time) {
		String st = time < 1000 ? "0" + time : "" + time;
		String hh = st.substring(0, 2);
		String mm = st.substring(2);
		return hh + ":" + mm;
	}
	
	public static String parseDateTime(long time,String dateFormat) {
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
		String date = sdf.format(new Date(time*1000));	
		return date;
	}

	public static String parseFullDateTime2YMD(String dateTime) {
		String date = "";
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date dt2 = sdf.parse(dateTime);
			date = sdf.format(dt2);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}
	
	/**判断是否为当天*/
	public static boolean checkIsNeedUpdate() {

		try {
			long time = SharePrefUtil.getLong(Conast.FIRST_START_ONE_DAY_TIME);
			TimeZone t = TimeZone.getTimeZone("GMT+08:00");// 获取东8区TimeZone
			Calendar calendar = Calendar.getInstance(t);
			calendar.setTimeInMillis(System.currentTimeMillis());
			int year = calendar.get(Calendar.YEAR);
			int month = calendar.get(Calendar.MONTH);
			int day = calendar.get(Calendar.DAY_OF_MONTH);
			if (time > 0) {
				Calendar calendar2 = Calendar.getInstance(t);
				calendar2.setTimeInMillis(time);
				int year2 = calendar2.get(Calendar.YEAR);
				int month2 = calendar2.get(Calendar.MONTH);
				int day2 = calendar2.get(Calendar.DAY_OF_MONTH);
				if (calendar.compareTo(calendar2) > 0) {
					if (year == year2 && month == month2 && day == day2) {
						return false;
					}
					SharePrefUtil.putLong(Conast.FIRST_START_ONE_DAY_TIME,System.currentTimeMillis());
					SharePrefUtil.commit();
					return true;
				}
			} else {
				SharePrefUtil.putLong(Conast.FIRST_START_ONE_DAY_TIME,System.currentTimeMillis());
				SharePrefUtil.commit();
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

}
