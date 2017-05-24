package com.phn.socketio.jesque;

import static net.greghaines.jesque.worker.WorkerEvent.JOB_EXECUTE;

import java.util.Collection;
import lombok.extern.slf4j.Slf4j;
import net.greghaines.jesque.Config;
import net.greghaines.jesque.Job;
import net.greghaines.jesque.worker.JobFactory;
import net.greghaines.jesque.worker.WorkerAware;
import net.greghaines.jesque.worker.WorkerImpl;

/*
	AdminImpl
	AdminPoolImpl
	WorkerImpl
	WorkerImplFactory
	WorkerPool
	WorkerPoolImpl
	WorkerPoolImplFactory
	//replace Callable
*/
@Slf4j
public class PhnWorkerImpl extends WorkerImpl {

	protected JobListener jobListener;
	
	public PhnWorkerImpl(Config config, Collection<String> queues, JobFactory jobFactory, JobListener jobListener) {
		super(config, queues, jobFactory);
		this.jobListener = jobListener;
	}

	@Override
	protected Object execute(final Job job, final String curQueue, final Object instance) throws Exception {
		log.info("PhnWorkerImpl execute");
        if (instance instanceof WorkerAware) {
            ((WorkerAware) instance).setWorker(this);
        }
        this.listenerDelegate.fireEvent(JOB_EXECUTE, this, curQueue, job, instance, null, null);
        final Object result = jobListener.onJob(job);
        return result;
    }
}
