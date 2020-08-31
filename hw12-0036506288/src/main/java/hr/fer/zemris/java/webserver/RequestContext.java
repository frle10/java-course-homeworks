package hr.fer.zemris.java.webserver;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Models an HTTP request context. Typical HTTP request contains a
 * header which has information about encoding, status code, HTTP version,
 * mime type etc.
 * <p>
 * Additionally, we support cookies in our requests.
 * 
 * @author Ivan Skorupan
 */
public class RequestContext {

	/**
	 * Output stream through which request content is written.
	 */
	private OutputStream outputStream;

	/**
	 * Charset used for this request's content.
	 */
	private Charset charset;
	
	/**
	 * A dispatcher used for this request.
	 */
	private IDispatcher dispatcher;
	
	/**
	 * Encoding of this request.
	 */
	private String encoding = "UTF-8";

	/**
	 * Status code of this request.
	 */
	private int statusCode = 200;

	/**
	 * Status text of this request.
	 */
	private String statusText = "OK";

	/**
	 * Mime type of this request.
	 */
	private String mimeType = "text/html";

	/**
	 * Length of this request's content.
	 */
	private Long contentLength;
	
	/**
	 * Session id of this request context.
	 */
	private String sid;

	/**
	 * Map that contains this request's parameters, gotten through the URL.
	 */
	private Map<String, String> parameters;

	/**
	 * Map that contains this request's temporary parameters, these are used
	 * by SmartScripts to store some results.
	 */
	private Map<String, String> temporaryParameters;

	/**
	 * Map that contains this request's persistent parameters, those being
	 * used to store certain preferences set by the client and are kept
	 * in between sessions unlike normal or temporary parameters.
	 */
	private Map<String, String> persistentParameters;

	/**
	 * List of this request's cookies that will be sent to the client.
	 */
	private List<RCCookie> outputCookies;

	/**
	 * Flag that marks if the request header was generated or not.
	 */
	private boolean headerGenerated;
	
	/**
	 * Constructs a new {@link RequestContext} object.
	 * 
	 * @param outputStream - this request context's output stream to write data through
	 * @param parameters - map of this request's parameters (usually parsed from URL)
	 * @param persistentParameters - map of this request's persistent parameters
	 * @param outputCookies - list of this request's cookies
	 * @see #RequestContext(OutputStream, Map, Map, List, Map, IDispatcher, String)
	 */
	public RequestContext(OutputStream outputStream, Map<String,String> parameters,
			Map<String,String> persistentParameters, List<RCCookie> outputCookies, String sid) {
		this(outputStream, parameters, persistentParameters, outputCookies, null, null, sid);
	}
	
	/**
	 * Constructs a new {@link RequestContext} object.
	 * <p>
	 * This object needs to have <code>outputStream</code> different
	 * from <code>null</code> as otherwise it wouldn't be able to
	 * write data anywhere.
	 * <p>
	 * Other arguments are collections and they can be <code>null</code>,
	 * in which case empty collections will be initialized.
	 * 
	 * @param outputStream - this request context's output stream to write data through
	 * @param parameters - map of this request's parameters (usually parsed from URL)
	 * @param persistentParameters - map of this request's persistent parameters
	 * @param outputCookies - list of this request's cookies
	 * @param temporaryParameters - map of this request's temporary parameters
	 * @param dispatcher - 
	 * @throws NullPointerException if <code>outputStream</code> is <code>null</code>
	 */
	public RequestContext(OutputStream outputStream, Map<String,String> parameters,
			Map<String,String> persistentParameters, List<RCCookie> outputCookies,
			Map<String, String> temporaryParameters, IDispatcher dispatcher, String sid) {
		this.outputStream = Objects.requireNonNull(outputStream);
		this.parameters = (parameters == null) ? new HashMap<>() : parameters;
		this.persistentParameters = (persistentParameters == null) ? new HashMap<>() : persistentParameters;
		this.outputCookies = (outputCookies == null) ? new ArrayList<>() : outputCookies;
		this.temporaryParameters = (temporaryParameters == null) ? new HashMap<>() : temporaryParameters;
		this.dispatcher = dispatcher;
		this.sid = sid;
	}
	
	/**
	 * Takes a collections of {@link String} objects and returns
	 * an unmodifiable set of its elements.
	 * 
	 * @param collection - collections to turn into an unmodifiable set
	 * @return an unmodifiable set of values contained in <code>collection</code>
	 */
	private Set<String> createUnmodifiableSetFromCollection(Collection<String> collection) {
		return collection.stream()
				.collect(Collectors.toUnmodifiableSet());
	}
	
