package lark.client.message.inbound.handler;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import lark.client.domain.ApplicationContext;
import lark.client.domain.MessageType;
import lark.client.net.NetClient;
import lark.domain.message.heartbeat.HeartbeatReq;
import lark.domain.message.heartbeat.HeartbeatReqBody;
import lark.domain.message.heartbeat.HeartbeatResp;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

public class HeartbeatFrame implements MessageInboundHandler{
	private static final Logger logger = LoggerFactory.getLogger(HeartbeatFrame.class);
	private NetClient netClient;
	public HeartbeatFrame(NetClient netClient) {
		MessageInboundHandlerDispatcher.registerMessageHandler(MessageType.heartbeat, this);
		this.netClient = netClient;
	}
	private Thread thread;
	private AtomicReference<String> heartbeatTransactionId = new AtomicReference<String>();
	private AtomicBoolean serverStatus = new AtomicBoolean(true);
	
	
	public void start(){
		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				int sendTimes = 0;
				while(true){
					String ticket = ApplicationContext.getTicket().get();
					if(StringUtils.isBlank(ticket)){
						logger.error("StringUtils.isBlank(ticket) == true");
						break;
					}
					if(!serverStatus.compareAndSet(true, false)){
						logger.error("serverStatus == false");
						break;
					}
					
					
					logger.info("ticket=[{}]",ticket);
					HeartbeatReqBody body = new HeartbeatReqBody();
					body.setTicket(ticket);
					String transactionId = UUID.randomUUID().toString();
					heartbeatTransactionId.set(transactionId);
					HeartbeatReq heartbeatReq = new HeartbeatReq();
					heartbeatReq.setTransactionId(transactionId);
					heartbeatReq.setType("heartbeat");
					heartbeatReq.setVersion(1);
					heartbeatReq.setTime(System.currentTimeMillis());
					heartbeatReq.setBody(body);
					String heartbeatMessage = JSON.toJSONString(heartbeatReq);
					
					
					logger.info("sendTimes=[{}]",sendTimes++);
					int returnCode = -1;
					try {
						returnCode = netClient.send(heartbeatMessage);
					} catch (InterruptedException e1) {
						logger.error("netClient.send fail",e1);
					}
					
					if(returnCode != 0){
						logger.error("netClient.send(heartbeatMessage[{}]) fail",heartbeatMessage);
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
	
	/*public class HeartbeatHandler implements MessageInboundHandler{
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
		
	}*/

	@Override
	public void handle(String message) {
		HeartbeatResp heartbeatResp = JSON.parseObject(message,HeartbeatResp.class);
		if(heartbeatResp.getStatusCode() == 1 && heartbeatTransactionId.get().equals(heartbeatResp.getTransactionId())){
			logger.info("心跳成功");
			serverStatus.set(true);
		}
		
	}
}
