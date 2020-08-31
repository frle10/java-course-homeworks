package hr.fer.zemris.java.webserver.workers;

import java.util.Map;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * This worker checks if there is a persistent parameter of name "bgcolor"
 * set and if so, it sets a temporary parameter of name "background" with fetched
 * value in the given request context.
 * <p>
 * If there is no existant "bgcolor" persistent parameter, the default color is set and
 * that's "7F7F7F" (grey).
 * <p>
 * In the end, this web worker delegates additional work to script "home.smscr".
 * 
 * @author Ivan Skorupan
 */
public class Home implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		Map<String, String> persistentParameters = context.getPersistentParameters();
		String bgColor = persistentParameters.get("bgcolor");
		
		context.setTemporaryParameter("background", (bgColor == null) ? "7F7F7F" : bgColor);
		context.getDispatcher().dispatchRequest("private/pages/home.smscr");
	}
	
}
