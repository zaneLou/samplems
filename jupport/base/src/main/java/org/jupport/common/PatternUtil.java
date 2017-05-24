package org.jupport.common;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PatternUtil {

	public static Pattern phonePattern = Pattern.compile("^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
	
	public static boolean isMobileNO(String mobiles){
		Matcher m = phonePattern.matcher(mobiles);
		return m.matches();
	}
}
