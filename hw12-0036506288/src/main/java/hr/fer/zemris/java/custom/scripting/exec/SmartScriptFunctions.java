package hr.fer.zemris.java.custom.scripting.exec;

import static java.lang.Math.*;

import java.text.DecimalFormat;
import java.util.Stack;

import hr.fer.zemris.java.webserver.RequestContext;

/**
 * This class provides <code>public static</code> methods that
 * model functions supported in SmartScript.
 * <p>
 * This is used in {@link SmartScriptEngine} to execute
 * the script.
 * 
 * @author Ivan Skorupan
 */
public class SmartScriptFunctions {
	
	/**
	 * Takes a value from the top of <code>temporaryStack</code>,
	 * performs a sine operation and pushed the result back on the stack.
	 * 
	 * @param temporaryStack - stack to take the operand from
	 */
	public static void sine(Stack<ValueWrapper> temporaryStack) {
		double value = Double.parseDouble(temporaryStack.pop().getValue().toString());
		temporaryStack.push(new ValueWrapper(sin(toRadians(value))));
	}
	
	/**
	 * Takes a string format and a <code>double</code> value from top of the
	 * <code>temporaryStack</code> and pushes on the stack a double value formatted
	 * using the {@link DecimalFormat} class.
	 * 
	 * @param temporaryStack - stack to take the operands from
	 */
	public static void decfmt(Stack<ValueWrapper> temporaryStack) {
		String f = temporaryStack.pop().getValue().toString();
		double x = Double.parseDouble(temporaryStack.pop().getValue().toString());
		DecimalFormat df = new DecimalFormat(f);
		temporaryStack.push(new ValueWrapper(df.format(x)));
	}
	
	/**
	 * Takes a value from the top of the <code>temporaryStack</code> and
	 * pushes back two such values, effectively duplicating it.
	 * 
	 * @param temporaryStack - stack to take the operand from
	 */
	public static void dup(Stack<ValueWrapper> temporaryStack) {
		ValueWrapper x = temporaryStack.pop();
		temporaryStack.push(x);
		temporaryStack.push(new ValueWrapper(x.getValue()));
	}
	
	/**
	 * Pops two values from <code>temporaryStack</code> and then pushes them
	 * back on the stack reversely.
	 * 
	 * @param temporaryStack - stack to take the operands from
	 */
	public static void swap(Stack<ValueWrapper> temporaryStack) {
		ValueWrapper a = temporaryStack.pop();
		ValueWrapper b = temporaryStack.pop();
		temporaryStack.push(a);
		temporaryStack.push(b);
	}
	
	/**
	 * Pops a value from <code>temporaryStack</code> and then sets it
	 * as <code>requestContext</code>'s mime type.
	 * 
	 * @param requestContext - request context object whose mime type should be set
	 * @param temporaryStack - stack to take the operand from
	 */
	public static void setMimeType(RequestContext requestContext, Stack<ValueWrapper> temporaryStack) {
		String x = temporaryStack.pop().getValue().toString();
		requestContext.setMimeType(x);
	}
	
	/**
	 * Pops two values from <code>temporaryStack</code>, first popped value being the default
	 * parameter value and the second one being parameter name.
	 * <p>
	 * Then, the parameter value is fetched from <code>requestContext</code>'s parameters map
	 * using the parameter name as key.
	 * <p>
	 * In the end, if the fetched value is <code>null</code>, the default value is pushed on the
	 * stack, otherwise the fetched value is pushed.
	 * 
	 * @param requestContext - request context object whose parameter value should be fetched
	 * @param temporaryStack - stack to take the operands from
	 */
	public static void paramGet(RequestContext requestContext, Stack<ValueWrapper> temporaryStack) {
		ValueWrapper defaultValue = temporaryStack.pop();
		String name = temporaryStack.pop().getValue().toString();
		String value = requestContext.getParameter(name);
		temporaryStack.push((value == null) ? defaultValue : new ValueWrapper(value));
	}
	
