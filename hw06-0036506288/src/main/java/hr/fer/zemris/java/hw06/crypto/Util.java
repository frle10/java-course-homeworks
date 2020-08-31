package hr.fer.zemris.java.hw06.crypto;

import java.util.Objects;

/**
 * A utility class that provides two public static methods for converting
 * a hex-encoded string to a <code>byte</code> array and vice versa.
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
	 * Converts a given hex-encoded string to a <code>byte</code> array.
	 * <p>
	 * The method supports both uppercase and lowercase letters in
	 * the string (A-F and a-f) for representing hexadecimal digits.
	 * <p>
	 * The string must be even sized and can contain only valid hexadecimal
	 * digits (0-9, a-f, A-F), otherwise a {@link IllegalArgumentException}
	 * is thrown.
	 * <p>
	 * For zero-length string, the method returns a zero-length <code>byte</code>
	 * array.
	 * 
	 * @param hex - a hex-encoded string
	 * @return an appropriate <code>byte</code> array
	 * @throws IllegalArgumentException if <code>hex</code> is of odd length or if it contains invalid hex digits
	 * @throws NullPointerException if <code>hey</code> is <code>null</code>
	 */
	public static byte[] hexToByte(String hex) {
		Objects.requireNonNull(hex);
		
		if(hex.length() % 2 != 0 || !isValidHexString(hex.toLowerCase())) {
			throw new IllegalArgumentException("The given string is either odd sized or contains an invalid hex character!");
		}
		
		byte[] bytes = new byte[hex.length() / 2];
		for(int i = 0; i < hex.length(); i += 2) {
			bytes[i / 2] = (byte) ((Character.digit(hex.charAt(i), HEX_BASE) << 4) + Character.digit(hex.charAt(i + 1), HEX_BASE));
		}
		
		return bytes;
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
	
	/**
	 * Checks if the given string contains only valid hex digits.
	 * 
	 * @param hex - a hex-encoded string to check
	 * @return <code>true</code> if <code>hex</code> is a valid hex-encoded string, <code>false</code> otherwise
	 * @throws NullPointerException if <code>hex</code> is <code>null</code>
	 */
	private static boolean isValidHexString(String hex) {
		Objects.requireNonNull(hex);
		
		char[] hexChars = hex.toCharArray();
		for(char character : hexChars) {
			if(!Character.isDigit(character) && !(character >= 'a' && character <= 'f')) {
				return false;
			}
		}
		
		return true;
	}
	
}
