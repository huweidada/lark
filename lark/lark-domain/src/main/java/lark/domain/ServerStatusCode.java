package lark.domain;


//注意100以下为公共的返回码定义，100以上的才是业务产生的编码
//返回码格式为：xxx只有三位，并且采用静态变量的定义法
public class ServerStatusCode {
	public static int success = 1;
	public static int parameterError = 2;
	
	public static int systemError = 3;
	public static int systemBusy = 4;//系统繁忙，稍后可以重试
	public static int frequencyLimitation = 5;//访问频率太快，稍后可以重试
	public static int flowLimitation = 6;//流量限制，稍后可以重试
	
	public static int ipBlacklist = 7;//命中了ip黑名单
	public static int macBlacklist = 8;//命中了mac黑名单
	
	//10-40段专门划分用于登录相关的错误码，因为登录认证相关的错误码所有的协议都会引用
	public static int notLogin = 10;//用户未登录
	public static int ticketTimeout = 11;//票据过期
	public static int ticketError = 12;//票据非法
	
}


