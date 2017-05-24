package org.jupport.common;

import java.text.SimpleDateFormat;
import java.util.Date;


public class AESUtil {

	protected AESCipher cipherWithIv;
	protected java.security.MessageDigest md;
	
	public void init()
	{
		//init
		String key = "483311e1ae220826ea76f2689";
		if(key.length()<32)
			key = paddingZero(key, 32);
		
		String iv = "";
		for (int i = 0; i < 16; i++) {
			iv += (char)0;
		}
		cipherWithIv = new AESCipher(key.getBytes(), iv.getBytes());
	}
	
	public String paddingZero(String key, int size)
	{
		for (int i = key.length(); i < size; i++) {
			key += (char)0;
		}
		return key;
	}
	
	public synchronized String encryp(String value)
	{
		return cipherWithIv.getEncryptedMessage(value);
	}
	
	public synchronized String decryp(String value)
	{
		return cipherWithIv.getDecryptedMessage(value);
	}
	
	public synchronized String getMD5(byte[] source) 
	{
	        String s = null;
	        char hexDigits[] = { 
	        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd',   'e', 'f' };
	        try {
	        		if(md == null)
	        			md = java.security.MessageDigest.getInstance("MD5");
	        	
	                md.update(source);
	                byte tmp[] = md.digest();
	                char str[] = new char[16 * 2];
	                int k = 0; 
	                for (int i = 0; i < 16; i++) { 
	                        byte byte0 = tmp[i]; 
	                        str[k++] = hexDigits[byte0 >>> 4 & 0xf]; 
	                        str[k++] = hexDigits[byte0 & 0xf];
	                }
	                s = new String(str); 
	        } catch (Exception e) {
	                e.printStackTrace();
	        }
	        return s;
	}
	
	/**将二进制转换成16进制 
	 * @param buf 
	 * @return 
	 */  
	public static String parseByte2HexStr(byte buf[]) {  
	        StringBuffer sb = new StringBuffer();  
	        for (int i = 0; i < buf.length; i++) {  
	                String hex = Integer.toHexString(buf[i] & 0xFF);  
	                if (hex.length() == 1) {  
	                        hex = '0' + hex;  
	                }  
	                sb.append(hex.toUpperCase());  
	        }  
	        return sb.toString();  
	}  
	
	public static byte[] parseHexStr2Byte(String hexStr) {  
        if (hexStr.length() < 1)  
                return null;  
        byte[] result = new byte[hexStr.length()/2];  
        for (int i = 0;i< hexStr.length()/2; i++) {  
                int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);  
                int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);  
                result[i] = (byte) (high * 16 + low);  
        }  
        return result;  
}  
	
	/*
	 * For test
	 */
	
	public static void main(String[] args) throws Exception 
	{
		AESUtil upAES = new AESUtil();
		upAES.init();
		upAES.test2("1006161");
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println("dateFormat:["+dateFormat.format(new Date())+"]");
	}	
	
	public void test2(String value) throws Exception 
	{
		String encryptedMessage = encryp(value);
		System.out.println("encryptedMessage:["+encryptedMessage+"]");
		String decryptedMessage = decryp(encryptedMessage);
		System.out.println("decryptedMessage:["+decryptedMessage+"]");
	}

}
