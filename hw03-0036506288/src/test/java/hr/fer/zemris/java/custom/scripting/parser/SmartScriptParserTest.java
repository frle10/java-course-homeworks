package hr.fer.zemris.java.custom.scripting.parser;

import static org.junit.jupiter.api.Assertions.*;
import static hr.fer.zemris.java.hw03.SmartScriptTester.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

import hr.fer.zemris.java.custom.scripting.nodes.DocumentNode;

class SmartScriptParserTest {
	
	@Test
	public void testParserOnNullDocument() {
		assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(null));
	}
	
	@Test
	public void testParserOnUnsupportedTag() {
		String docBody = "{$ CD $} {$ END $}";
		
		assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
	}
	
	@Test
	public void testParserOnInvalidEscapeSequenceInBody() {
		String docBody = "Malo običnog teksta pa onda"
				+ " neki razmak i sad \\n kao novi red i neki tab \\t.";
		
		assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
	}
	
	@Test
	public void testParserOnInvalidEscapeSequenceInTag() {
		String docBody = "Imamo tag {$= i osam \"neki \\{ tekst\" pi $}";
		
		assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
	}
	
	@Test
	public void testParserOnDocument1() {
		String docBody = loader("testDocument1.txt");
		SmartScriptParser parser = new SmartScriptParser(docBody);
		DocumentNode document = parser.getDocumentNode();
		String originalDocumentBody = createOriginalDocumentBody(document);
		SmartScriptParser parser2 = new SmartScriptParser(originalDocumentBody);
		DocumentNode document2 = parser2.getDocumentNode();
		// now document and document2 should be structurally identical trees
		
		assertEquals(document, document2);
	}
	
	@Test
	public void testParserOnDocument2() {
		String docBody = loader("testDocument2.txt");
		SmartScriptParser parser = new SmartScriptParser(docBody);
		DocumentNode document = parser.getDocumentNode();
		String originalDocumentBody = createOriginalDocumentBody(document);
		SmartScriptParser parser2 = new SmartScriptParser(originalDocumentBody);
		DocumentNode document2 = parser2.getDocumentNode();
		// now document and document2 should be structurally identical trees
		
		assertEquals(document, document2);
	}
	
	@Test
	public void testParserOnDocument3() {
		String docBody = loader("testDocument3.txt");
		SmartScriptParser parser = new SmartScriptParser(docBody);
		DocumentNode document = parser.getDocumentNode();
		String originalDocumentBody = createOriginalDocumentBody(document);
		SmartScriptParser parser2 = new SmartScriptParser(originalDocumentBody);
		DocumentNode document2 = parser2.getDocumentNode();
		// now document and document2 should be structurally identical trees
		
		assertEquals(document, document2);
	}
	
	@Test
	public void testParserOnDocument4() {
		String docBody = loader("testDocument4.txt");
		
		assertThrows(SmartScriptParserException.class, () -> new SmartScriptParser(docBody));
	}
	
	private String loader(String filename) {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try(InputStream is = this.getClass().getClassLoader().getResourceAsStream(filename)) {
			byte[] buffer = new byte[1024];
			while(true) {
				int read = is.read(buffer);
				if(read<1) break;
				bos.write(buffer, 0, read);
			}
			return new String(bos.toByteArray(), StandardCharsets.UTF_8);
		} catch(IOException ex) {
			return null;
		}
	}
	
}
