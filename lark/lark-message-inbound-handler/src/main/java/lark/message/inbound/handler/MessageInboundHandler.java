package lark.message.inbound.handler;

import lark.domain.AccessPoint;

public interface MessageInboundHandler{
	void handle(AccessPoint accessPoint,String message);
}
