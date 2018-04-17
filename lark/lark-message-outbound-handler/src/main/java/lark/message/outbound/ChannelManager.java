package lark.message.outbound;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


//以Channel的维度看，Channel的事件都是串行执行的
//但是以用户的角度看，则用户的事件是有可能发生在多个Channel上的
//所以针对的多个用户的事件是非串行的，针对同一个用户的信息的操作最好是加锁
public class ChannelManager {
	private static Map<String,Channel> mapChannel = new ConcurrentHashMap<String,Channel>();
	
	public static void registerChannel(String channelId,Channel channel){		
		mapChannel.put(channelId, channel);
	}
	
	public static void unregisterChannel(String channelId){		
		mapChannel.remove(channelId);
	}
	
	
	public static Channel getChannel(String channelId){
		return mapChannel.get(channelId);
	}
}
