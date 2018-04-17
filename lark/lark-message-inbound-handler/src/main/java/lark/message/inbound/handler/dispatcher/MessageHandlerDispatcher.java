package lark.message.inbound.handler.dispatcher;

public interface MessageHandlerDispatcher {
	void dispatch(String channelId,String message);
}
