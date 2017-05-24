package com.phn.socketio.jesque;

import net.greghaines.jesque.Job;
import net.greghaines.jesque.worker.JobFactory;

public class PhnJobFactory implements JobFactory{

	@Override
	public Object materializeJob(Job job) throws Exception {
		return null;
	}

}
