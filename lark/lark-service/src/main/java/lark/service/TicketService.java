package lark.service;

public interface TicketService {
	
	String generateTicket(int authType,String userId,long ttl);
	
	String checkTicket(String ticket);

	String checkTicketAndResetTll(String ticket,long ttl);
	
}
