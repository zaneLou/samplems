package com.phn.socketio.listener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.corundumstudio.socketio.AckRequest;
import com.corundumstudio.socketio.SocketIOClient;
import com.corundumstudio.socketio.listener.DataListener;
import com.phn.base.component.PhnConstants;
import com.phn.proto.PhnNetBuf.PhnDataBuf;
import com.phn.proto.PhnNetBuf.PhnLoginBuf;
import com.phn.socketio.jesque.JobListener;
import com.phn.socketio.jesque.PhnJobFactory;
import com.phn.socketio.jesque.PhnWorkerImplFactory;
import com.phn.socketio.manager.UserManager;

import lombok.extern.slf4j.Slf4j;
import net.greghaines.jesque.Config;
import net.greghaines.jesque.ConfigBuilder;
import net.greghaines.jesque.Job;
import net.greghaines.jesque.client.Client;
import net.greghaines.jesque.client.ClientPoolImpl;
import net.greghaines.jesque.utils.PoolUtils;
import net.greghaines.jesque.worker.WorkerPool;

@Slf4j
@Component
//byte[]
public class PhnDataListener implements DataListener<String>, JobListener {

	@Value("${phn.redis.worker.host:127.0.0.1}")
	protected String redisHost;

	@Value("${phn.redis.worker.queue:phn:queue:data}")
	protected String queue;

	@Autowired
	protected UserManager userManager;

	protected Client jesqueClientPool;
	protected WorkerPool workerPool;
	protected Thread workerThread;
	
	public static AtomicInteger onDataCount = new AtomicInteger(0);
	
	@PostConstruct
	public void postConstruct() {
		final ConfigBuilder configBuilder = new ConfigBuilder();
		Config config = configBuilder.build();
		jesqueClientPool = new ClientPoolImpl(config, PoolUtils.createJedisPool(config));
		configBuilder.withHost(redisHost);
		Collection<String> queues = new ArrayList<String>();
		queues.add(queue);
		PhnWorkerImplFactory workerFactory = new PhnWorkerImplFactory(config, queues, new PhnJobFactory(), this);
		workerPool = new WorkerPool(workerFactory, 50);
		workerThread = new Thread(workerPool);
		workerThread.start();

		log.info("postConstruct queues : {}", queues);
	}

	@PreDestroy
	public void preDestroy() {
		jesqueClientPool.end();
		workerPool.end(true);
		try {
			workerThread.join();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onData(SocketIOClient client, String value, AckRequest ackRequest) throws Exception {
		byte[] bytes = value.getBytes("UTF-8");
		log.info("server ondata onDataCount {} ", onDataCount.addAndGet(1));
		PhnDataBuf dataBuf = PhnDataBuf.parseFrom(bytes);
		log.info("server onData client {} datatype {} PhnDataBuf {}", client.getSessionId(), dataBuf.getDataType(),
				dataBuf);
		// check is ack requested by client,
		ackRequest.sendAckData(dataBuf.getDataId());
		if (userManager.hadProcessData(client.getSessionId().toString(), dataBuf.getDataId())) {
			return;
		}
		String content = value;// new String(bytes, "UTF-8");
		final Job job = new Job(dataBuf.getDataId(), new Object[] { client.getSessionId(), content });
		jesqueClientPool.enqueue(queue, job);
	}

	@Override
	public Object onJob(Job job) throws Exception {
		log.info("server onJob");
		try {
			String sessionId = (String)job.getArgs()[0];
			String content = (String)job.getArgs()[1];
			byte[] bytes = content.getBytes("UTF-8");
			PhnDataBuf dataBuf = PhnDataBuf.parseFrom(bytes);
			log.info("server onJob client {} datatype {} PhnDataBuf {}", sessionId, dataBuf.getDataType(),
					dataBuf);
			switch (dataBuf.getDataType()) {
			case PhnConstants.SocketIO_Data_Type_LoginByUsername:
				PhnDataBuf resultDataBuf = userManager.validate(dataBuf.getDevice(),
						PhnLoginBuf.parseFrom(dataBuf.getDataObj()));
				/*
				client.sendEvent(PhnConstants.SocketIO_Data_Event, new AckCallback<String>(String.class) {
					@Override
					public void onSuccess(String result) {
						log.info("ack from client: " + client.getSessionId() + " data: " + result);
					}
				}, resultDataBuf.toByteArray());
				*/
				break;
			default:
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
