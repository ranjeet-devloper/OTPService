package com.otp.serviceimpl;
import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.stereotype.Service;

import com.otp.service.OtpService;

//package com.otp.services;
//
//
//import java.util.Random;
//import java.util.concurrent.TimeUnit;
//
//import org.springframework.stereotype.Service;
//
//import com.google.common.cache.CacheBuilder;
//import com.google.common.cache.CacheLoader;
//import com.google.common.cache.LoadingCache;
//
//@Service
//public class OTPServices {
//   
//	private static final Integer expmin=3;
//	Random random=new Random();
//	private LoadingCache<String, Integer> otpCache;
//	public OTPServices() {
//		super();
//		this.otpCache = CacheBuilder.newBuilder().expireAfterWrite(expmin, TimeUnit.MINUTES)
//				.build(new CacheLoader<String, Integer>(){
//					public Integer load(String key)
//					{
//						return 0;
//					}
//				});
//	}
//	
//	public int generateOTP(String key)
//	{
//		int otp=100000+random.nextInt(900000);
//		otpCache.put(key, otp);
//		return otp;
//	}
//	
//	public int getOTP(String key)
//	{
//		try {
//		return	otpCache.get(key);
//		} catch (Exception e) {
//			return 0;
//		}
//	}
//	public void clearOTP(String key)
//	{
//		otpCache.invalidate(key);
//	}
//	
//}
@Service
public class TOTP implements OtpService
{
	
	private static final int[] DIGITS_POWER
    = {1,10,100,1000,10000,100000,1000000,10000000,100000000 };
	private TOTP() {}

	
	public String generateTOTP256(String key,
            String time,
            String returnDigits){
        return generateTOTP(key, time, returnDigits, "HmacSHA256");
    }
	
	
    
    public String generateTOTP(String key,
            String time,
            String returnDigits,
            String crypto){
        int codeDigits = Integer.decode(returnDigits).intValue();
        String result = null;
        while (time.length() < 16 )
            time = "0".concat(time);
        byte[] msg = hexStr2Bytes(time);
        byte[] k = hexStr2Bytes(key);
        byte[] hash = hmacSHA(crypto, k, msg);
        int offset = hash[hash.length - 1] & 0xf;
        int binary =
            ((hash[offset] & 0x7f) << 24) |
            ((hash[offset + 1] & 0xff) << 16) |
            ((hash[offset + 2] & 0xff) << 8) |
            (hash[offset + 3] & 0xff);
        int otp = binary % DIGITS_POWER[codeDigits];
        result = Integer.toString(otp);
        while (result.length() < codeDigits) {
            result="0".concat(result);
        }
        return result;
    }
    public byte[] hmacSHA(String crypto, byte[] keyBytes,
            byte[] text){
        try {
            Mac hmac;
            hmac = Mac.getInstance(crypto);
            SecretKeySpec macKey =
                new SecretKeySpec(keyBytes, "RAW");
            hmac.init(macKey);
            return hmac.doFinal(text);
        } catch (GeneralSecurityException gse) {
            throw new UndeclaredThrowableException(gse);
        }
    }
    
    public byte[] hexStr2Bytes(String hex){
        byte[] bArray = new BigInteger("10" + hex,16).toByteArray();
        byte[] ret = new byte[bArray.length - 1];
        for (int i = 0; i < ret.length; i++)
            ret[i] = bArray[i+1];
        return ret;
    }
    

}