	/**
	 * Adds a cookie to this request's list of output cookies.
	 * <p>
	 * If the request header was already generated, this method will
	 * throw an exception because it makes no sense to add additional
	 * cookies after the header was already configured.
	 * 
	 * @param cookie - cookie to be added to this request context
	 * @throws RuntimeException if the request header was already generated
	 * upon this method call
	 */
	public void addRCCookie(RCCookie cookie) {
		checkHeaderGenerated();
		outputCookies.add(Objects.requireNonNull(cookie));
	}
	
	/**
	 * Gets the {@link IDispatcher} object associated with this
	 * request context.
	 * 
	 * @return {@link IDispatcher} object associated with this request context
	 */
	public IDispatcher getDispatcher() {
		return dispatcher;
	}

	/**
	 * Getter for this request context's charset.
	 * 
	 * @return charset of this request context
	 */
	public Charset getCharset() {
		return charset;
	}
	
	/**
	 * Gets the parameter <code>name</code>'s value from
	 * internal parameters map.
	 * 
	 * @param name - name of the parameter to fetch
	 * @return value of parameter <code>name</code>
	 */
	public String getParameter(String name) {
		return parameters.get(name);
	}
	
	/**
	 * Gets a set of all parameter names contained in this request.
	 * 
	 * @return set of all parameter names existant in this request
	 */
	public Set<String> getParameterNames() {
		return createUnmodifiableSetFromCollection(parameters.values());
	}
	
	/**
	 * Gets the persistent parameter <code>name</code>'s value from
	 * internal persistent parameters map.
	 * 
	 * @param name - name of the persistent parameter to fetch
	 * @return value of persistent parameter <code>name</code>
	 */
	public String getPersistentParameter(String name) {
		return persistentParameters.get(name);
	}
	
	/**
	 * Gets all persistent parameter names that exist in this request.
	 * 
	 * @return set of all persistent parameter names
	 */
	public Set<String> getPersistentParameterNames() {
		return createUnmodifiableSetFromCollection(persistentParameters.values());
	}
	
	/**
	 * Puts a new entry into internal persistent parameters map using
	 * <code>name</code> as key and <code>value</code> as value.
	 * 
	 * @param name - persistent parameter name
	 * @param value - persistent parameter value
	 */
	public void setPersistentParameter(String name, String value) {
		persistentParameters.put(name, value);
	}
	
	/**
	 * Removes a persistent parameter with name <code>name</code> from
	 * internal persistent parameters map.
	 * 
	 * @param name - name of persistent parameter to remove
	 */
	public void removePersistentParameter(String name) {
		persistentParameters.remove(name);
	}
	
	/**
	 * Gets the temporary parameter <code>name</code>'s value from
	 * internal temporary parameters map.
	 * 
	 * @param name - name of the temporary parameter to fetch
	 * @return value of temporary parameter <code>name</code>
	 */
	public String getTemporaryParameter(String name) {
		return temporaryParameters.get(name);
	}
	
	/**
	 * Gets all temporary parameter names that exist in
	 * this request.
	 * 
	 * @return set of all temporary parameter names in this request context
	 */
	public Set<String> getTemporaryParameterNames() {
		return createUnmodifiableSetFromCollection(temporaryParameters.values());
	}
	
	/**
	 * Returns the session ID this request corresponds to.
	 * 
	 * @return corresponding session ID for this request
	 */
	public String getSessionID() {
		return sid;
	}
	
	/**
	 * Puts a new entry into internal temporary parameters map using
	 * <code>name</code> as key and <code>value</code> as value.
	 * 
	 * @param name - temporary parameter name
	 * @param value - temporary parameter value
	 */
	public void setTemporaryParameter(String name, String value) {
		temporaryParameters.put(name, value);
	}
	
	/**
	 * Removes a temporary parameter with name <code>name</code> from
	 * internal temporary parameters map.
	 * 
	 * @param name - name of temporary parameter to remove
	 */
	public void removeTemporaryParameter(String name) {
		temporaryParameters.remove(name);
	}
	
	/**
	 * This method writes the given <code>data</code> to the
	 * output stream.
	 * <p>
	 * It uses method {@link #write(byte[], int, int)} with
	 * arguments (data, 0, data.length) to minimize code
	 * duplication.
	 * <p>
	 * For extra information on how the write methods work,
	 * see {@link #write(byte[], int, int)}.
	 * 
	 * @param data - bytes to be written to the output stream
	 * @return the {@link RequestContext} object upon which this method was called
	 * @throws IOException if there is an error while writing the data to output stream
	 * @see #write(byte[], int, int)
	 */
	public RequestContext write(byte[] data) throws IOException {
		return write(data, 0, data.length);
	}
	
