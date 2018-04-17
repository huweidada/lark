package lark.service.user;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import lark.domain.Account;
import lark.domain.User;

//对每一次登录的用户进行管理，需要精细化操作，不然可能出现内部泄露，导致系统崩溃
public class UserManager {
	private static Map<String,User> userIdUserMap = new ConcurrentHashMap<String,User>();
	private static Map<String,Account> channelIdAccountMap = new ConcurrentHashMap<String,Account>();
	
	public static void registerAccount(Account account){		
		channelIdAccountMap.put(account.getChannelId(), account);//这个是为了查询方便
		
		User user = userIdUserMap.get(account.getUserId());
		if(user == null){
			user = new User(account);
			userIdUserMap.put(account.getUserId(), user);
		}else{
			user.addAccount(account);
		}
		
	}
	
	public static void unregisterAccount(Account account){
		channelIdAccountMap.remove(account.getChannelId());
		
		User user = userIdUserMap.get(account.getUserId());
		if(user == null) throw new RuntimeException("user == null");

		user.delAccount(account);
		
		if(!user.isExistAccount()){
			userIdUserMap.remove(account.getUserId());
		}
		
	}
	
	public static void unregisterAccountByChannelId(String channelId){
		Account account = channelIdAccountMap.remove(channelId);
		if(account == null) return;
		
		User user = userIdUserMap.get(account.getUserId());
		if(user == null) throw new RuntimeException("user == null");

		user.delAccount(account);
		
		if(!user.isExistAccount()){
			userIdUserMap.remove(account.getUserId());
		}
		
	}
	
	
	public static User getUser(String userId){
		return userIdUserMap.get(userId);
	}
	
	
	public static List<String> listUser(){
		Set<String> userIdSet = userIdUserMap.keySet();
		List<String> userIdList = new LinkedList<String>();
		for (String userId : userIdSet) {
			userIdList.add(userId);
		}
		return userIdList;
	}
}
