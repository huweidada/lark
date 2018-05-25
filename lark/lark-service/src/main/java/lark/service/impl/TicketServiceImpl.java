package lark.service.impl;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

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
	
	private String charset = "UTF-8";
	
	@Override
	public String generateTicket(int authType, String userId,long ttl) {
		logger.info("authType=[{}],userId=[{}],ttl=[{}]",authType,userId,ttl);
		
		if(StringUtils.isBlank(userId)){
			logger.error("StringUtils.isBlank(userId) == true");
			throw new RuntimeException("StringUtils.isBlank(userId) == true");
		}
		if(ttl < 0){
			logger.error("ttl[{}] < 0",ttl);
			throw new RuntimeException("ttl < 0");
		}
		
		long random = (long) (Math.random() * 99982507);
		//设计ticket的格式，随机数+"###"+authType+"###"+userId，这里需要和userId生成的地方约定
		//userId不能包含"###"
		String source = String.valueOf(random) + "###" + String.valueOf(authType) + "###" + userId;
		
		return aesEncrypt(source);
		
	}

	@Override
	public String checkTicket(String ticket) {
		return aesDecrypt(ticket);
	}

	@Override
	public String checkTicketAndResetTll(String ticket, long ttl) {
		return null;
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

	
	public static void main(String[] args) {
		TicketServiceImpl impl = new TicketServiceImpl();
		
		String src = "huwei33";
		
		String e = impl.generateTicket(1, src, 100);
		
		String d = impl.checkTicket(e);
		
		System.out.println(e);
		System.out.println(d);
		
	}
	
	
	
	
	
	
	
	
}
