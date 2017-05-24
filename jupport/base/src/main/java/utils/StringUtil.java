package utils;

public class StringUtil {

	public static String removeSuffix(String value, String suffix)
	{
		int index = value.indexOf(suffix);
		if(index>0)
		{
			return value.substring(0, index);
		}
		return value;
	}
}
