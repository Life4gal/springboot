package com.springboot.eft.utils.decryption;

import com.springboot.eft.utils.encryption.JavaEncrypt;

import java.util.Base64;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class JavaDecrypt {

	private JavaDecrypt() {
	}

	/**
	 * base64解密
	 *
	 * @param code {@link String}
	 * @return {@link String}
	 */
	public static String base64(String code) {
		return new String(Base64.getDecoder().decode(code));
	}

	/**
	 * des解密
	 *
	 * @param code {@link String}
	 * @return {@link String}
	 * @throws InvalidKeyException          异常
	 * @throws NoSuchAlgorithmException     异常
	 * @throws NoSuchPaddingException       异常
	 * @throws UnsupportedEncodingException 异常
	 * @throws IllegalBlockSizeException    异常
	 * @throws BadPaddingException          异常
	 */
	public static String des(String code) throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
		return JavaEncrypt.decryptDES(code, "DES");
	}

	/**
	 * des3解密
	 *
	 * @param code {@link String}
	 * @return {@link String}
	 * @throws InvalidKeyException          异常
	 * @throws NoSuchAlgorithmException     异常
	 * @throws NoSuchPaddingException       异常
	 * @throws UnsupportedEncodingException 异常
	 * @throws IllegalBlockSizeException    异常
	 * @throws BadPaddingException          异常
	 */
	public static String des3(String code) throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
		return JavaEncrypt.decryptDES(code, "DESede");
	}

	/**
	 * aes解密
	 *
	 * @param code {@link String}
	 * @return {@link String}
	 * @throws InvalidKeyException          异常
	 * @throws NoSuchAlgorithmException     异常
	 * @throws NoSuchPaddingException       异常
	 * @throws UnsupportedEncodingException 异常
	 * @throws IllegalBlockSizeException    异常
	 * @throws BadPaddingException          异常
	 */
	public static String aes(String code) throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
		return JavaEncrypt.decryptDES(code, "AES");
	}

	/**
	 * rsa解密
	 *
	 * @param code {@link String}
	 * @return {@link String}
	 * @throws InvalidKeyException          异常
	 * @throws NoSuchAlgorithmException     异常
	 * @throws NoSuchPaddingException       异常
	 * @throws UnsupportedEncodingException 异常
	 * @throws IllegalBlockSizeException    异常
	 * @throws BadPaddingException          异常
	 */
	public static String rsa(String code) throws InvalidKeyException, NoSuchAlgorithmException,
			NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException {
		return JavaEncrypt.decryptDES(code, "RSA");
	}
}
