package hr.fer.zemris.java.webserver.workers;

import java.util.Map;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * This web worker checks if parameter "bgcolor" was received and if it is a valid
 * hex-encoded color (a string containing 6 hex-digits).
 * <p>
 * If it is, then the received value is stored in persistent parameters map under
 * name "bgcolor" and an HTML document is generated with message that the color
 * was updated.
 * <p>
 * Otherwise, the parameter is ignored and an HTML focument is generated saying
 * that the color was <b>not</b> updated.
 * <p>
 * No matter the case, the generated HTML document will contain a link to
 * "/index2.html".
 * 
 * @author Ivan Skorupan
 */
public class BgColorWorker implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		Map<String, String> parameters = context.getParameters();
		String bgColor = parameters.get("bgcolor");
		
		StringBuilder sb = new StringBuilder(
				"<html>" +
				"<head><title>Color updated?</title></head>" +
				"<body>The color was "
		);
		
		if(isValidHexString(bgColor)) {
			context.setPersistentParameter("bgcolor", bgColor);
			sb.append("updated!");
		} else {
			sb.append("NOT updated!");
		}
		
		sb.append(
				"<p><a href=\"/index2.html\">index2.html</a></p>" +
				"</body></html>"
		);
		
		context.write(sb.toString());
	}
	
	/**
	 * Tests if the given string <code>hex</code> is a valid hex string.
	 * <p>
	 * A string is a vlaid hex string if and only if each of its characters
	 * is either a digit 0-9 or one of letters A, B, C, D, E, F
	 * (case does not matter).
	 * 
	 * @param hex - string to be tested
	 * @return <code>true</code> if <code>hex</code> is a valid hex string, <code>false</code> otherwise
	 */
	private boolean isValidHexString(String hex) {
		if(hex == null || hex.length() != 6) {
			return false;
		}
		
		for(int i = 0; i < hex.length(); i++) {
			char hexDigit = Character.toUpperCase(hex.charAt(i));
			if(!Character.isDigit(hexDigit) && !(hexDigit >= 'A' && hexDigit <= 'F')) {
				return false;
			}
		}
		
		return true;
	}
	
}
