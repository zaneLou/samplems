package com.phn.socketio.jesque;

import net.greghaines.jesque.Job;

public interface JobListener {

	public Object onJob(final Job job) throws Exception;
}
