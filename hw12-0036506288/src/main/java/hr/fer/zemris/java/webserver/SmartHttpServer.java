package hr.fer.zemris.java.webserver;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PushbackInputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;

import hr.fer.zemris.java.custom.scripting.exec.SmartScriptEngine;
import hr.fer.zemris.java.custom.scripting.parser.SmartScriptParser;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;

/**
 * This class models a simple multithreaded HTTP server that has
 * support for cookies.
 * <p>
 * The server also supports the execution of SmartScripts and also
 * web workers modeled by {@link IWebWorker} interface.
 * 
 * @author Ivan Skorupan
 */
public class SmartHttpServer {
	
	/**
	 * Fully Qualified Class Path (FQCN) to package where all web workers for this server are situated.
	 */
	private static final String WORKERS_PATH = "hr.fer.zemris.java.webserver.workers";
	
	/**
	 * This server's IP address.
	 */
	private String address;
	
	/**
	 * This server's domain name.
	 */
	private String domainName;
	
	/**
	 * Port on which this server listens for connections.
	 */
	private int port;
	
	/**
	 * Number of worker threads this server will use to proccess client requests.
	 */
	private int workerThreads;
	
	/**
	 * Number of seconds that a single session should last before becoming invalid.
	 */
	private int sessionTimeout;
	
	/**
	 * Map of all mime types this server supports.
	 */
	private Map<String,String> mimeTypes = new HashMap<>();
	
	/**
	 * Map of workers this server supports.
	 */
	private Map<String,IWebWorker> workersMap = new HashMap<>();
	
	/**
	 * A reference to this server's thread object.
	 */
	private ServerThread serverThread = new ServerThread();
	
	/**
	 * The threadpool used by this server to parallelize client request processing.
	 */
	private ExecutorService threadPool;
	
	/**
	 * Path to the root folder of this server. The client should never be able to reach a path
	 * that's not a child of this path.
	 */
	private Path documentRoot;
	
	/**
	 * Map of sessions that exist in this server.
	 */
	private Map<String, SessionMapEntry> sessions = new HashMap<>();
	
	/**
	 * A reference to a {@link Random} object used for generating new session IDs.
	 */
	private Random sessionRandom = new Random();

	/**
	 * Constructs a new {@link SmartHttpServer} object.
	 * <p>
	 * The constructor does a lot of things and does not just
	 * set this class's fields.
	 * <p>
	 * Mostly it parses information about the server from .properties
	 * configuration files but it also makes a session cleaning thread
	 * which makes sure that every 5 minutes all inactive sessions are
	 * deleted from the <code>sessions</code> map in order to reduce
	 * memory leakage.
	 * 
	 * @param configFileName - path to server.properties file
	 * @throws IOException if there is an IO error while reading from configuration files
	 */
	public SmartHttpServer(String configFileName) throws IOException {
		Path configFile = null;
		try {
			configFile = Paths.get(configFileName);
		} catch(InvalidPathException ex) {
			System.out.println("The given configuration file path is invalid!");
			System.exit(-1);
		}

		Properties serverProperties = new Properties();
		serverProperties.load(Files.newInputStream(configFile));

		this.address = serverProperties.getProperty("server.address");
		this.domainName = serverProperties.getProperty("server.domainName");
		this.port = Integer.parseInt(serverProperties.getProperty("server.port"));
		this.workerThreads = Integer.parseInt(serverProperties.getProperty("server.workerThreads"));
		this.sessionTimeout = Integer.parseInt(serverProperties.getProperty("session.timeout"));
		this.documentRoot = Paths.get(serverProperties.getProperty("server.documentRoot"));

		Path mimeConfig = Paths.get(serverProperties.getProperty("server.mimeConfig"));
		Properties mimeProperties = new Properties();
		mimeProperties.load(Files.newInputStream(mimeConfig));

		for(Entry<Object, Object> entry : mimeProperties.entrySet()) {
			mimeTypes.put((String) entry.getKey(), (String) entry.getValue());
		}
		
		Path workerConfig = Paths.get(serverProperties.getProperty("server.workers"));
		Properties workerProperties = new Properties();
		workerProperties.load(Files.newInputStream(workerConfig));
		
		for(Entry<Object, Object> entry : workerProperties.entrySet()) {
			if(workersMap.containsKey(entry.getKey())) {
				throw new IllegalStateException("There cannot be two identical paths in workers.properties file!");
			}
			
			String path = (String) entry.getKey();
			String fqcn = (String) entry.getValue();
			workersMap.put(path, getWorkerInstance(fqcn));
		}
		
		startSessionCleaningThread();
	}
	
