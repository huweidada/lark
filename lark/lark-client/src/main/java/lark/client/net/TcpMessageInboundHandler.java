package lark.client.net;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.apache.commons.lang3.StringUtils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;
import io.netty.channel.ChannelHandler.Sharable;

@Sharable
public class TcpMessageInboundHandler extends ChannelInboundHandlerAdapter{
	private static final Logger logger = LoggerFactory.getLogger(TcpMessageInboundHandler.class);
	
	private static TcpMessageInboundHandler instance;
	public static TcpMessageInboundHandler getInstance(){
		if(instance == null){
			instance = new TcpMessageInboundHandler();
		}
		return instance;
	}
	private TcpMessageInboundHandler() {
		super();
	}
	
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
	}
	
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(msg == null) return;
        ByteBuf byteBuf = (ByteBuf)msg;
        String message = byteBuf.toString(CharsetUtil.UTF_8);   
        
        if(StringUtils.isBlank(message)){
        	logger.info("StringUtils.isBlank(message) == true");
        	return;
        }
        
        logger.info("received message = [{}]",message);
        
        MessageHandlerDispatcher.dispatch(message);
    }
	
	@Override
	public void userEventTriggered(ChannelHandlerContext ctx,Object evt) throws Exception {
	}
	
	
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        logger.error("exceptionCaught",cause);
        try{
        	 ctx.close();
        }catch(Exception e){
        	logger.error("ctx.close() fail",e);
        }
    }
}
