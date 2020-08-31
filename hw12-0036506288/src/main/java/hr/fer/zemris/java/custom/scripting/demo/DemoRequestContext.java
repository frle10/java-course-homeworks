package hr.fer.zemris.java.custom.scripting.demo;

import hr.fer.zemris.java.webserver.RequestContext;
import hr.fer.zemris.java.webserver.RequestContext.RCCookie;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * This is a simple demo program that tests the implementation of
 * {@link RequestContext} class.
 * 
 * @author Ivan Skorupan
 */
public class DemoRequestContext {
	
	/**
	 * Starting point of this demo program.
	 * 
	 * @param args - command line arguments (not used)
	 * @throws IOException if there is a problem while writing content to a file
	 */
	public static void main(String[] args) throws IOException {
		demo1("primjer1.txt", "ISO-8859-2");
		demo1("primjer2.txt", "UTF-8");
		demo2("primjer3.txt", "UTF-8");
	}
	
	/**
	 * First demo method that sets up one {@link RequestContext} and then
	 * writes some content through it.
	 * 
	 * @param filePath - path to which the request data will be written
	 * @param encoding - encoding to use for data writing
	 * @throws IOException if there is a problem while writing data to the file
	 */
	private static void demo1(String filePath, String encoding) throws IOException {
		OutputStream os = Files.newOutputStream(Paths.get(filePath));
		
		RequestContext rc = new RequestContext(os, new HashMap<String, String>(),
				new HashMap<String, String>(),
				new ArrayList<RequestContext.RCCookie>(),
				null);
		rc.setEncoding(encoding);
		rc.setMimeType("text/plain");
		rc.setStatusCode(205);
		rc.setStatusText("Idemo dalje");
		
		// Only at this point will header be created and written...
		rc.write("Čevapčići i Šiščevapčići.");
		
		os.close();
	}
	
	/**
	 * Second demo method that sets up one {@link RequestContext} and then
	 * writes some content through it.
	 * <p>
	 * This method also adds some cookied to the request before writing to
	 * a file.
	 * 
	 * @param filePath - path to which the request data will be written
	 * @param encoding - encoding to use for data writing
	 * @throws IOException if there is a problem while writing data to the file
	 */
	private static void demo2(String filePath, String encoding) throws IOException {
		OutputStream os = Files.newOutputStream(Paths.get(filePath));
		
		RequestContext rc = new RequestContext(os, new HashMap<String, String>(),
				new HashMap<String, String>(),
				new ArrayList<RequestContext.RCCookie>(),
				null);
		rc.setEncoding(encoding);
		rc.setMimeType("text/plain");
		rc.setStatusCode(205);
		rc.setStatusText("Idemo dalje");
		rc.addRCCookie(new RCCookie("korisnik", "perica", 3600, "127.0.0.1", "/", false));
		rc.addRCCookie(new RCCookie("zgrada", "B4", null, null, "/", false));
		
		// Only at this point will header be created and written...
		rc.write("Čevapčići i Šiščevapčići.");
		
		os.close();
	}
}
