package lark.server;

import lark.server.websocket.handler.WebSocketChannelInitializer;
import lark.service.message.handler.provider.register.HandlerProviderRegister;
import lark.tools.NamedThreadFactory;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LarkWebsocketBootstrap {
	private static final Logger logger = LoggerFactory.getLogger(LarkWebsocketBootstrap.class);
	
	public LarkWebsocketBootstrap init(){
		HandlerProviderRegister.registerMessageHandlerProvider();
		return this;
	}
	
	public void start(short port, int workThreadNumber) throws Exception{
		EventLoopGroup bossGroup = new NioEventLoopGroup(1, new NamedThreadFactory("netty-boss"));
		EventLoopGroup workGroup = new NioEventLoopGroup(workThreadNumber, new NamedThreadFactory("netty-worker"));
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workGroup)
             .channel(NioServerSocketChannel.class)
             .handler(new LoggingHandler(LogLevel.INFO))
             .childHandler(new WebSocketChannelInitializer());
            Channel ch = b.bind(port).sync().channel();

            logger.info("Open your web browser and navigate to " +
                    ("http") + "://127.0.0.1:" + port + '/');

            ch.closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
	}
}
