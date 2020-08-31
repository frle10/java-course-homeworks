package hr.fer.zemris.java.webserver.workers;

import java.util.Map;
import java.util.Map.Entry;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * This web worker creates an HTML page that contains a table
 * of all parameters given by the user.
 * <p>
 * The first column of the table contains the parameter names
 * while the second column contains their values.
 * 
 * @author Ivan Skorupan
 */
public class EchoParams implements IWebWorker {
	
	@Override
	public void processRequest(RequestContext context) throws Exception {
		Map<String, String> params = context.getParameters();
		StringBuilder sb = new StringBuilder(
				"<html>\r\n" +
				"  <head>\r\n" + 
				"    <title>Parameters Table</title>\r\n"+
				"  </head>\r\n" + 
				"  <body>\r\n" + 
				"    <table border='1'>\r\n" +
				"		<tr>" +
				"			<td>Name</td>" +
				"			<td>Value</td>" +
				"		</tr>"
		);
		
		for(Entry<String, String> entry : params.entrySet()) {
			sb.append("<tr>\r\n");
			sb.append("<td>" + entry.getKey() + "</td>\r\n");
			sb.append("<td>" + entry.getValue() + "</td>\r\n");
			sb.append("</tr>");
		}
		
		sb.append(
			"</table>\r\n" +
			"</body>"
		);
		
		context.setMimeType("text/html");
		context.write(sb.toString());
	}
	
}
