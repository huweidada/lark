package lark.message.inbound.handler.dispatcher;

import lark.domain.AccessPoint;

public interface MessageHandlerDispatcher {
	//void dispatch(String channelId,String message);
	void dispatch(AccessPoint accessPoint,String message);
}
