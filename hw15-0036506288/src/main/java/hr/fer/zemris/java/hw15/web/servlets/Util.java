package hr.fer.zemris.java.hw15.web.servlets;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * This class provides <code>public static</code> methods for working with
 * passwords in this web application. Since we cannot store pure passwords in plain text,
 * because that would be a security vulnerability, we need methods that can digest a
 * password using SHA-1 algorithm and a method that hex-encodes that string for extra protection.
 * 
 * @author Ivan Skorupan
 */
public class Util {
	
	/**
	 * Hexadecimal number system base.
	 */
	private static final int HEX_BASE = 16;
	
	/**
	 * A hex mask for keeping only the lower byte of an integer.
	 */
	private static final int BYTE_MASK = 0xFF;
	
	/**
	 * A hex mask for keeping only the lower nibble of an integer.
	 */
	private static final int NIBBLE_MASK = 0x0F;
	
	/**
	 * Digests the given file and returns the calculated digest as an array of bytes.
	 * 
	 * @param inputFilePath - path of the input file
	 * @return a byte array containing the calculated digest
	 * @throws NoSuchAlgorithmException if {@link MessageDigest#getInstance(String) getInstance()} method gets an unsupported algorithm string
	 * @throws IOException if there was an error reading the input file
	 */
	public static byte[] digestString(String text) {
		MessageDigest digest = null;
		try {
			digest = MessageDigest.getInstance("SHA-1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		
		byte[] textBytes = text.getBytes();
		digest.update(textBytes, 0, textBytes.length);

		return digest.digest();
	}
	
	/**
	 * Converts a given <code>byte</code> array into a hex-encoded
	 * string.
	 * <p>
	 * The resulting hex-encoded string uses lowercase letters a-f for
	 * representing hex digits.
	 * <p>
	 * For zero-length array an empty string is returned. 
	 * 
	 * @param bytes - an array of bytes to be hex-encoded
	 * @return an appropriate hex-encoded string for given <code>byte</code> array
	 */
	public static String byteToHex(byte[] bytes) {
		StringBuilder hexString = new StringBuilder();
		for(int i = 0; i < bytes.length; i++) {
			int maskedByte = bytes[i] & BYTE_MASK;
			hexString.append(Character.forDigit(maskedByte >>> 4, HEX_BASE));
			hexString.append(Character.forDigit(maskedByte & NIBBLE_MASK, HEX_BASE));
		}
		
		return hexString.toString();
	}
	
}
