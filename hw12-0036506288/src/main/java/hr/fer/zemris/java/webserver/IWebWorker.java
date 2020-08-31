package hr.fer.zemris.java.webserver;

/**
 * Models objects that know how to process a client request.
 * <p>
 * A web worker gets the current request and is expected to create content
 * for the client.
 * 
 * @author Ivan Skorupan
 */
public interface IWebWorker {
	
	/**
	 * Processes the current user request.
	 * 
	 * @param context - request context object that will be used to write generated content to client
	 * @throws Exception if any kind of problem arises while making content for the client
	 */
	public void processRequest(RequestContext context) throws Exception;
	
}
