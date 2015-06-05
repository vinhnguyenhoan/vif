package vn.vif.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.joda.time.DateTime;
import org.joda.time.Period;

public class Utility {
	public static Date parseLocalDate(String sDate){
		Date result=null;
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		try {
			result= df.parse(sDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}
    public static String parseParamaterDate(Date date){
    	SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
    	return df.format(date);
    }
    
    public static Date parseParamaterDate(String sDate){
		Date result=null;
		SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		try {
			result= df.parse(sDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
    }

	public static boolean isValidDate(String input) {
		SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
	     try {
	          format.parse(input);
	          return true;
	     }
	     catch(ParseException e){
	          return false;
	     }
	}
	
	public static String timeDiff(Date startTime, Date endTime) {
		DateTime jdStartTime = new DateTime(startTime);
		DateTime jdEndTime = new DateTime(endTime);
		return timeDiff(jdStartTime, jdEndTime);
	}
	
	public static String timeDiff(DateTime jdStartTime, DateTime jdEndTime) {
		Period howLong = new Period(jdStartTime, jdEndTime);
		return String.format("%02d:%02d:%02d", howLong.getHours(), howLong.getMinutes(), howLong.getSeconds());
	}
	
	public static String timeDiff(Calendar startTime, Calendar endTime) {
		DateTime jdStartTime = new DateTime(startTime);
		DateTime jdEndTime = new DateTime(endTime);
		return timeDiff(jdStartTime, jdEndTime);
	}
	
	public static boolean isNumeric(String str)	{
	    for (char c : str.toCharArray()) {
	        if (!Character.isDigit(c)) return false;
	    }
	    return true;
	}
}
