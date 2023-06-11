package com.clayfin.util;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {

	private static final String ALGORITHM = "AES";
	/*
	 * public static void main(String[] args) throws Exception {
	 * 
	 * String plainText = "MadhuKinnera"; String key = "abcdefghijklmnop"; String
	 * encryptedText = encrypt(plainText, key); System.out.println("Encrypted " +
	 * encryptedText); String decryptedText = decrypt(encryptedText, key);
	 * System.out.println("Decrypted " + decryptedText);
	 * 
	 * }
	 */

	public static String encrypt(String plainText, String key) throws Exception {

		Cipher cipher = Cipher.getInstance(ALGORITHM);

		SecretKey secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);

		cipher.init(Cipher.ENCRYPT_MODE, secretKey);

		byte[] encryptedText = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
		return Base64.getEncoder().encodeToString(encryptedText);
	}

	public static String decrypt(String encryptedText, String key) throws Exception {

		Cipher cipher = Cipher.getInstance(ALGORITHM);

		SecretKey secretKey = new SecretKeySpec(key.getBytes(), ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, secretKey);

		byte[] encryptedBytes = Base64.getDecoder().decode(encryptedText.getBytes());

		byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

		return new String(decryptedBytes, StandardCharsets.UTF_8);

	}

}