	/**
	 * Writes data to the output stream of this {@link RequestContext}.
	 * <p>
	 * The data that needs to be written is defined by the array of bytes
	 * <code>data</code>, <code>offset</code> and <code>len</code>.
	 * <p>
	 * This method takes into account the flag that marks if request header
	 * was generated or not. In case it was generated, the method simply
	 * writes the given data to the output stream.
	 * <p>
	 * Otherwise, the method will generate an appropriate header, set the flag
	 * to <code>true</code>, update the {@link #charset} field and then write
	 * the header along with the data to the output stream.
	 * <p>
	 * Header generation happens only on the first call of this method. Every
	 * next call simply writes the given data to the output stream.
	 * 
	 * @param data - bytes to be written to the output stream
	 * @param offset - index in <code>data</code> at which to start writing
	 * @param len - number of bytes to write to the output stream
	 * @return the {@link RequestContext} object upon which this method was called
	 * @throws IOException if there is an error while writing the data to output stream
	 */
	public RequestContext write(byte[] data, int offset, int len) throws IOException {
		if(!headerGenerated) {
			String header = generateHeader();
			byte[] headerBytes = header.getBytes();
			outputStream.write(headerBytes);
			charset = Charset.forName(encoding);
			headerGenerated = true;
		}
		
		outputStream.write(data, offset, len);
		return this;
	}
	
	/**
	 * Converts the given <code>text</code> to bytes and then
	 * calls the {@link #write(byte[], int, int)} method with
	 * parameters (textBytes, 0, textBytes.length) in order to
	 * minimize code duplication.
	 * <p>
	 * For extra information on how the write methods work,
	 * see {@link #write(byte[], int, int)}.
	 * 
	 * @param text - text data to be written to the output stream
	 * @return the {@link RequestContext} object upon which this method was called
	 * @throws IOException if there is an error while writing the data to output stream
	 * @see #write(byte[], int, int)
	 */
	public RequestContext write(String text) throws IOException {
		byte[] textBytes = text.getBytes(encoding);
		return write(textBytes, 0, textBytes.length);
	}
	
	/**
	 * This is a helper method that generates an appropriate header for this request
	 * if any of the {@link #write(byte[], int, int) write()} methods was called for
	 * the first time.
	 * 
	 * @return an appropriate header for this request as a single {@link String} object
	 */
	private String generateHeader() {
		StringBuilder header = new StringBuilder();
		header.append("HTTP/1.1 " + statusCode + " " + statusText + "\r\n"
			+ "Content-Type: " + mimeType + (mimeType.startsWith("text/") ? "; charset=" + encoding : "") + "\r\n"
			+ (contentLength == null ? "" : "Content-Length: " + contentLength + "\r\n")
		);
		
		if(!outputCookies.isEmpty()) {
			for(RCCookie cookie : outputCookies) {
				header.append("Set-Cookie: " + cookie.name + "=\"" + cookie.value + "\"");
				header.append(cookie.domain != null ? "; Domain=" + cookie.domain : "");
				header.append(cookie.path != null ? "; Path=" + cookie.path : "");
				header.append(cookie.maxAge != null ? "; Max-Age=" + cookie.maxAge : "");
				header.append(cookie.httpOnly == true ? "; HttpOnly" : "");
				header.append("\r\n");
			}
		}
		
		header.append("\r\n");
		return header.toString();
	}
	
	/**
	 * Helper method that checks if the request header was already generated and
	 * if so, throws a {@link RuntimeException}.
	 * <p>
	 * This method is used in all <code>public</code> methods that can modify request
	 * context's properties even after the header was generated in order to stop
	 * subsequent modifications after header writing.
	 * 
	 * @throws RuntimeException if the header generation flag is set to <code>true</code>
	 */
	private void checkHeaderGenerated() {
		if(headerGenerated) throw new RuntimeException("The header was already generated!");
	}
	
	/**
	 * Sets this request's encoding.
	 * <p>
	 * Cannot be set if the header was already generated.
	 * 
	 * @param encoding - encoding to set for this request
	 * @throws RuntimeException if the request header was already generated
	 */
	public void setEncoding(String encoding) {
		checkHeaderGenerated();
		this.encoding = encoding;
	}
	
	/**
	 * Sets this request's status code.
	 * 
	 * @param statusCode - status code to set for this request
	 * @throws RuntimeException if the request header was already generated
	 */
	public void setStatusCode(int statusCode) {
		checkHeaderGenerated();
		this.statusCode = statusCode;
	}
	
