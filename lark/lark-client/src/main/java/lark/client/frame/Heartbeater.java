package lark.client.frame;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import lark.client.net.MessageHandlerDispatcher;
import lark.client.net.MessageInboundHandler;
import lark.client.net.NetClient;
import lark.domain.message.heartbeat.HeartbeatReq;
import lark.domain.message.heartbeat.HeartbeatReqBody;
import lark.domain.message.heartbeat.HeartbeatResp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

public class Heartbeater {
	private static final Logger logger = LoggerFactory.getLogger(Heartbeater.class);
	public Heartbeater() {
	}
	private Thread thread;
	private String heartbeatTransactionId = "";
	
	private Lock lock = new ReentrantLock();
	private Condition condition = lock.newCondition();
	
	public void start(String ticket,final NetClient netClient){
		MessageHandlerDispatcher.registerMessageHandler("heartbeat", new HeartbeatHandler());
		
		logger.info("ticket=[{}]",ticket);
		HeartbeatReqBody body = new HeartbeatReqBody();
		body.setTicket(ticket);
		heartbeatTransactionId = UUID.randomUUID().toString();
		HeartbeatReq heartbeatReq = new HeartbeatReq();
		heartbeatReq.setTransactionId(heartbeatTransactionId);
		heartbeatReq.setType("heartbeat");
		heartbeatReq.setVersion(1);
		heartbeatReq.setTime(System.currentTimeMillis());
		heartbeatReq.setBody(body);
		final String heartbeatMessage = JSON.toJSONString(heartbeatReq);
		
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				int sendTimes = 0;
				while(true){
					netClient.send(heartbeatMessage);
					logger.info("sendTimes=[{}]",sendTimes++);
					boolean heartbeatResult = false;
					try {
						lock.lock();
						heartbeatResult = condition.await(5, TimeUnit.SECONDS);
					} catch (InterruptedException e) {
						logger.info("InterruptedException");
						continue;
					}finally{
						lock.unlock();
					}
					
					if(!heartbeatResult){
						//需要通知上层，系统网络层出问题了，可能要执行一个弹出框提示，需要重新初始化网络层
						logger.error("heartbeatResult == false");
						break;
					}
					
					try {
						Thread.sleep(45*1000L);
					} catch (InterruptedException e) {
						logger.info("Thread.sleep(45*1000L) InterruptedException",e);
					}
				}
			}
		});
		
		thread.start();
		
	}
	
	public class HeartbeatHandler implements MessageInboundHandler{
		@Override
		public void handle(String message) {
			HeartbeatResp heartbeatResp = JSON.parseObject(message,HeartbeatResp.class);
			if(heartbeatResp.getStatusCode() == 1 && heartbeatTransactionId.equals(heartbeatResp.getTransactionId())){
				logger.info("心跳成功");
				lock.lock();
				try{
					condition.signal();
				}catch(Exception e){
					logger.error("condition.signal fail",e);
				}finally{
					lock.unlock();
				}
			}
		}
		
	}
}
