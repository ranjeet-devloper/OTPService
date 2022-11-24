package com.otp.serviceimpl;
import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigInteger;
import java.security.GeneralSecurityException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import com.otp.dto.UserDto;
import com.otp.exception.OtpException;
import com.otp.response.OtpValidationSuccessResponse;
import com.otp.service.OtpService;

@Service
public class TOTP implements OtpService {

	private static final int[] DIGITS_POWER = { 1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000 };
	private static LocalDateTime duration = null;
	private long ids = 0;
	private int counter = 0;
	private String result = "";

	private TOTP() {
	}

	/**
     * This method generates a TOTP value for the given
     * parameters.
     *
     * @param userdto: data transfer object for user.
     * @return: a numeric String in base 10 that includes
     *              {@link truncationDigits} digits
     */
	
	public String generateTOTP256(UserDto userdto) {
		byte[] sec = (userdto.getEmail() + LocalTime.now()).getBytes();
		for (byte b : sec) {
			result = result.concat("" + b);
		}
		return generateTOTP(result, "30", "6", "HmacSHA256");
	}

	 /**
     * This method generates a TOTP value for the given
     * set of parameters.
     *
     * @param key: the shared secret, HEX encoded
     * @param time: a value that reflects a time
     * @param returnDigits: number of digits to return
     * @param crypto: the crypto function to use
     * @return: a numeric String in base 10 that includes
     *              {@link truncationDigits} digits
     */
	public String generateTOTP(String key, String time, String returnDigits, String crypto) {
		int codeDigits = Integer.decode(returnDigits).intValue();

		while (time.length() < 16)
			time = "0".concat(time);
		byte[] msg = hexStr2Bytes(time);
		byte[] k = hexStr2Bytes(key);
		byte[] hash = hmacSHA(crypto, k, msg);
		int offset = hash[hash.length - 1] & 0xf;
		int binary = ((hash[offset] & 0x7f) << 24) | ((hash[offset + 1] & 0xff) << 16)
				| ((hash[offset + 2] & 0xff) << 8) | (hash[offset + 3] & 0xff);
		int otp = binary % DIGITS_POWER[codeDigits];
		result = Integer.toString(otp);
		duration = LocalDateTime.now().plusSeconds(30);
		while (result.length() < codeDigits) {
			result = "0".concat(result);
		}
		return result;
	}

	 /**
     * This method uses the JCE to provide the crypto algorithm.
     * HMAC computes a Hashed Message Authentication Code with the
     * crypto hash algorithm as a parameter.
     * 
     * @param crypto: the crypto algorithm (HmacSHA1, HmacSHA256,
     *                             HmacSHA512)
     * @param keyBytes: the bytes to use for the HMAC key
     * @param text: the message or text to be authenticated
     */
	public byte[] hmacSHA(String crypto, byte[] keyBytes, byte[] text) {
		try {
			Mac hmac;
			hmac = Mac.getInstance(crypto);
			SecretKeySpec macKey = new SecretKeySpec(keyBytes, "RAW");
			hmac.init(macKey);
			return hmac.doFinal(text);
		} catch (GeneralSecurityException gse) {
			throw new UndeclaredThrowableException(gse);
		}
	}
	
	
	/**
     * This method converts a HEX string to Byte[]
     *
     * @param hex the HEX string
     * @return a byte array
     */
	public byte[] hexStr2Bytes(String hex) {
		byte[] bArray = new BigInteger("10" + hex, 16).toByteArray();
		byte[] ret = new byte[bArray.length - 1];
		for (int i = 0; i < ret.length; i++)
			ret[i] = bArray[i + 1];
		return ret;
	}

	/** This method is user for validate the otp. 
	 * which is send by user
	 * 
	 * @param  6 digit otp send by user.
	 * 
	 * @return ResponseEntity object which have OtpValidationSuccessResponse object.
	 */
	@Override
	public ResponseEntity<?> validateOtp(String otp) {

		if (LocalDateTime.now().compareTo(duration) < 0) {
			if (otp.equals(result)) {
				OtpValidationSuccessResponse res = OtpValidationSuccessResponse.builder().id(ids++)
						.traceid(Instant.now().toEpochMilli()).message("Otp Validate Successfully").statuscode(200l)
						.build();
				return new ResponseEntity<>(res, HttpStatus.ACCEPTED);
			} else {
				throw new OtpException("Otp Not Validate");
			}

		} else {

			return new ResponseEntity<>("otp expire please resend...", HttpStatus.GATEWAY_TIMEOUT);
		}

	}

	/** Purpose of this method is re-send the otp.
	 * 
	 * @return  this function return otp to the user or if they cross the no. of limit 
	 * then this function return a string with suitable message.
	 */
	
	@Override
	public String resendOtp() {
		if (counter == -1) {
			return "you crossed maximum no. of limits please try again after 30 min.";
		} else if (counter < 5) {
			counter++;
			return generateTOTP(result, "30", "6", "HmacSHA256");
		}

		else {
			counter = -1;
			return "you crossed maximum no. of limits please try again after 30 min.";
		}

	}
}
