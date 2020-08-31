package hr.fer.zemris.java.webserver.workers;

import java.util.Map;

import hr.fer.zemris.java.webserver.IWebWorker;
import hr.fer.zemris.java.webserver.RequestContext;

/**
 * This web worker tries to parse parameters with names "a" and "b"
 * given by the user in URL.
 * <p>
 * If those parameters exist, the worker tries to parse an integer value from them.
 * If the operation fails, the default values are set to a and b which are a = 1 and
 * b = 2.
 * <p>
 * In any case, after parsing there are definitely integer values in variables a and b,
 * they can be default or they could have been successfully parsed from the user.
 * <p>
 * Then, the worker sums those values, sets temporary parameters "varA", "varB"
 * and "zbroj" and an "imgName" temporary parameter whose value is a path to a picture.
 * <p>
 * The picture used depends on the sum. If the sum is even, one picture is used, otherwise
 * another picture is used.
 * <p>
 * After all that, the worker delegates additional work to script "calc.smscr".
 * 
 * @author Ivan Skorupan
 */
public class SumWorker implements IWebWorker {

	@Override
	public void processRequest(RequestContext context) throws Exception {
		Map<String, String> parameters = context.getParameters();
		int a = 1;
		int b = 2;
		
		try {
			a = Integer.parseInt(parameters.get("a"));
		} catch(NumberFormatException ex) {
		}
		
		try {
			b = Integer.parseInt(parameters.get("b"));
		} catch(NumberFormatException ex) {
		}
		
		int sum = a + b;
		context.setTemporaryParameter("zbroj", String.valueOf(sum));
		context.setTemporaryParameter("varA", String.valueOf(a));
		context.setTemporaryParameter("varB", String.valueOf(b));
		
		if(sum % 2 == 0) {
			context.setTemporaryParameter("imgName", "/images/owl.png");
		} else {
			context.setTemporaryParameter("imgName", "/images/tardis.png");
		}
		
		context.getDispatcher().dispatchRequest("/private/pages/calc.smscr");
	}
	
}
