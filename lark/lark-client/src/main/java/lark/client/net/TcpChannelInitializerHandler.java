package lark.client.net;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;

public class TcpChannelInitializerHandler  extends ChannelInitializer<SocketChannel>{

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline channelPipeline = ch.pipeline();
		channelPipeline.addLast(new LineBasedFrameDecoder(64 * 1024));
		channelPipeline.addLast(TcpMessageInboundHandler.getInstance());
	}

}
