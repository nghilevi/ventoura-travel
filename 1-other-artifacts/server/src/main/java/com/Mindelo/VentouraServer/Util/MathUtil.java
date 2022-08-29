package com.Mindelo.VentouraServer.Util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MathUtil {
	/**
	 * @param plaintext
	 *            a string need to be encrypted
	 * @return the encrypted text
	 * @throws NoSuchAlgorithmException
	 */
	public static String Md5(String plaintext) throws NoSuchAlgorithmException {

		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(plaintext.getBytes());

		byte byteData[] = md.digest();

		// convert the byte to hex format method 1
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < byteData.length; i++) {
			sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16)
					.substring(1));
		}
		return sb.toString();
	}
}