	/**
	 * Pops two values from <code>temporaryStack</code>, first popped value being the default
	 * parameter value and the second one being persistent parameter name.
	 * <p>
	 * Then, the persistent parameter value is fetched from <code>requestContext</code>'s
	 * persistent parameters map using the persistent parameter name as key.
	 * <p>
	 * In the end, if the fetched value is <code>null</code>, the default value is pushed on the
	 * stack, otherwise the fetched value is pushed.
	 * 
	 * @param requestContext - request context object whose persistent parameter value should be fetched
	 * @param temporaryStack - stack to take the operands from
	 */
	public static void pparamGet(RequestContext requestContext, Stack<ValueWrapper> temporaryStack) {
		ValueWrapper defaultValue = temporaryStack.pop();
		String name = temporaryStack.pop().getValue().toString();
		String value = requestContext.getPersistentParameter(name);
		temporaryStack.push((value == null) ? defaultValue : new ValueWrapper(value));
	}
	
	/**
	 * Pops two values from <code>temporaryStack</code>, those being the persistent
	 * parameter name and value.
	 * <p>
	 * The popped values are used to set one <code>requestContext</code>'s persistent parameter
	 * to (name, value).
	 * 
	 * @param requestContext - request context object whose persistent parameter value should be set
	 * @param temporaryStack - stack to take the operands from
	 */
	public static void pparamSet(RequestContext requestContext, Stack<ValueWrapper> temporaryStack) {
		String name = temporaryStack.pop().getValue().toString();
		String value = temporaryStack.pop().getValue().toString();
		requestContext.setPersistentParameter(name, value);
	}
	
	/**
	 * Pops one value from <code>temporaryStack</code>, that being a persistent parameter name and
	 * then deletes the entry for that name from <code>requestContext</code>'s persistent
	 * parameters map.
	 * 
	 * @param requestContext - request context object whose persistent parameter entry should be deleted
	 * @param temporaryStack - stack to take the operand from
	 */
	public static void pparamDel(RequestContext requestContext, Stack<ValueWrapper> temporaryStack) {
		String name = temporaryStack.pop().getValue().toString();
		requestContext.removePersistentParameter(name);
	}
	
	/**
	 * Pops two values from <code>temporaryStack</code>, first popped value being the default
	 * parameter value and the second one being persistent parameter name.
	 * <p>
	 * Then, the temporary parameter value is fetched from <code>requestContext</code>'s
	 * temporary parameters map using the temporary parameter name as key.
	 * <p>
	 * In the end, if the fetched value is <code>null</code>, the default value is pushed on the
	 * stack, otherwise the fetched value is pushed.
	 * 
	 * @param requestContext - request context object whose temporary parameter value should be fetched
	 * @param temporaryStack - stack to take the operands from
	 */
	public static void tparamGet(RequestContext requestContext, Stack<ValueWrapper> temporaryStack) {
		ValueWrapper defaultValue = temporaryStack.pop();
		String name = temporaryStack.pop().getValue().toString();
		String value = requestContext.getTemporaryParameter(name);
		temporaryStack.push((value == null) ? defaultValue : new ValueWrapper(value));
	}
	
	/**
	 * Pops two values from <code>temporaryStack</code>, those being the temporary
	 * parameter name and value.
	 * <p>
	 * The popped values are used to set one <code>requestContext</code>'s temporary parameter
	 * to (name, value).
	 * 
	 * @param requestContext - request context object whose temporary parameter entry should be set
	 * @param temporaryStack - stack to take the operands from
	 */
	public static void tparamSet(RequestContext requestContext, Stack<ValueWrapper> temporaryStack) {
		String name = temporaryStack.pop().getValue().toString();
		String value = temporaryStack.pop().getValue().toString();
		requestContext.setTemporaryParameter(name, value);
	}
	
	/**
	 * Pops one value from <code>temporaryStack</code>, that being a temporary parameter name and
	 * then deletes the entry for that name from <code>requestContext</code>'s temporary
	 * parameters map.
	 * 
	 * @param requestContext - request context object whose temporary parameter entry should be deleted
	 * @param temporaryStack - stack to take the operand from
	 */
	public static void tParamDel(RequestContext requestContext, Stack<ValueWrapper> temporaryStack) {
		String name = temporaryStack.pop().getValue().toString();
		requestContext.removeTemporaryParameter(name);
	}
	
}
