package com.phn.embryo;

import java.util.List;

public interface EmbryoPacket {

	//register login logout connect ack message 
	public static String PATH_REGISTER = "/__register";
	public static String PATH_LOGIN = "/__login";
	public static String PATH_CONNECT = "/__connect";
	public static String PATH_ACK = "/__ack";
	public static String PATH_MESSAGE = "/__message";
	
	public static String USERNAME = "username";
	public static String PASSWORD = "password";

	public String getPacketId(); 
	
	public void setAckId(long ackId);
	
	public long getAckId();
	
	public String getNamespace();
	
	public String getPath();
	
	public String getHash();
	
	public List<EmbryoEntry> getHeadsList();
	
	public List<EmbryoEntry> getParams();
	
	public byte[] getBytes();

}
