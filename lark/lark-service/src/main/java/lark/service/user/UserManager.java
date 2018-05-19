package lark.service.user;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import lark.domain.Account;
import lark.domain.User;

//对每一次登录的用户进行管理，需要精细化操作，不然可能出现内部泄露，导致系统崩溃
public class UserManager {
	private static final Logger logger = LoggerFactory.getLogger(UserManager.class);
	
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
		logger.info("account=[{}]",JSON.toJSONString(account));
		
		if(account == null) return;
		
		User user = userIdUserMap.get(account.getUserId());
		logger.info("user=[{}]",JSON.toJSONString(user));
		
		if(user == null) throw new RuntimeException("user == null");

		user.delAccount(account);
		
		if(!user.isExistAccount()){
			userIdUserMap.remove(account.getUserId());
		}
		
	}
	
	
	public static User getUserByUserId(String userId){
		return userIdUserMap.get(userId);
	}
	
	public static Account getAccountByChannelId(String channelId){
		return channelIdAccountMap.get(channelId);
	}
	
	/*public static User getUserByChannelId(String channelId){
		Account account = channelIdAccountMap.get(channelId);
		if(account == null) return null;
		return getUserByUserId(account.getUserId());
	}*/
	
	
	public static Map<String,User> listUser(){
		/*Set<String> userIdSet = userIdUserMap.keySet();
		List<String> userIdList = new LinkedList<String>();
		for (String userId : userIdSet) {
			userIdList.add(userId);
		}
		return userIdList;*/
		return userIdUserMap;
	}
}
