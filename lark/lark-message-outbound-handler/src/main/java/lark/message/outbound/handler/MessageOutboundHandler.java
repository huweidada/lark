package lark.message.outbound.handler;

public interface MessageOutboundHandler {
	long write(String channelId,String message);
}
