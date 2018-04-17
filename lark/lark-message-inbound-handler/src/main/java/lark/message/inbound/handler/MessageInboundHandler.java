package lark.message.inbound.handler;

public interface MessageInboundHandler{
	void handle(String channelId,String message);
}
