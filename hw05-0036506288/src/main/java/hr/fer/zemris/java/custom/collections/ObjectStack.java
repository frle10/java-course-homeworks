package hr.fer.zemris.java.custom.collections;

import java.util.Objects;

/**
 * This is an Adapter class for {@link LinkedListIndexedCollectio} class.
 * <p>
 * It models the stack data structure and provides methods for use that would
 * be expected from a stack to have such as <code>pop()</code>, <code>push()</code>,
 * <code>peek()</code> etc.
 * 
 * @author Ivan Skorupan
 */
public class ObjectStack<T> {
	
	/**
	 * A backing {@link LinkedListIndexedCollectio} object that is used as a stack.
	 * <p>
	 * All stack operations are performed on this object.
	 */
	private ArrayIndexedCollection<T> stack = new ArrayIndexedCollection<>();
	
	/**
	 * Tests if the stack has <b>no</b> elements.
	 * 
	 * @return <code>true</code> if the stack is empty, <code>false</code> otherwise
	 */
	public boolean isEmpty() {
		return stack.isEmpty();
	}
	
	/**
	 * Returns the number of elements in this stack (it's size).
	 * 
	 * @return number of elements in the stack
	 */
	public int size() {
		return stack.size();
	}
	
	/**
	 * Pushes the given value on the stack.
	 * <p>
	 * Values equal to <code>null</code> are not allowed to be placed on the stack.
	 * 
	 * @param value - an object to be pushed on the stack
	 * @throws NullPointerException if <code>value</code> is <code>null</code>
	 */
	public void push(T value) {
		Objects.requireNonNull(value);
		
		stack.add(value);
		return;
	}
	
	/**
	 * Removes last value pushed on stack from stack and returns it.
	 * 
	 * @return the removed value
	 * @throws EmptyStackException if stack is empty
	 */
	public T pop() {
		if(this.isEmpty()) {
			throw new EmptyStackException("The stack is empty!");
		}
		
		T topObject = stack.get(this.size() - 1);
		stack.remove(this.size() - 1);
		return topObject;
	}
	
	/**
	 * Returns the last element placed on the stack but does not remove it from the stack.
	 * 
	 * @return last element placed on the stack
	 * @throws EmptyStackException if stack is empty
	 */
	public T peek() {
		if(this.isEmpty()) {
			throw new EmptyStackException("The stack is empty!");
		}
		
		return stack.get(this.size() - 1);
	}
	
	/**
	 * Removes all elements from the stack.
	 */
	public void clear() {
		stack.clear();
		return;
	}
	
}