	/**
	 * Sets this request's status text.
	 * 
	 * @param statusText - status text to set for this request
	 * @throws RuntimeException if the request header was already generated
	 */
	public void setStatusText(String statusText) {
		checkHeaderGenerated();
		this.statusText = statusText;
	}
	
	/**
	 * Sets this request's mime type.
	 * 
	 * @param mimeType - mime type to set for this request
	 * @throws RuntimeException if the request header was already generated
	 */
	public void setMimeType(String mimeType) {
		checkHeaderGenerated();
		this.mimeType = mimeType;
	}
	
	/**
	 * Sets this request's content length.
	 * 
	 * @param contentLength - content length to set for this request
	 * @throws RuntimeException if the request header was already generated
	 */
	public void setContentLength(Long contentLength) {
		checkHeaderGenerated();
		this.contentLength = contentLength;
	}
	
	/**
	 * Getter for the map of parameters of this request.
	 * 
	 * @return this request's parameters
	 */
	public Map<String, String> getParameters() {
		return parameters;
	}
	
	/**
	 * Getter for the map of temporary parameters of this request.
	 * 
	 * @return this request's temporary parameters
	 */
	public Map<String, String> getTemporaryParameters() {
		return temporaryParameters;
	}

	/**
	 * Sets the map of temporary parameters of this request.
	 * 
	 * @param temporaryParameters - map of temporary parameters to set for this request
	 */
	public void setTemporaryParameters(Map<String, String> temporaryParameters) {
		this.temporaryParameters = temporaryParameters;
	}

	/**
	 * Getter for the map of persistent parameters of this request.
	 * 
	 * @return this request's persistent parameters
	 */
	public Map<String, String> getPersistentParameters() {
		return persistentParameters;
	}

	/**
	 * Sets the map of persistent parameters of this request.
	 * 
	 * @param persistentParameters - map of persistent parameters to set for this request
	 */
	public void setPersistentParameters(Map<String, String> persistentParameters) {
		this.persistentParameters = persistentParameters;
	}
	
	/**
	 * This class models an HTTP request cookie.
	 * <p>
	 * This cookie has HTTP only property support.
	 * 
	 * @author Ivan Skorupan
	 */
	public static class RCCookie {
		
		/**
		 * This cookie's name.
		 */
		private String name;

		/**
		 * This cookie's value.
		 */
		private String value;

		/**
		 * The domain this cookie is valid for.
		 */
		private String domain;

		/**
		 * This cookie's path.
		 */
		private String path;

		/**
		 * Time period in which this cookie is valid.
		 */
		private Integer maxAge;
		
		/**
		 * Flag that indicates whether or not this cookie is HTTP only.
		 */
		private boolean httpOnly;
		
		/**
		 * Constructs a new {@link RCCookie} object.
		 * 
		 * @param name - this cookie's name
		 * @param value - this cookie's value
		 * @param maxAge - time period in which this cookie is valid
		 * @param domain - domain for which this cookie is valid
		 * @param path - this cookie's path
		 * @param httpOnly - indicator if this is an HTTP only cookie
		 * @throws NullPointerException if <code>name</code> or <code>value</code> is <code>null</code>
		 */
		public RCCookie(String name, String value, Integer maxAge, String domain, String path, boolean httpOnly) {
			this.name = Objects.requireNonNull(name);
			this.value = Objects.requireNonNull(value);
			this.domain = domain;
			this.path = path;
			this.maxAge = maxAge;
			this.httpOnly = httpOnly;
		}
		
		/**
		 * Getter for cookie name field.
		 * 
		 * @return the name property of this cookie
		 */
		public String getName() {
			return name;
		}

		/**
		 * Getter for cookie value field.
		 * 
		 * @return the value property of this cookie
		 */
		public String getValue() {
			return value;
		}
		
		/**
		 * Getter for cookie domain field.
		 * 
		 * @return the domain property of this cookie
		 */
		public String getDomain() {
			return domain;
		}

		/**
		 * Getter for cookie path field.
		 * 
		 * @return the path property of this cookie
		 */
		public String getPath() {
			return path;
		}

		/**
		 * Getter for cookie max age field.
		 * 
		 * @return the max age property of this cookie
		 */
		public Integer getMaxAge() {
			return maxAge;
		}
		
		/**
		 * Returns the <code>httpOnly</code> flag.
		 * 
		 * @return <code>true</code> if this cookie is HTTP only, <code>false</code> otherwise
		 */
		public boolean isHttpOnly() {
			return httpOnly;
		}
	}
	
}
