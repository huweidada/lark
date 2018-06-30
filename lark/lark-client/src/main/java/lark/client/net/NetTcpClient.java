package lark.client.net;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

public class NetTcpClient implements NetClient {
	private static final Logger logger = LoggerFactory.getLogger(NetClient.class);
	
	private String serverIp;
	private int serverPort;
	
	private EventLoopGroup group;
	private Channel channel;
	
	@Override
	public int init() {
		try{
			doInit();
		}catch(Exception e){
			logger.error("doInit fail",e);
			return -1;
		}
		return 0;
	}
	
	public NetTcpClient(String serverIp, int serverPort) {
		this.serverIp = serverIp;
		this.serverPort = serverPort;
	}

	private void doInit() throws Exception{
		group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class).handler(new TcpChannelInitializerHandler());
        channel = b.connect(serverIp, serverPort).sync().channel();
	}

	@Override
	public int send(String message) throws InterruptedException {
		int returnCode = -1;
		if(channel == null){
			returnCode = init();
			if(returnCode < 0) return -1;
		}
		ChannelFuture channelFuture = channel.writeAndFlush(Unpooled.copiedBuffer(message + "\r\n",CharsetUtil.UTF_8));
		boolean sendResult = channelFuture.await(5, TimeUnit.SECONDS);
		if(!sendResult){
			logger.error("channelFuture.await(5, TimeUnit.SECONDS) timeout");
			return 1;
		}
		return 0;
	}

	@Override
	public void close() {
		if(channel == null) return;
		group.shutdownGracefully();
		group = null;
		channel = null;
	}

	@Override
	public String getLocalIp() {
		if(channel == null){
			init();
		}
		if(channel != null) return ((InetSocketAddress)channel.localAddress()).getAddress().getHostAddress();
		return "";
	}

	@Override
	public int getLocalPort() {
		if(channel == null){
			init();
		}
		if(channel != null) return ((InetSocketAddress)channel.localAddress()).getPort();
		return -1;
	}

	@Override
	public String getNetType() {
		Enumeration<NetworkInterface> nifs;
		try {
			nifs = NetworkInterface.getNetworkInterfaces();
		} catch (SocketException e) {
			return "";
		} 
		
		String localIp = getLocalIp();
		
        while (nifs.hasMoreElements()) {  
            NetworkInterface nif = nifs.nextElement();  
            // 获得与该网络接口绑定的 IP 地址，一般只有一个  
            Enumeration<InetAddress> addresses = nif.getInetAddresses();  
            while (addresses.hasMoreElements()) {  
                InetAddress addr = addresses.nextElement();  
                if (addr instanceof Inet4Address) { // 只关心 IPv4 地址  
                	if(localIp.equals(addr.getHostAddress()))
                    return nif.getName();
                }  
            }  
        }  
        return "";
	}

}
