package utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.Manager;
import org.apache.catalina.Session;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.RequestFacade;

public class HttpUtils {

	private int activeUserNumber(HttpServletRequest request) {  
	    int activeSessions = 0;  
	    try {  
	        if (request instanceof RequestFacade) {  
	            Field requestField = request.getClass().getDeclaredField(  
	                    "request");  
	            requestField.setAccessible(true);  
	            Request req = (Request) requestField.get(request);  
	            org.apache.catalina.Context context = req.getContext();  
	            Manager manager = context.getManager();  
	            activeSessions = manager.getActiveSessions();  
	        }  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    }  
	    //log.info("users:{}", activeSessions);  
	    return activeSessions;  
	}  
	  
	private List activeUsers(HttpServletRequest request) {  
	    List list = new ArrayList();  
	    try {  
	        if (request instanceof RequestFacade) {  
	            Field requestField = request.getClass().getDeclaredField(  
	                    "request");  
	            requestField.setAccessible(true);  
	            Request req = (Request) requestField.get(request);  
	            org.apache.catalina.Context context = req.getContext();  
	            Manager manager = context.getManager();  
	  
	            Session[] sessions = manager.findSessions();  
	            for (Session session : sessions) {  
	                 //your  
	            }  
	        }  
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    }  
	    //log.info("users:{}", list);  
	    return list;  
	}  
}
