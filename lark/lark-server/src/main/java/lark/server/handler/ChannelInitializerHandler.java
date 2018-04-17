package lark.server.handler;
import java.util.concurrent.TimeUnit;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.timeout.IdleStateHandler;

public class ChannelInitializerHandler extends ChannelInitializer<SocketChannel> {

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline channelPipeline = ch.pipeline();
		channelPipeline.addLast(new IdleStateHandler(0, 0, 60, TimeUnit.SECONDS));
		channelPipeline.addLast(new LineBasedFrameDecoder(64 * 1024));
		channelPipeline.addLast(MessageInboundHandler.getInstance());
		channelPipeline.addLast(MessageOutboundHandler.getInstance());
	}
}
