package hr.fer.zemris.java.custom.scripting.exec;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * This data structure can be thought of as a {@link Map}, but a special
 * kind of map. This collection allows the user to store multiple values
 * for the same key and it provides a stack-like abstraction.
 * <p>
 * Keys of this collection are instances of class {@link String} and values
 * associated with those keys are instances of class {@link ValueWrapper}.
 * <p>
 * This collection is not parameterized.
 * <p>
 * Informally, we can imagine that for every distinct string key, there is a
 * independent stack behind it. Stacks that belong to different keys are
 * completely isolated from each other.
 * 
 * @author Ivan Skorupan
 * @see ValueWrapper
 */
public class ObjectMultistack {
	
	/**
	 * Internal map that maps each key string to a head of its stack.
	 */
	private Map<String, MultistackEntry> virtualStacks;
	
	/**
	 * Constructs a new {@link ObjectMultistack} object.
	 * <p>
	 * This constructor initializes the internal (key, value) map to
	 * an empty {@link HashMap}.
	 */
	public ObjectMultistack() {
		this.virtualStacks = new HashMap<>();
	}
	
	/**
	 * Pushes a new value to the top of the stack that's mapped to the given
	 * <code>keyName</code>.
	 * <p>
	 * Neither the <code>keyName</code> nor <code>valueWrapper</code> can be <code>null</code>.
	 * 
	 * @param keyName - the key to whose stack to push the value
	 * @param valueWrapper - value to be pushed to a virtual stack mapped under <code>keyName</code>
	 * @throws NullPointerException if <code>keyName</code> or <code>valueWrapper</code> is <code>null</code>
	 */
	public void push(String keyName, ValueWrapper valueWrapper) {
		Objects.requireNonNull(keyName);
		Objects.requireNonNull(valueWrapper);
		
		MultistackEntry virtualStackHead = virtualStacks.get(keyName);
		
		if(virtualStackHead == null) {
			virtualStacks.put(keyName, new MultistackEntry(valueWrapper, null));
			return;
		}
		
		while(virtualStackHead.next != null) {
			virtualStackHead = virtualStackHead.next;
		}
		
		virtualStackHead.next = new MultistackEntry(valueWrapper, null);
	}
	
	/**
	 * Removes and returns the value from the top of the stack mapped under
	 * given <code>keyName</code>.
	 * <p>
	 * The <code>keyName</code> cannot be <code>null</code>.
	 * 
	 * @param keyName - key whose virtual stack to pop the value from
	 * @return removed value from top of virtual stack for given key
	 * @throws NullPointerException if <code>keyName</code> is <code>null</code>
	 * @throws EmptyStackException if the stack under <code>keyName</code> is empty
	 */
	public ValueWrapper pop(String keyName) {
		Objects.requireNonNull(keyName);
		
		MultistackEntry virtualStackHead = virtualStacks.get(keyName);
		ValueWrapper poppedValue = null;
		
		if(virtualStackHead == null) {
			throw new EmptyStackException();
		}
		
		if(virtualStackHead.next == null) {
			return virtualStacks.remove(keyName).value;
		}
		
		while(virtualStackHead.next.next != null) {
			virtualStackHead = virtualStackHead.next;
		}
		
		poppedValue = virtualStackHead.next.value;
		virtualStackHead.next = null;
		return poppedValue;
	}
	
	/**
	 * Returns the value on top of virtual stack under given <code>keyName</code>,
	 * but does not remove it.
	 * 
	 * @param keyName - key whose stack to peek at
	 * @return value on top of stack for given key
	 * @throws NullPointerException if <code>keyName</code> is <code>null</code>
	 * @throws EmptyStackException if stack under <code>keyName</code> is empty
	 */
	public ValueWrapper peek(String keyName) {
		Objects.requireNonNull(keyName);
		
		MultistackEntry virtualStackHead = virtualStacks.get(keyName);
		
		if(virtualStackHead == null) {
			throw new EmptyStackException();
		}
		
		while(virtualStackHead.next != null) {
			virtualStackHead = virtualStackHead.next;
		}
		
		return virtualStackHead.value;
	}
	
	/**
	 * Tests if a virtual stack under given <code>keyName</code>
	 * is empty.
	 * 
	 * @param keyName - key whose stack to test for emptiness
	 * @return <code>true</code> if the stack under <code>keyName</code> is empty, <code>false</code> otherwise
	 * @throws NullPointerException if <code>keyName</code> is <code>null</code>
	 */
	public boolean isEmpty(String keyName) {
		Objects.requireNonNull(keyName);
		return virtualStacks.get(keyName) == null;
	}
	
	/**
	 * Models values (entries) for internal map of {@link ObjectMultistack}.
	 * <p>
	 * This entry model acts as a single-linked list whose nodes contain a value
	 * and a reference to the next node in the list.
	 * <p>
	 * That kind of implementation enables the use of {@link MultistackEntry} as
	 * a virtual stack and {@link ObjectMultistack} as a map of virtual stacks.
	 * 
	 * @author Ivan Skorupan
	 */
	private static class MultistackEntry {
		
		/**
		 * Value of this node.
		 */
		private ValueWrapper value;
		
		/**
		 * Reference to the next entry in the list.
		 */
		private MultistackEntry next;
		
		/**
		 * Constructs a new {@link MultistackEntry} object.
		 * <p>
		 * Both <code>value</code> and <code>next</code> can be <code>null</code>.
		 * 
		 * @param value - this node's value
		 * @param next - reference to the next node after this one
		 */
		public MultistackEntry(ValueWrapper value, MultistackEntry next) {
			this.value = value;
			this.next = next;
		}
	}
	
}
