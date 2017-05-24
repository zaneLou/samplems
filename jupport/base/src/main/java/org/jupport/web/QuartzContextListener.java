package org.jupport.web;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;


public class QuartzContextListener implements ServletContextListener{

	@Override  
    public void contextDestroyed(ServletContextEvent arg0) 
	{  
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();

        Scheduler scheduler = (Scheduler) context.getBean("quartzSchedulerFactory");
        try {
			scheduler.shutdown(true);
		} catch (SchedulerException e1) {
			e1.printStackTrace();
		}
		
        /*
        WebApplicationContext webApplicationContext = (WebApplicationContext) arg0.getServletContext()  
                .getAttribute( WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);  
        org.quartz.impl.StdScheduler startQuertz = (org.quartz.impl.StdScheduler) webApplicationContext.getBean("startQuertz");  
        if(startQuertz != null) {  
            startQuertz.shutdown();  
        }  
        */
        try {  
            Thread.sleep(500);  
        } catch (InterruptedException e) {  
            e.printStackTrace();  
        }  
    }  
  
    /* 
     * (non-Javadoc) 
     *  
     * @see 
     * javax.servlet.ServletContextListener#contextInitialized(javax.servlet 
     * .ServletContextEvent) 
     */  
    @Override  
    public void contextInitialized(ServletContextEvent arg0) {  }  

    
}
