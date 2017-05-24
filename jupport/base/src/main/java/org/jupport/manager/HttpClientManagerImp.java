package org.jupport.manager;

import java.io.File;
import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.fluent.Request;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class HttpClientManagerImp implements HttpClientManager{

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	
	protected CloseableHttpClient 		httpclient;
	protected ResponseHandler<String> 	responseHandler = new BasicResponseHandler();
	
	@PostConstruct
	public void postConstruct() 
	{
		String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_3) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/38.0.2125.122 Safari/537.36";
		//BROWSER_COMPATIBILITY
		httpclient = HttpClientBuilder.create().setUserAgent(USER_AGENT)
				.setDefaultRequestConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.BEST_MATCH).setConnectionRequestTimeout(30000).setConnectTimeout(30000).build())
				/*
				.setRedirectStrategy(new RedirectStrategy()
				{
					@Override
					public boolean isRedirected(HttpRequest paramHttpRequest,
							HttpResponse paramHttpResponse, HttpContext paramHttpContext)
							throws ProtocolException {
						// TODO Auto-generated method stub
						return false;
					}
					
					@Override
					public HttpUriRequest getRedirect(HttpRequest paramHttpRequest,
							HttpResponse paramHttpResponse, HttpContext paramHttpContext)
							throws ProtocolException {
						// TODO Auto-generated method stub
						return null;
					}
				})
				*/
			 .build();
		//httpclient = HttpClients.createDefault();
	}
	
	
	@PreDestroy
    public void preDestroy()
    {
		try {
			httpclient.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
	
	@Override
	public String getHttpContent(String url)
	{
		try {
			return Request.Get(url) .execute().returnContent().asString();
		}
		catch (HttpResponseException e) {
			logger.error("+++HttpResponseException getHttpContent+++ " + e.getStatusCode() +  "[url]" + url) ;
			e.printStackTrace();
		} 
		catch (IOException e) {
			logger.error("+++getHttpContent+++[url]" + url) ;
			e.printStackTrace();
		}
		return "";
	}
   
	@Override
	public String postHttpContent(String url)
	{
		try {
			return Request.Post(url) .execute().returnContent().asString();
		}
		catch (HttpResponseException e) {
			logger.error("+++HttpResponseException getHttpContent+++ " + e.getStatusCode() +  "[url]" + url) ;
			e.printStackTrace();
		} 
		catch (IOException e) {
			logger.error("+++getHttpContent+++[url]" + url) ;
			e.printStackTrace();
		}
		return "";
	}
	
	@Override
	public void getDownloadHttpContent(String url, String forder, String name) 
	{
		// mkdirs
		File foderFile = new File(forder);
		if(!foderFile.exists())
			foderFile.mkdirs();
		
		try {
			Request.Get(url)
			.useExpectContinue()
			.execute().saveContent(new File(forder+name));
		}
		catch (HttpResponseException e) {
			logger.error("+++HttpResponseException getDownloadHttpContent+++ " + e.getStatusCode() +  "[url]" + url) ;
			e.printStackTrace();
		}
		catch (IOException e) {
			logger.error("+++IOException postDownloadHttpContent+++[url]" + url) ;
			e.printStackTrace();
		}
	}
}
