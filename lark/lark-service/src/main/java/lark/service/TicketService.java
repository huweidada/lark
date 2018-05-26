package lark.service;

public interface TicketService {
	
	String generateTicket(int authType,String userId);
	
	String checkTicket(String ticket);

	String checkTicketAndResetTll(String ticket);
	
}
