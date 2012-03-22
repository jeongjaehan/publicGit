package com.paran.bizportal.common;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Date Util
 * @author ¡§¿Á«—
 *
 */
public class DateUtil {
	
	/**
	 * 
	 * @param format
	 * @return
	 */
	public static String getDateString(String format){
		if(format == null || format.length() <1 || format.equals(""))
			format = "yyyyMMddHHmmss";
		
		long now = System.currentTimeMillis();
		SimpleDateFormat sdfNow = new SimpleDateFormat(format);
		String str_now = sdfNow.format(new Date(now));
		return str_now;
	}
}
