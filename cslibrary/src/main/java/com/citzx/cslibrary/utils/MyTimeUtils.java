package com.citzx.cslibrary.utils;

import android.annotation.SuppressLint;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@SuppressLint("SimpleDateFormat")
public class MyTimeUtils {
	public static int getWeekdayOfMonth() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
		int dayOfWeek;
		String sysDate = "";
		String sys_year = "";
		String sys_month = "";
		String sys_day = "";
		Date date = new Date();
		sysDate = sdf.format(date); // 当期日期
		sys_year = sysDate.split("-")[0];
		sys_month = sysDate.split("-")[1];
		sys_day = sysDate.split("-")[2];
		Calendar cal = Calendar.getInstance();
		cal.set(Integer.parseInt(sys_year), Integer.parseInt(sys_month) - 1, Integer.parseInt(sys_day));
		dayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
		if (dayOfWeek == 0) {
			dayOfWeek = 7;
		}
		return dayOfWeek;
	}
	public static String getAllDate(){
		String mYear;
		String mMonth;
		String mDay;
		String mWay;
		final Calendar c = Calendar.getInstance();
		c.setTimeZone(TimeZone.getTimeZone("GMT+8:00"));
		mYear = String.valueOf(c.get(Calendar.YEAR)); // 获取当前年份
		mMonth = String.valueOf(c.get(Calendar.MONTH) + 1);// 获取当前月份
		mDay = String.valueOf(c.get(Calendar.DAY_OF_MONTH));// 获取当前月份的日期号码
		mWay = String.valueOf(c.get(Calendar.DAY_OF_WEEK));
		if("1".equals(mWay)){
			mWay ="天";
		}else if("2".equals(mWay)){
			mWay ="一";
		}else if("3".equals(mWay)){
			mWay ="二";
		}else if("4".equals(mWay)){
			mWay ="三";
		}else if("5".equals(mWay)){
			mWay ="四";
		}else if("6".equals(mWay)){
			mWay ="五";
		}else if("7".equals(mWay)){
			mWay ="六";
		}
		return mYear + "年" + mMonth + "月" + mDay+"日"+"    星期"+mWay;
	}
	public static String getTime(String CreateDate) {
		String day = "";
		try {
			if (CreateDate.startsWith("/")) {
				CreateDate = CreateDate.replace("/", "").replace("Date", "").replace("(", "").replace(")", "");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Long timestamp = Long.parseLong(CreateDate);
				day = sdf.format(new Date(timestamp));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		return day;
	}

    /**
     * @param CreateDate
     * @return  获得年、月、日 int类型
     */
    public static void getYearMonthDay(String CreateDate, OnChuoTimeListener listener) {
        try {
            if (CreateDate.startsWith("/")) {
                CreateDate = CreateDate.replace("/", "").replace("Date", "").replace("(", "").replace(")", "");
                SimpleDateFormat sdfyear = new SimpleDateFormat("yyyy");
                SimpleDateFormat sdfmonth = new SimpleDateFormat("MM");
                SimpleDateFormat sdfday = new SimpleDateFormat("dd");
                Long timestamp = Long.parseLong(CreateDate);
                int year = Integer.parseInt(sdfyear.format(new Date(timestamp)));
                int month = Integer.parseInt(sdfmonth.format(new Date(timestamp)));
                int day = Integer.parseInt(sdfday.format(new Date(timestamp)));
                listener.onChuoTime(year,month,day);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
    public interface OnChuoTimeListener{
        public void onChuoTime(int year, int month, int day);
    }
	public static String getTime2(String CreateDate) {
		String day = "";
		try {
			if (CreateDate.startsWith("/")) {
				CreateDate = CreateDate.replace("/", "").replace("Date", "").replace("(", "").replace(")", "");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
				Long timestamp = Long.parseLong(CreateDate);
				day = sdf.format(new Date(timestamp));
			}
		} catch (Exception e) {

		}

		return day;
	}

	public static String getTime3(String CreateDate) {
		String day = "";
		try {
			if (CreateDate.startsWith("/")) {
				CreateDate = CreateDate.replace("/", "").replace("Date", "").replace("(", "").replace(")", "");
				SimpleDateFormat sdf = new SimpleDateFormat("MM-dd HH:mm");
				Long timestamp = Long.parseLong(CreateDate);
				day = sdf.format(new Date(timestamp));
			}
		} catch (Exception e) {

		}

		return day;
	}

	public static String getTime4(String CreateDate) {
		String day = null;
		try {
			if (CreateDate.startsWith("/")) {
				CreateDate = CreateDate.replace("/", "").replace("Date", "").replace("(", "").replace(")", "");
				SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
				Long timestamp = Long.parseLong(CreateDate);
				day = sdf.format(new Date(timestamp));
			}
		} catch (Exception e) {

		}

		return day;
	}

	public static String getTime5(String CreateDate) {
		String day = "";
		try {
			if (CreateDate.startsWith("/")) {
				CreateDate = CreateDate.replace("/", "").replace("Date", "").replace("(", "").replace(")", "");
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				Long timestamp = Long.parseLong(CreateDate);
				day = sdf.format(new Date(timestamp));
			}
		} catch (Exception e) {

		}

		return day;
	}

	public static String getTime6(String CreateDate) {
		String day = "";
		try {
			if (CreateDate.startsWith("/")) {
				CreateDate = CreateDate.replace("/", "").replace("Date", "").replace("(", "").replace(")", "");
				SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm");
				Long timestamp = Long.parseLong(CreateDate);
				day = sdf.format(new Date(timestamp));
			}
		} catch (Exception e) {

		}
		return day;
	}

	/**
	 * @category 返回xx月xx日
	 * @param CreateDate
	 * @return
	 */
	public static String getTime7(String CreateDate) {
		String day = "";
		try {
			if (CreateDate.startsWith("/")) {
				CreateDate = CreateDate.replace("/", "").replace("Date", "").replace("(", "").replace(")", "");
				// SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm");
				SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日");
				Long timestamp = Long.parseLong(CreateDate);
				day = sdf.format(new Date(timestamp));
			}
		} catch (Exception e) {

		}
		return day;
	}

	/**
	 * @category 返回xx时xx分
	 * @param CreateDate
	 * @return
	 */
	public static String getTime8(String CreateDate) {
		String day = "";
		try {
			if (CreateDate.startsWith("/")) {
				CreateDate = CreateDate.replace("/", "").replace("Date", "").replace("(", "").replace(")", "");
				SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
				// SimpleDateFormat sdf = new SimpleDateFormat("MM月dd日 HH:mm");
				Long timestamp = Long.parseLong(CreateDate);
				day = sdf.format(new Date(timestamp));
			}
		} catch (Exception e) {

		}
		return day;
	}
    public static String getTime9(String CreateDate) {
        String day = null;
        try {
            if (CreateDate.startsWith("/")) {
                CreateDate = CreateDate.replace("/", "").replace("Date", "").replace("(", "").replace(")", "");
                SimpleDateFormat sdf = new SimpleDateFormat("dd");
                Long timestamp = Long.parseLong(CreateDate);
                day = sdf.format(new Date(timestamp));
            }
        } catch (Exception e) {

        }
        return day;
    }
	/**
	 * @category 时间String 转Date
	 */
	public static Date toDate(String time) {
		Date date = null;
		try {
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			date = format.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * @category 时间Date 转 String
	 */
	public static String toString(Date date, String format) {
		String day = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			day = sdf.format(date); // 当期日期
		} catch (Exception e) {

		}
		return day;
	}

	/**
	 * @category 时间Date 转 String
	 */
	public static String toString(String CreateTime, String format) {
		String day = "";
		try {
			if (CreateTime.startsWith("/")) {
				CreateTime = CreateTime.replace("/", "").replace("Date", "").replace("(", "").replace(")", "");
				SimpleDateFormat sdf = new SimpleDateFormat(format);
				Long timestamp = Long.parseLong(CreateTime);
				day = sdf.format(new Date(timestamp));
			}
		} catch (Exception e) {

		}
		return day;
	}

	/**
	 * @category 时间Date 转 String
	 */
	public static String toString(Date date, SimpleDateFormat format) {
		String day = "";
		try {
			day = format.format(date);
		} catch (Exception e) {

		}
		return day;
	}

	/**
	 * @category 求相差天数(严格意义上的天数)
	 */
	public static int getDay(Date begin, Date end) {
		int days = 0;
		try {
			days = (int) Math.abs((end.getTime() - begin.getTime()) / (24 * 60 * 60 * 1000)) + 1;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return days;
	}

	/**
	 * @category 求相差天数(日历上的)
	 */
	public static int daysOfTwo(Date begin, Date end) {
		int day = 0;
		try {
			Calendar aCalendar = Calendar.getInstance();
			aCalendar.setTime(begin);
			int day1 = aCalendar.get(Calendar.DAY_OF_YEAR);
			aCalendar.setTime(end);
			int day2 = aCalendar.get(Calendar.DAY_OF_YEAR);
			day = day2 - day1;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return day;

	}
	   /**
	   * 获取两个日期之间的间隔天数
	   * @return
	   */
	   public static int getGapCount(Date startDate, Date endDate) {
	           Calendar fromCalendar = Calendar.getInstance();
	           fromCalendar.setTime(startDate);  
	           fromCalendar.set(Calendar.HOUR_OF_DAY, 0);
	           fromCalendar.set(Calendar.MINUTE, 0);
	           fromCalendar.set(Calendar.SECOND, 0);
	           fromCalendar.set(Calendar.MILLISECOND, 0);
	     
	           Calendar toCalendar = Calendar.getInstance();
	           toCalendar.setTime(endDate);  
	           toCalendar.set(Calendar.HOUR_OF_DAY, 0);
	           toCalendar.set(Calendar.MINUTE, 0);
	           toCalendar.set(Calendar.SECOND, 0);
	           toCalendar.set(Calendar.MILLISECOND, 0);
	     
	           return (int) ((toCalendar.getTime().getTime() - fromCalendar.getTime().getTime()) / (1000 * 60 * 60 * 24));
	   }

	/**
	 * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014-06-14-16-09-00"）
	 *
	 * @param time
	 * @return
	 */
	public static String getTimeChuo(String time) {
		Long times = Long.parseLong(time);
		SimpleDateFormat sdr = new SimpleDateFormat("yyyy年MM月dd日HH时mm分");
		Date d = new Date(times);
		return sdr.format(d);

	}

    /**
     * @param time
     * @param type
     * @return  日期加一天的算法。
     */
    public static String addOneDay(String time, int type, int addday){
        Date date = null;
        try {
            date = (new SimpleDateFormat("yyyy年MM月dd日")).parse(time);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, addday);
        String day=null;
        if(type==0){//年月日
            day=(new SimpleDateFormat("yyyy年MM月dd日")).format(cal.getTime());
        }else if(type==1){//年
            day=(new SimpleDateFormat("yyyy")).format(cal.getTime());
        }else if(type==2){//月
            day=(new SimpleDateFormat("MM")).format(cal.getTime());
        }else if(type==3){//日
            day=(new SimpleDateFormat("dd")).format(cal.getTime());
        }
        return day;
    }

    /**
     * @param timeString
     * @return  日期转时间戳  例如：2016-10-26转时间戳
     */
    public static String getChuoTime(String timeString){
        String timeStamp = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date d;
        try{
            d = sdf.parse(timeString);
            long l = d.getTime();
            timeStamp = String.valueOf(l);
        } catch(ParseException e){
            e.printStackTrace();
        }
        return timeStamp;
    }

	/**
	 * @return  获取今天日期
	 */
	public static String getToday(String format){
		SimpleDateFormat sDateFormat    =   new SimpleDateFormat(format);
		String date    =    sDateFormat.format(new Date());
		return  date;
	}
}
