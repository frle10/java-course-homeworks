package hr.fer.zemris.java.webserver;

/**
 * Models objects that know how to process a client request
 * based on the URL path the user entered in their browser.
 * 
 * @author Ivan Skorupan
 */
public interface IDispatcher {
	
	/**
	 * This method should decide what to do with a client request
	 * based on the given <code>urlPath</code>.
	 * <p>
	 * If the given path is path to a SmartScript, then we should
	 * execute the script.
	 * <p>
	 * If it is a path to some worker, then the worker's appropriate
	 * execution method should be called.
	 * <p>
	 * Otherwise, the path probably points to a file on the server and
	 * therefore it should be rendered in the browser based on the
	 * request's mime type.
	 * 
	 * @param urlPath
	 * @throws Exception
	 */
	void dispatchRequest(String urlPath) throws Exception;
	
}
