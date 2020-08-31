package hr.fer.zemris.java.hw06.crypto;

import static org.junit.jupiter.api.Assertions.*;
import static hr.fer.zemris.java.hw06.crypto.Util.*;

import org.junit.jupiter.api.Test;

class UtilTest {

	@Test
	void testHexToByte() {
		byte[] expected = new byte[] {1, -82, 34};
		assertArrayEquals(expected, hexToByte("01aE22"));
		
		expected = new byte[] {0, 0, 0};
		assertArrayEquals(expected, hexToByte("000000"));
		
		expected = new byte[] {1, 2, 3};
		assertArrayEquals(expected, hexToByte("010203"));
		
		expected = new byte[] {-1, -1, -1, -1};
		assertArrayEquals(expected, hexToByte("ffffffff"));
		
		assertThrows(NullPointerException.class, () -> hexToByte(null));
	}

	@Test
	void testByteToHex() {
		String expected = "01ae22";
		assertEquals(expected, byteToHex(new byte[] {1, -82, 34}));
		
		expected = "000000";
		assertEquals(expected, byteToHex(new byte[] {0, 0, 0}));
		
		expected = "010203";
		assertEquals(expected, byteToHex(new byte[] {1, 2, 3}));
		
		expected = "ffffffff";
		assertEquals(expected, byteToHex(new byte[] {-1, -1, -1, -1}));
	}

}
