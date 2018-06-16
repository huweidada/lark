package lark.service.impl;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lark.service.TicketService;

public class TicketServiceImpl implements TicketService {
	private static final Logger logger = LoggerFactory.getLogger(TicketServiceImpl.class);
	private String privateKey = "5dd6e74aa327a4er";
	private SecretKeySpec secretKeySpec = new SecretKeySpec(privateKey.getBytes(), "AES");
	
	private String initVectorKey = "0102030405060708";
	private IvParameterSpec ivParameterSpec = new IvParameterSpec(initVectorKey.getBytes());
	
	private String rawMaterialDelimiter = "###";
	
	private String charset = "UTF-8";
	
	private long ticketTtl = 1*60*60;
	
	private Map<String,Long> ticketMap;
	private Thread expiredTicketCleanThread;
	
	
	private static TicketService instance;
	
	public static TicketService getInstance(){
		if(instance != null) return instance;
		
		instance = new TicketServiceImpl();
		return instance;
	}
	
	
	private TicketServiceImpl() {
		ticketMap = new ConcurrentHashMap<String, Long>();	
		
		expiredTicketCleanThread = new ExpiredTicketCleanThread();
		expiredTicketCleanThread.start();
	}
	
	/**
	 * 设计ticket的格式，随机数+"###"+authType+"###"+userId，这里需要和userId生成的地方约定
	 * userId不能包含"###"
	 */
	@Override
	public String generateTicket(int authType, String userId) {
		logger.info("authType=[{}],userId=[{}]",authType,userId);
		if(StringUtils.isBlank(userId)){
			logger.error("StringUtils.isBlank(userId) == true");
			throw new RuntimeException("StringUtils.isBlank(userId) == true");
		}
		long random = (long) (Math.random() * 99982507);
		String source = String.valueOf(random) + rawMaterialDelimiter + String.valueOf(authType) + rawMaterialDelimiter + userId;
		String ticket = aesEncrypt(source);
		ticketMap.put(ticket, ticketTtl + System.currentTimeMillis()/1000 );
		return ticket;
	}

	/**
	 * 设计ticket的格式，随机数+"###"+authType+"###"+userId，这里需要和userId生成的地方约定
	 * userId不能包含"###"
	 */
	@Override
	public String checkTicket(String ticket) {
		logger.info("ticket=[{}]",ticket);
		//首先需要对长度进行检查，如果ticket太长就直接抛出异常
		if(StringUtils.isBlank(ticket) || ticket.length() > 256){
			logger.error("ticket=[{}]",ticket);
			logger.error("StringUtils.isBlank(ticket) || ticket.length() > 256");
			throw new RuntimeException("StringUtils.isBlank(ticket) || ticket.length() > 256");
		}
		
		Long deadline = ticketMap.get(ticket);
		if(deadline == null){
			logger.info("deadline == null,ticket=[{}]",ticket);
			return null;
		}
		
		long ttl = deadline - System.currentTimeMillis()/1000;
		if(ttl < 0){
			logger.info("ttl[{}] < 0",ttl);
			return null;
		}
		//第一步是检查其是否过期
		
		
		String plainTicket = aesDecrypt(ticket);
		String[] rawMaterial = plainTicket.split(rawMaterialDelimiter);
		
		if(rawMaterial.length != 3){
			logger.error("rawMaterial.length[{}] != 3",rawMaterial.length);
			throw new RuntimeException("rawMaterial.length != 3");
		}
		
		//int authType = Integer.valueOf(rawMaterial[1]);
		String userId = rawMaterial[2];
		if(StringUtils.isBlank(userId)){
			logger.error("StringUtils.isBlank(userId) == true");
			throw new RuntimeException("StringUtils.isBlank(userId) == true");
		}
		return userId;
	}

	@Override
	public String checkTicketAndResetTll(String ticket) {
		String userId = checkTicket(ticket);
		if(StringUtils.isBlank(userId)) return null;
		
		ticketMap.put(ticket, ticketTtl + System.currentTimeMillis()/1000 );
		return userId;
	}
	
	private String aesEncrypt(String source){
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			logger.error("Cipher.getInstance(\"AES/CBC/PKCS5Padding\") fail",e);
			throw new RuntimeException("Cipher.getInstance fail");
		}
		
        try {
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);
		} catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
			logger.error("cipher.init fail",e);
			throw new RuntimeException("cipher.init fail");
		}
        byte[] result = null;
        try {
			result = cipher.doFinal(source.getBytes(charset));
		} catch (IllegalBlockSizeException | BadPaddingException
				| UnsupportedEncodingException e) {
			logger.error("cipher.doFinal fail",e);
			throw new RuntimeException("cipher.doFinal fail");
		}
        Encoder encoder = Base64.getEncoder();
		return encoder.encodeToString(result);
	}
	
	private String aesDecrypt(String source){
		Cipher cipher = null;
		try {
			cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			logger.error("Cipher.getInstance(\"AES/CBC/PKCS5Padding\") fail",e);
			throw new RuntimeException("Cipher.getInstance fail");
		}
		
        try {
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);
		} catch (InvalidKeyException | InvalidAlgorithmParameterException e) {
			logger.error("cipher.init fail",e);
			throw new RuntimeException("cipher.init fail");
		}
        
        Decoder decoder = Base64.getDecoder();
        
        byte[] result = null;
        try {
        	result = cipher.doFinal(decoder.decode(source.getBytes()));
		} catch (IllegalBlockSizeException | BadPaddingException e) {
			logger.error("cipher.doFinal fail",e);
			throw new RuntimeException("cipher.doFinal fail");
		}
        try {
			return new String(result,charset);
		} catch (UnsupportedEncodingException e) {
			logger.error("new String fail",e);
			throw new RuntimeException("new String fail");
		}
        
	}

	private void cleanExpiredTicket(){
		long expiredTicketNumber = 0;
		long currentTimeSeconds = System.currentTimeMillis()/1000;
		Iterator<Map.Entry<String,Long>> iter = ticketMap.entrySet().iterator();
		while(iter.hasNext()){
			Map.Entry<String,Long> entry = iter.next();
			if(entry.getValue() - currentTimeSeconds < 0){
				iter.remove();
				expiredTicketNumber++;
			}
		}
		
		logger.info("expiredTicketNumber=[{}]",expiredTicketNumber);
	}
	
	
	public class ExpiredTicketCleanThread extends Thread{
		@Override
		public void run(){
			long begin = System.currentTimeMillis();
			long runTimes = 0;
			while(true){
				runTimes++;
				logger.info("ExpiredTicketCleanThread.runTimes=[{}],runTime=[{}]",runTimes,(System.currentTimeMillis() - begin)/1000);
				try {
					Thread.sleep(10*60*1000L);
				} catch (InterruptedException e) {
					logger.error("ExpiredTicketCleanThread.Thread.sleep(10*60*1000L) InterruptedException",e);
					logger.error("ExpiredTicketCleanThread fail");
					continue;
				}
				
				cleanExpiredTicket();
				
			}
		}
	}
	
	
	
	public static void main(String[] args) {
		TicketServiceImpl impl = new TicketServiceImpl();
		
		String src = "iidsifiadf";
		
		String e = impl.generateTicket(1, src);
		
		String d = impl.checkTicket(e);
		
		System.out.println(e);
		System.out.println(d);
		
	}
	
	
	
	
	
	
	
	
}
