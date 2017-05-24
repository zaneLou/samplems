package org.jupport.web;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.github.cage.token.RandomTokenGenerator;
import com.github.cage.Cage;
import com.github.cage.IGenerator;
import com.github.cage.GCage;

@Controller
public class DefaultController {

	//code:Succes
    public static int UrlCodeSuccess = 200;
    public static int UrlCodeFail = 201;
    
    public static int UrlCodeNoNeedToUpdateVersion = 210; 
     
    //code:Register
    public static int UrlCodeDidUidRegister = 550;
    
    //code:Login
    public static int UrlCodeRegisterHasUsername = 551;
    public static int UrlCodeNotLogin = 560;
    public static int UrlCodeLoginFailed = 561;
    
    public static String jsonView =  "include/jsonObj";
    
	//Cage
	private static final Cage cage = new GCage();
	private final IGenerator<String> tokenGenerator = new RandomTokenGenerator(new Random(), 6);
	public static String registerCaptchaToken = "registerCaptchaToken";
	
    //Validate
	public static boolean isEmail(String email){  
        //\\w+@(\\w+.)+[a-z]{2,3}  
        String check = "\\w+@(\\w+.)+[a-z]{2,3}";  
        Pattern regex = Pattern.compile(check);  
        Matcher matcher = regex.matcher(email);   
        return matcher.matches();  
    } 
	
	/*
	 * Cage
	 */
	public void generateToken(HttpServletRequest request, String attributeName) 
	{
		final String token = tokenGenerator.next();
		request.getSession().setAttribute(attributeName, token);
	}

	public static String getToken(HttpServletRequest request, String attributeName) {
		final Object val = request.getSession().getAttribute(attributeName);
		return val != null ? val.toString() : null;
	}
	
	@RequestMapping("/registerCaptcha")
	public void captchaHandler(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		response.setContentType("image/" + cage.getFormat());
		response.setHeader("Cache-Control", "no-cache, no-store");
		response.setHeader("Pragma", "no-cache");
		final long time = System.currentTimeMillis();
		response.setDateHeader("Last-Modified", time);
		response.setDateHeader("Date", time);
		response.setDateHeader("Expires", time);
		
		generateToken(request, registerCaptchaToken);
		cage.draw(getToken(request, registerCaptchaToken), response.getOutputStream());
	}
	
	private static String getHeader(HttpServletRequest request, String headName) 
	{  
	    String value = request.getHeader(headName);  
	    return StringUtils.hasLength(value) && !"unknown".equalsIgnoreCase(value) ? value : "";  
	}  
	
	//RemoteAddrIp
	public static String getRemoteAddrIp(HttpServletRequest request) 
	{  
	    String ipFromNginx = getHeader(request, "X-Real-IP");  
	    //System.out.println("ipFromNginx:" + ipFromNginx);  
	    //System.out.println("getRemoteAddr:" + request.getRemoteAddr());  
	    return !StringUtils.hasLength(ipFromNginx) ? request.getRemoteAddr() : ipFromNginx;  
	} 
	
	//ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger(ch.qos.logback.classic.Logger.ROOT_LOGGER_NAME);
	//root.setLevel(Level.TRACE);
	//root.setLevel(Level.INFO);
	
	/*
	 * Handler
	 */
	
	@RequestMapping("/welcome")
	public String welcomeHandler(HttpServletRequest request, ServletResponse response, Model model) throws UnsupportedEncodingException 
	{
		return "welcome";
	}
	
	@RequestMapping("/unauthorized")
	public String unauthorizedHandler(HttpServletRequest request, ServletResponse response, Model model) throws UnsupportedEncodingException 
	{
		return "/unauthorized";
	}
	
	@RequestMapping("/loginXmlTip")
	public String loginXmlTip(HttpServletRequest request, ServletResponse response, Model model) throws UnsupportedEncodingException 
	{
		return "/f/user/loginXmlTip";
	}

	@RequestMapping("/loginJsonTip")
	public String loginJsonTip(HttpServletRequest request, ServletResponse response, Model model) throws UnsupportedEncodingException 
	{
		return "/f/user/loginJsonTip";
	}
	
}
