package lark.server.handler;

import lark.message.outbound.handler.MessageOutboundHandlerManager;
import lark.message.outbound.handler.provider.MessageOutboundHandlerProvider;
import lark.message.outbound.handler.provider.TcpMessageOutboundHandlerProvider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;


@Sharable
public class MessageOutboundHandler extends ChannelOutboundHandlerAdapter{
	private static final Logger logger = LoggerFactory.getLogger(MessageOutboundHandler.class);
	
	private static MessageOutboundHandler instance;
	//private MessageHandlerDispatcher dispatcher; 

	public static MessageOutboundHandler getInstance(){
		if(instance == null){
			instance = new MessageOutboundHandler();
		}
		return instance;
	}
	private MessageOutboundHandler() {
		MessageOutboundHandlerProvider tcpMessageHandlerProvider = new TcpMessageOutboundHandlerProvider();
		MessageOutboundHandlerManager.registerProvider(tcpMessageHandlerProvider.getName(), tcpMessageHandlerProvider);
		logger.info("MessageOutboundHandler created");
	}
	
	@Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
		logger.info("MessageOutboundHandler write begin");
        ctx.write(msg, promise);
    }
	
	@Override
    public void flush(ChannelHandlerContext ctx) throws Exception {
		logger.info("MessageOutboundHandler flush begin");
        ctx.flush();
    }
}