	/**
	 * Instantiates and starts a daemon thread which cleans inactive sessions
	 * every 5 minutes in order to reduce memory usage of this server. 
	 */
	private void startSessionCleaningThread() {
		Timer timer = new Timer(true);
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				Iterator<Entry<String, SessionMapEntry>> it = sessions.entrySet().iterator();
				while(it.hasNext()) {
					Entry<String, SessionMapEntry> entry = it.next();
					if(entry.getValue().validUntil < Calendar.getInstance().getTimeInMillis()) {
						it.remove();
					}
				}
			}
		};
		timer.schedule(task, 300 * 1000);
	}
	
	/**
	 * Starting point of this program.
	 * 
	 * @param args - command line arguments
	 */
	public static void main(String[] args) {
		if(args.length != 1) {
			System.out.println("Exactly one argument was expected: a path to server configuration file!");
			return;
		}

		SmartHttpServer server = null;
		try {
			server = new SmartHttpServer(args[0]);
		} catch(IOException e) {
			System.out.println("An IO error occurred while trying to make a SmartHttpServer object!");
			System.out.println("The error message is: " + e.getMessage());
			return;
		}

		server.start();
		System.out.println("Server started...");
	}
	
	/**
	 * Uses Java Reflection API in order to instantiate an {@link IWebWorker}
	 * object using the given fully qualified class name <code>fqcn</code> as a
	 * path to the class that should be instantiated.
	 * 
	 * @param fqcn - fully qualified class name of a web worker to be instantiated
	 * @return an instance of {@link IWebWorker} object whose instance is of class type defined by <code>fqcn</code>
	 */
	private IWebWorker getWorkerInstance(String fqcn) {
		Class<?> referenceToClass = null;
		Object newObject = null;
		
		try {
			referenceToClass = this.getClass().getClassLoader().loadClass(fqcn);
			newObject = referenceToClass.getDeclaredConstructor().newInstance();
		} catch (Exception e) {
			System.out.println("An error occured while processing workers using Java Reflection API!");
			System.out.println("The fqcn was: " + fqcn);
			System.out.println("The error message is: " + e.getMessage());
			System.exit(-1);
		}
		
		return (IWebWorker) newObject;
	}
	
	/**
	 * Generates a new random session ID as a sequence of 20 randomly generated
	 * and concatenated English uppercase letters.
	 * 
	 * @return a new randomly generated session ID as a {@link String} object
	 */
	private String generateRandomSID() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < 20; i++) {
			sb.append(Character.toChars(sessionRandom.nextInt(25) + 65));
		}
		
		return sb.toString();
	}

	/**
	 * Starts the {@link #serverThread} if it hasn't already been started and
	 * also instantiates a new threadpool using {@link Executors} class to
	 * parallelize the server.
	 */
	protected synchronized void start() {
		if(!serverThread.isAlive()) {
			serverThread.start();
		}
		threadPool = Executors.newFixedThreadPool(workerThreads);
	}

	/**
	 * Signals the {@link #serverThread} to stop and shuts
	 * down the threadpool.
	 */
	protected synchronized void stop() {
		serverThread.stop = true;
		threadPool.shutdown();
	}
	
	/**
	 * Models a single session.
	 * 
	 * @author Ivan Skorupan
	 */
	private static class SessionMapEntry {
		
		/**
		 * This session's id.
		 */
		private String sid;
		
		/**
		 * The host that this session belongs to.
		 */
		private String host;
		
		/**
		 * Time until this session becomes invalid.
		 */
		private long validUntil;
		
		/**
		 * This map contains parameters that will later become
		 * persistent parameters of the server's response request.
		 */
		private Map<String, String> map = new ConcurrentHashMap<>();
		
		/**
		 * Constructs a new {@link SessionMapEntry} object.
		 * 
		 * @param sid - this session's id
		 * @param host - host this session belongs to
		 * @param validUntil - time until this session becomes invalid
		 */
		public SessionMapEntry(String sid, String host, long validUntil) {
			this.sid = sid;
			this.host = host;
			this.validUntil = validUntil;
		}
		
	}

	/**
	 * Models a server thread that runs in a while loop until it is signaled to
	 * stop.
	 * <p>
	 * In the while loop, the thread listens for new connections and when it gets one,
	 * it delegates the client request processing to a new {@link ClientWorker} that
	 * is submitted to the threadpool.
	 * 
	 * @author Ivan Skorupan
	 */
	protected class ServerThread extends Thread {

		/**
		 * Flag that indicates if the server thread should keep running or stop.
		 */
		private volatile boolean stop;
		
		@Override
		public void run() {
			ServerSocket serverSocket = null;
			try {
				serverSocket = new ServerSocket();
				serverSocket.bind(new InetSocketAddress(InetAddress.getByName(address), port));
				serverSocket.setSoTimeout(1000);
			} catch (IOException e) {
				System.out.println("There was a problem while opening a server socket on port: " + port + "!");
				System.out.println("The error message is: " + e.getMessage());
				SmartHttpServer.this.stop();
			}

			Socket client = null;
			while(!stop) {
				try {
					client = serverSocket.accept();
				} catch(SocketTimeoutException ex) {
					continue;
				} catch(IOException e) {
					System.out.println("An IO error occurred while waiting for a connection!");
					System.out.println("The error message is: " + e.getMessage());
					SmartHttpServer.this.stop();
					continue;
				}
				ClientWorker cw = new ClientWorker(client);
				threadPool.submit(cw);
			}

			if(client != null) {
				try {
					client.close();
				} catch (IOException e) {
					System.out.println("The client socket could not properly close because of an IO error!");
				}
			}
			
			try {
				serverSocket.close();
			} catch (IOException e) {
				System.out.println("The server socket could not properly close because of an IO error!");
			}
		}
	}

	/**
	 * Models objects that can process client requests.
	 * 
	 * @author Ivan Skorupan
	 */
	private class ClientWorker implements Runnable, IDispatcher {
		
		/**
		 * Client socket that this client worker uses.
		 */
		private Socket csocket;
		
		/**
		 * The input stream used to read client request data.
		 */
		private PushbackInputStream istream;
		
		/**
		 * The output stream used to make a response request context and through
		 * which to write error messages to the client.
		 */
		private OutputStream ostream;
		
		/**
		 * HTTP protocol version used by the client.
		 */
		private String version;
		
		/**
		 * The client request HTTP method sent.
		 */
		private String method;
		
		/**
		 * The host from which the server got the request processed by this client worker.
		 */
		private String host;
		
		/**
		 * Map of parameters given to us by the client through the URL.
		 */
		private Map<String,String> params = new HashMap<String, String>();
		
		/**
		 * Map of temporary parameters.
		 */
		private Map<String,String> tempParams = new HashMap<String, String>();
		
		/**
		 * Map of temporary parameters.
		 */
		private Map<String,String> permParams;
		
		/**
		 * Map of cookies we got from the client.
		 */
		private List<RCCookie> outputCookies = new ArrayList<RequestContext.RCCookie>();
		
		/**
		 * The request context used by this client worker to send a server response to the client.
		 */
		private RequestContext context;
		
		/**
		 * The session ID of currently processed user request.
		 */
		private String SID;

		/**
		 * Constructs a new {@link ClientWorker} object.
		 * 
		 * @param csocket - client socket that this client worker uses
		 * @throws NullPointerException if <code>csocket</code> is <code>null</code>s
		 */
		public ClientWorker(Socket csocket) {
			this.csocket = Objects.requireNonNull(csocket);
		}

		@Override
		public void run() {
			try {
				serveClient();
				ostream.flush();
				csocket.close();
			} catch(IOException e) {
				System.out.println("There was a problem while performing an IO operation!");
				System.out.println("The error message is: " + e.getMessage());
				return;
			}
		}
		
		/**
		 * Serves the client by processing its request. 
		 * <p>
		 * The method first reads all headers of gotten client request
		 * and tests its validity.
		 * <p>
		 * Afterwards, it delegates work to other methods in order to decide how
		 * to actually serve the client.
		 * 
		 * @throws IOException is there is an IO error while reading from an input stream
		 * or writing to an output stream (to the client)
		 */
		private void serveClient() throws IOException {
			istream = new PushbackInputStream(new BufferedInputStream(csocket.getInputStream()));
			ostream = new BufferedOutputStream(csocket.getOutputStream());

			byte[] request = readRequest(istream);
			if(request==null) {
				sendError(ostream, 400, "Bad request");
				return;
			}

			String requestStr = new String(
					request,
					StandardCharsets.US_ASCII
					);

			List<String> headers = extractHeaders(requestStr);
			String[] firstLine = headers.isEmpty() ? 
					null : headers.get(0).split(" ");
			if(firstLine==null || firstLine.length != 3) {
				sendError(ostream, 400, "Bad request");
				return;
			}

			method = firstLine[0].toUpperCase();
			if(!method.equals("GET")) {
				sendError(ostream, 405, "Method Not Allowed");
				return;
			}

			version = firstLine[2].toUpperCase();
			if(!version.equals("HTTP/1.1") && !version.equals("HTTP/1.0")) {
				sendError(ostream, 505, "HTTP Version Not Supported");
				return;
			}

			for(String line : headers) {
				if(line.startsWith("Host:")) {
					String value = line.substring(5).trim();
					if(value.contains(":")) {
						host = value.substring(0, value.indexOf(":"));
					} else {
						host = value;
					}
				}
			}

			host = (host == null) ? domainName : host;

			String pathTokens[] = firstLine[1].split("\\?");
			String urlPath = pathTokens[0];
			String paramString = (pathTokens.length == 2) ? pathTokens[1] : null;
			
			checkSession(headers);
			permParams = sessions.get(SID).map;
			
			if(paramString != null) {
				parseParameters(paramString);
			}
			
			internalDispatchRequest(urlPath, true);
		}
		
		/**
		 * Checks if given user request's headers match a session
		 * that has already been saved into internal sessions map.
		 * <p>
		 * If so, the session's lifetime is reset
		 * <p>
		 * Otherwise, a new session is created and saved.
		 * 
		 * @param headers - client request headers
		 */
		private synchronized void checkSession(List<String> headers) {
			String sidCandidate = null;
			for(String header : headers) {
				if(!header.startsWith("Cookie:")) continue;
				
				String cookies = header.substring(7).trim();
				String[] distinctCookies = cookies.split(";");
				for(String cookie : distinctCookies) {
					String[] nameValuePair = cookie.split("=");
					if(nameValuePair[0].equals("sid")) {
						sidCandidate = nameValuePair[1].substring(1, nameValuePair[1].length() - 1);
					}
				}
			}
			
			Calendar calendar = Calendar.getInstance();
			if(sidCandidate == null) {
				createNewSession(calendar);
			} else {
				SessionMapEntry session = sessions.get(sidCandidate);
				
				if(session == null || !host.equals(session.host)) {
					createNewSession(calendar);
				} else if(session.validUntil < Calendar.getInstance().getTimeInMillis()) {
					sessions.remove(session.sid);
					createNewSession(calendar);
				} else {
					SID = session.sid;
					session.validUntil = calendar.getTimeInMillis() + 1000 * sessionTimeout;
				}
			}
		}
		
		/**
		 * Creates a new session and puts it in the sessions map.
		 * 
		 * @param calendar - an instance of a calendar to use for setting the lifetime of this new session
		 */
		private void createNewSession(Calendar calendar) {
			SID = generateRandomSID();
			SessionMapEntry entry = new SessionMapEntry(SID, host,
					calendar.getTimeInMillis() + 1000 * sessionTimeout);
			
			sessions.put(SID, entry);
		}

		/**
		 * This method contains the most important logic for deciding how to process a client request.
		 * <p>
		 * The given <code>urlPath</code> is tested and based on the path decisions are made.
		 * <p>
		 * A decision could be an execution of a SmartScript, image or file serving or web worker
		 * execution.
		 * 
		 * @param urlPath - user typed path in the URL part of address bar
		 * @param directCall - indicator if this method was called from the outside (by the client through a request)
		 * @throws IOException if an IO error occurs while writing data to the client
		 */
		private void internalDispatchRequest(String urlPath, boolean directCall) throws IOException {
			Path path = Paths.get(urlPath.startsWith("/") ? urlPath.substring(1) : urlPath);
			Path requestedFile = documentRoot.resolve(path);
			
			if(directCall == true && urlPath.startsWith("/private/")) {
				sendError(ostream, 404, "File not found");
				return;
			}
			
			RequestContext rc = (context == null) ? new RequestContext(ostream, params, permParams,
					outputCookies, tempParams, this, SID) : context;
			
			RCCookie sessionCookie = new RCCookie("sid", SID, null, host, "/", true);
			rc.addRCCookie(sessionCookie);
			
			IWebWorker worker = null;
			if(urlPath.startsWith("/ext/")) {
				worker = getWorkerInstance(WORKERS_PATH + "." + urlPath.substring(urlPath.lastIndexOf("/") + 1));
			} else {
				worker = workersMap.get(urlPath);
			}
			
			if(worker != null) {
				try {
					worker.processRequest(rc);
				} catch (Exception e) {
					System.out.println("An error occurred during workers processing!");
					System.out.println("The error message is: " + e.getMessage());
				}
				return;
			}
			
			if(!requestedFile.normalize().startsWith(documentRoot)) {
				sendError(ostream, 403, "Forbidden");
			}

			if(!Files.isReadable(requestedFile)) {
				sendError(ostream, 404, "File not found");
				return;
			}
			
			if(isSmartScript(requestedFile)) {
				String document = Files.readString(requestedFile);
				SmartScriptParser parser = new SmartScriptParser(document);
				SmartScriptEngine engine = new SmartScriptEngine(parser.getDocumentNode(), rc);
				engine.execute();
				return;
			}
			
			String mimeType = determineMimeType(requestedFile.getFileName().toString());
			rc.setMimeType(mimeType);

			if(mimeType.startsWith("image/")) {
				serveImage(rc, requestedFile, mimeType.substring(mimeType.indexOf("/") + 1));
			} else {
				rc.setContentLength(Files.size(requestedFile));
				serveFile(rc, requestedFile);
			}
		}

		/**
		 * Tests if the given <code>requestedFile</code> is a SmartScript.
		 * <p>
		 * A file is a SmartScript if it has extension ".smscr".
		 * 
		 * @param requestedFile - file to be tested for being a SmartScript
		 * @return <code>true</code> if <code>requestedFile</code> is a SmartScript, <code>false</code> otherwise
		 */
		private boolean isSmartScript(Path requestedFile) {
			return requestedFile.getFileName().toString().endsWith(".smscr");
		}

		@Override
		public void dispatchRequest(String urlPath) throws Exception {
			internalDispatchRequest(urlPath, false);
		}

		/**
		 * Serves an image to the client in case the requested URL path led to an image.
		 * 
		 * @param requestContext - server response request to use for serving the image to client
		 * @param image - image path
		 * @param format - format in which to serve the image
		 * @throws IOException if an IO error occurs while writing the image to a buffer or to to output stream
		 * of <code>requestContext</code>
		 */
		private void serveImage(RequestContext requestContext, Path image, String format) throws IOException {
			BufferedImage bim = ImageIO.read(image.toFile());
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			ImageIO.write(bim, format, bos);
			byte[] podatci = bos.toByteArray();
			requestContext.write(podatci);
		}

		/**
		 * Serves a file to the client in case the requested URL path led to a file.
		 * 
		 * @param requestContext - server response request to use for serving the image to client
		 * @param requestedFile - requested file path
		 * @throws IOException if an IO error occurs while while writing the file into a buffer or to an output
		 * stream of <code>requestContext</code>
		 */
		private void serveFile(RequestContext requestContext, Path requestedFile) throws IOException {
			try(InputStream is = new BufferedInputStream(Files.newInputStream(requestedFile))){
				byte[] buf = new byte[1024];
				while(true) {
					int r = is.read(buf);
					if(r < 1) break;
					requestContext.write(buf, 0, r);
				}
			}
		}

		/**
		 * Returns the appropriate mime type for given <code>fileName</code>.
		 * 
		 * @param fileName - name of file to derive a mime type from
		 * @return an appropriate mime type fetched from mime.properties
		 * file
		 */
		private String determineMimeType(String fileName) {
			fileName = fileName.toLowerCase();
			String mimeType = mimeTypes.get(fileName.substring(fileName.lastIndexOf(".") + 1));
			return (mimeType == null) ? "application/octet-stream" : mimeType;
		}

		/**
		 * Parses parameters from given <code>paramString</code> and puts them in
		 * the parameters map of this client worker.
		 * <p>
		 * The parameters must be separated by a "&" sign and the key-value pair
		 * of a single parameter must be separated by an "=" sign.
		 * <p>
		 * A parameter with only its name given, without the value, is also valid.
		 * In that case, it will also be put into parameters map, but with a value
		 * <code>null</code>
		 * 
		 * @param paramString - string which contains parameters to be parsed
		 */
		private void parseParameters(String paramString) {
			String[] parameters = paramString.split("&");
			for(String parameter : parameters) {
				String[] keyValue = parameter.split("=");
				if(keyValue.length == 1) {
					params.put(keyValue[0], null);
					continue;
				}
				
				params.put(keyValue[0], keyValue[1]);
			}
		}

		/**
		 * Reads a client request from the input stream <code>is</code> using
		 * a simple Moore state machine.
		 * 
		 * @param is - client input stream
		 * @return bytes array of the client's request
		 * @throws IOException if an IO error occurs while reading or writing the client request
		 */
		private byte[] readRequest(InputStream is) throws IOException {
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int state = 0;
			l:		while(true) {
				int b = is.read();
				if(b==-1) return null;
				if(b!=13) {
					bos.write(b);
				}
				switch(state) {
				case 0: 
					if(b==13) { state=1; } else if(b==10) state=4;
					break;
				case 1: 
					if(b==10) { state=2; } else state=0;
					break;
				case 2: 
					if(b==13) { state=3; } else state=0;
					break;
				case 3: 
					if(b==10) { break l; } else state=0;
					break;
				case 4: 
					if(b==10) { break l; } else state=0;
					break;
				}
			}
			return bos.toByteArray();
		}

		/**
		 * Extracts request headers from a given <code>requestHeader</code>
		 * and returns them in a list of strings.
		 * 
		 * @param requestHeader - string that contains the request headers
		 * @return a list of request headers
		 */
		private List<String> extractHeaders(String requestHeader) {
			List<String> headers = new ArrayList<String>();
			String currentLine = null;
			for(String s : requestHeader.split("\n")) {
				if(s.isEmpty()) break;
				char c = s.charAt(0);
				if(c==9 || c==32) {
					currentLine += s;
				} else {
					if(currentLine != null) {
						headers.add(currentLine);
					}
					currentLine = s;
				}
			}
			if(!currentLine.isEmpty()) {
				headers.add(currentLine);
			}
			return headers;
		}

		/**
		 * Sends an error message to the client using the given output stream <code>ostream</code>.
		 * <p>
		 * The message will be seen as an HTML page indicating the given <code>statusCode</code>
		 * and <code>statusText</code> in order to give the client some more information about the
		 * error that occurred.
		 * 
		 * @param ostream - output stream to send an error through (usually a response request context's output stream)
		 * @param statusCode - the status code of the occurred error
		 * @param statusText - status text of occurred error
		 * @throws IOException if an IO error occurs while writing the error message to the output stream <code>ostream</code>
		 */
		private void sendError(OutputStream ostream, int statusCode, String statusText) throws IOException {
			ostream.write(
					("HTTP/1.1 " + statusCode + " " + statusText + "\r\n" +
						"Server: simple java server\r\n" +
						"Content-Type: text/plain;charset=UTF-8\r\n" +
						"Content-Length: 0\r\n" +
						"Connection: close\r\n" +
						"\r\n").getBytes(StandardCharsets.US_ASCII)
			);
		}
	}
}
