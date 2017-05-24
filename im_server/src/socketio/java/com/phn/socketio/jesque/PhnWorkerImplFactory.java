package com.phn.socketio.jesque;

import java.util.Collection;
import java.util.concurrent.Callable;

import net.greghaines.jesque.Config;
import net.greghaines.jesque.worker.JobFactory;

public class PhnWorkerImplFactory implements Callable<PhnWorkerImpl> {
    
    private final Config config;
    private final Collection<String> queues;
    private final JobFactory jobFactory;
    protected JobListener jobListener;
    
	/**
     * Create a new factory. Returned <code>WorkerImpl</code>s will use the provided arguments.
     * @param config used to create a connection to Redis and the package prefix for incoming jobs
     * @param queues the list of queues to poll
     * @param jobFactory the job factory that materializes the jobs
     */
    public PhnWorkerImplFactory(final Config config, final Collection<String> queues,
            final JobFactory jobFactory, JobListener jobListener) {
        this.config = config;
        this.queues = queues;
        this.jobFactory = jobFactory;
        this.jobListener = jobListener;
    }

    /**
     * Create a new <code>WorkerImpl</code> using the arguments provided to this factory's constructor.
     * @return a new <code>WorkerImpl</code>
     */
    @Override
	public PhnWorkerImpl call() {
        return new PhnWorkerImpl(this.config, this.queues, this.jobFactory, this.jobListener);
    }
}
