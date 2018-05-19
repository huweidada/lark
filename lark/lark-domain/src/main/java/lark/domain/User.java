package lark.domain;

import java.io.Serializable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@ToString
public class User implements Serializable{
	public User(Account account) {
		if(account == null) throw new RuntimeException("account == null");
		
		userId = account.getUserId();
		
		channelIdAccountMap = new ConcurrentHashMap<String, Account>();
		channelIdAccountMap.put(account.getChannelId(), account);
		
		activeChannelId = account.getChannelId();
		
		existAccount = true;
		
	}
	
	
	public void addAccount(Account account) {
		if(account == null) throw new RuntimeException("account == null");
		
		channelIdAccountMap.put(account.getChannelId(), account);
		
		existAccount = true;
		
	}
	
	
	public void delAccount(Account account){
		if(account == null) throw new RuntimeException("account == null");
		
		channelIdAccountMap.remove(account.getChannelId());
		
		if(account.getChannelId().equals(activeChannelId)){
			activeChannelId = null;
		}
		
		if(channelIdAccountMap.size() == 0){
			userId = null;
			channelIdAccountMap = null;
			existAccount = false;
		}
	}
	
	public void updateActiveChannelId(String channelId){
		if(channelIdAccountMap.containsKey(activeChannelId)){
			activeChannelId = channelId;
		}else{
			throw new RuntimeException("channelIdAccountMap.containsKey(activeChannelId) == false");
		}
	}
		

	private static final long serialVersionUID = 1L;
	@Getter @Setter private String userId;
	
	@Getter @Setter private Map<String,Account> channelIdAccountMap;
	
	@Getter @Setter private String activeChannelId;
	
	@Getter @Setter private boolean existAccount;
}
