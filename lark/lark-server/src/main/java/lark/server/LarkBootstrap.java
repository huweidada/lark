package lark.server;

import java.net.InetSocketAddress;

import lark.server.handler.ChannelInitializerHandler;
import lark.service.message.context.MessageContext;
import lark.tools.NamedThreadFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LarkBootstrap {
	private static final Logger logger = LoggerFactory.getLogger(LarkBootstrap.class);
	
	public LarkBootstrap init(){
		MessageContext.registerMessageHandlerProvider();
		return this;
	}
	
	public void start(short port, int workThreadNumber) throws Exception{
		EventLoopGroup bossGroup = new NioEventLoopGroup(1, new NamedThreadFactory("netty-boss"));
		EventLoopGroup workGroup = new NioEventLoopGroup(workThreadNumber, new NamedThreadFactory("netty-worker"));
		
		try{
			ServerBootstrap serverBootstrap = new ServerBootstrap();
			serverBootstrap.group(bossGroup,workGroup)
			.channel(NioServerSocketChannel.class)
			.option(ChannelOption.SO_BACKLOG, 128)
			.option(ChannelOption.SO_REUSEADDR, true)
			.localAddress(new InetSocketAddress(port))
			.childHandler(new ChannelInitializerHandler())
			.childOption(ChannelOption.TCP_NODELAY,true)
			.childOption(ChannelOption.SO_KEEPALIVE,true);
			
			
			ChannelFuture channelFuture = serverBootstrap.bind().sync();
			
			logger.info("MessageServer start...");
			
			channelFuture.channel().closeFuture().sync();
		}catch(Throwable e){
			logger.info("MessageServer start fail...",e);
			if(e instanceof java.net.BindException){
				System.exit(0);
			}
		}finally{
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
			logger.info("MessageServer shutdown...");
		}
		
	}
}
