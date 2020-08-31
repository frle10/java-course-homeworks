package hr.fer.zemris.java.custom.scripting.demo;

import java.io.IOException;
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
 * This is the third demo of SmartScript execution using the
 * {@link SmartScriptEngine}.
 * <p>
 * The script executed is "brojPoziva.smscr".
 * 
 * @author Ivan Skorupan
 */
public class ScriptExecutionDemo3 {

	/**
	 * Entry point of this demo program.
	 * 
	 * @param args - command line arguments (not used)
	 */
	public static void main(String[] args) {
		String documentBody = null;
		try {
			documentBody = Files.readString(Paths.get("webroot/scripts/brojPoziva.smscr"));
		} catch (IOException e) {
			System.out.println("There was a problem while reading the file from disk!");
			return;
		}

		Map<String,String> parameters = new HashMap<String, String>();
		Map<String,String> persistentParameters = new HashMap<String, String>();
		List<RCCookie> cookies = new ArrayList<RequestContext.RCCookie>();
		persistentParameters.put("brojPoziva", "3");
		RequestContext rc = new RequestContext(System.out, parameters, persistentParameters,
				cookies, null);
		new SmartScriptEngine(
				new SmartScriptParser(documentBody).getDocumentNode(), rc
		).execute();
		System.out.println("Vrijednost u mapi: " + rc.getPersistentParameter("brojPoziva"));

	}

}
