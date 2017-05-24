package org.jupport.common;


import org.apache.commons.lang3.text.translate.CodePointTranslator;
import org.apache.commons.lang3.text.translate.UnicodeUnescaper;

public class EscapeStringUtil {

	protected static final CodePointTranslator escaper = new PropertyEscaper();
	protected static final UnicodeUnescaper unescaper = new UnicodeUnescaper();
	
	public static String utfEncodedWithString(String string) 
	{
		return escaper.translate(string);
	}
	
	public static String utfDecodedWithString(String string)
	{
		return unescaper.translate(string);
	}
	
	public static void main(String[] args)  
	{
		String string = EscapeStringUtil.utfEncodedWithString("我");
		System.out.println("1.string:"+string);
		string = EscapeStringUtil.utfDecodedWithString(string);
		System.out.println("2.string:"+string);
	}
}
