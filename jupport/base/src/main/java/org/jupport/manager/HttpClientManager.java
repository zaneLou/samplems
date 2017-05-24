package org.jupport.manager;


public interface HttpClientManager {

	public String getHttpContent(String url);
	public String postHttpContent(String url);
	public void getDownloadHttpContent(String url, String forder, String name) ;
}
