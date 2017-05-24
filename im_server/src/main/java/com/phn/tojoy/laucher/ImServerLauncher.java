package com.phn.tojoy.laucher;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.phn.tojoy.laucher.config.RootApplicationContextConfig;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ImServerLauncher {

	private static final String SPRING_PROFILE = "jpa";
	private static AnnotationConfigApplicationContext context;

	public static void main(String[] args) throws InterruptedException {
		// 将hook线程添加到运行时环境中去
		Runtime.getRuntime().addShutdownHook(new CleanWorkThread());
		context = new AnnotationConfigApplicationContext();
		context.register(RootApplicationContextConfig.class);
		context.getEnvironment().setDefaultProfiles(SPRING_PROFILE);
		context.refresh();
		
		ExitThread exitThreadf = new ExitThread();
		exitThreadf.start();
		log.info("Im Server Launch ");
	}

	static class CleanWorkThread extends Thread {
		@Override
		public void run() {
			context.close();
			log.info("Im Server Shutdown ");
			try {
				Thread.sleep(2 * 1000);// sleep 2s
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
