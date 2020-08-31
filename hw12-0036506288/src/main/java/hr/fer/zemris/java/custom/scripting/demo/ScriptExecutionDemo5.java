package hr.fer.zemris.java.custom.scripting.demo;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * This is the fifth demo of SmartScript execution using the
 * {@link SmartScriptEngine}.
 * <p>
 * The script executed is "fibonaccih.smscr".
 * 
 * @author Ivan Skorupan
 */
public class ScriptExecutionDemo5 {

	/**
	 * Entry point of this demo program.
	 * 
	 * @param args - command line arguments (not used)
	 */
	public static void main(String[] args) {
		String documentBody = null;
		try {
			documentBody = Files.readString(Paths.get("webroot/scripts/fibonaccih.smscr"));
		} catch (IOException e) {
			System.out.println("There was a problem while reading the file from disk!");
			return;
		}

		Map<String,String> parameters = new HashMap<String, String>();
		Map<String,String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		
		OutputStream os = null;
		try {
			os = Files.newOutputStream(Paths.get("fibonaccihHTML.html"));
		} catch (IOException e) {
			
		}

		// create engine and execute it
		new SmartScriptEngine(
				new SmartScriptParser(documentBody).getDocumentNode(),
				new RequestContext(os, parameters, persistentParameters, cookies, null)
		).execute();
	}

}
