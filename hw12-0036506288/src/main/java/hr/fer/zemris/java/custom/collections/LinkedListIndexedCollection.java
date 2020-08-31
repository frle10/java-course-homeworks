package hr.fer.zemris.java.custom.collections;

import java.util.ConcurrentModificationException;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * This class is an implementation of a linked list-backed collection of objects.
 * <p>
 * This collection <b>allows</b> duplicate elements. Each of these elements will be
 * held in a separate list node.
 * <p>
 * Storage of <code>null</code> references in this collection is <b>not</b> allowed.
 * 
 * @author Ivan Skorupan
 */
public class LinkedListIndexedCollection implements List {

	/**
	 * An integer variable containing the current number of objects in this list.
	 */
	private int size;
	
	/**
	 * A {@link ListNode} reference containing the first node in this list (the beginning of the list).
	 */
	private ListNode first;
	
	/**
	 * A {@link ListNode} reference containing the last node in this list (the end of the list).
	 */
	private ListNode last;
	
	/**
	 * Number of structural modifications made by the user on this collection.
	 */
	private long modificationCount;
	
	/**
	 * An implementation of {@link ElementsGetter} that fetches elements from
	 * {@link LinkedListIndexedCollection} type of objects.
	 * 
	 * @author Ivan Skorupan
	 */
	private static class LinkedListElementsGetter implements ElementsGetter {
		
		/**
		 * An outer {@link LinkedListIndexedCollection} reference whose elements to fetch.
		 */
		private LinkedListIndexedCollection list;
		
		/**
		 * Next node in the list whose value should be returned.
		 */
		private ListNode nextElement;
		
		/**
		 * Number of structural modifications made to the outer collection
		 * at the time of creation of this {@link LinkedListElementsGetter} object.
		 */
		private long savedModificationCount;
		
		/**
		 * Constructs an object of type {@link LinkedListElementsGetter}.
		 * 
		 * @param list - a list whose elements we will be returning
		 */
		public LinkedListElementsGetter(LinkedListIndexedCollection list) {
			this.list = Objects.requireNonNull(list);
			this.nextElement = list.first;
			this.savedModificationCount = list.modificationCount;
		}

		@Override
		public boolean hasNextElement() {
			if(savedModificationCount != list.modificationCount) {
				throw new ConcurrentModificationException();
			}
			
			return nextElement != null;
		}

		@Override
		public Object getNextElement() {
			if(hasNextElement()) {
				Object nextElementValue = nextElement.value;
				nextElement = nextElement.next;
				return nextElementValue;
			} else {
				throw new NoSuchElementException();
			}
		}
		
	}

	/**
	 * This class models a single list node.
	 * <p>
	 * One node contains references to its adjacent nodes (one for next node and one
	 * for previous node) and a value object that is stored inside it.
	 * 
	 * @author Ivan Skorupan
	 */
	private static class ListNode {
		/**
		 * A reference to the next node in the list.
		 */
		ListNode next;
		
		/**
		 * A reference to the previous node in the list.
		 */
		ListNode previous;
		
		/**
		 * An {@link Object} reference containing the data stored in this node.
		 */
		Object value;

		/**
		 * Initializes a new {@link ListNode} object.
		 * <p>
		 * Does <b>not</b> allow a <code>null</code> reference to be passed
		 * for <code>value</code>.
		 * 
		 * @param next - reference to the next node in the list
		 * @param previous - reference to the previous node in the list
		 * @param value - an object to be stored in this node
		 * @throws NullPointerException if <code>value</code> is <code>null</code>
		 */
		public ListNode(ListNode next, ListNode previous, Object value) {
			this.next = next;
			this.previous = previous;
			this.value = Objects.requireNonNull(value);
		}
	}

	/**
	 * This is the default constructor for this class.
	 * <p>
	 * It initializes a new {@link LinkedListIndexedCollection} object.
	 */
	public LinkedListIndexedCollection() {
		this.first = null;
		this.last = null;
		this.size = 0;
		this.modificationCount = 0;
	}

	/**
	 * Initializes a new {@link LinkedListIndexedCollection} object.
	 * <p>
	 * Copies all objects from <code>other</code> into this new list.
	 * <p>
	 * Collection <code>other</code> must be different from <code>null</code>.
	 * 
	 * @param other - a {@link Collection} object to use for initialization of this list.
	 * @throws NullPointerException if <code>other</code> is <code>null</code>
	 */
	public LinkedListIndexedCollection(Collection other) {
		this();
		this.addAll(Objects.requireNonNull(other));
	}

	@Override
	public int size() {
		return this.size;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * The average complexity of this method is O(1).
	 * @throws NullPointerException if value is <code>null</code>
	 */
	@Override
	public void add(Object value) {
		Objects.requireNonNull(value);

		if(size == 0) {
			first = new ListNode(null, null, value);
			last = first;
		} else {
			last.next = new ListNode(null, last, value);
			last = last.next;
		}

		size++;
		modificationCount++;
		return;
	}

	@Override
	public boolean contains(Object value) {
		if(value == null) {
			return false;
		}

		ListNode current = first;
		while(current != null) {
			if(current.value.equals(value)) {
				return true;
			}
			current = current.next;
		}

		return false;
	}

	/**
	 * Returns a list node object from the given position.
	 * <p>
	 * Implemented with complexity never greater than O(n/2 + 1).
	 * 
	 * @param index - position of the node to get
	 * @return a {@link ListNode} object on position <code>index</code>
	 */
	private ListNode getNode(int index) {
		if((index < 0) || (index > size - 1)) {
			throw new IndexOutOfBoundsException();
		}
		
		ListNode current = null;
		int currentIndex = 0;

		if((index <= size - index)) {
			current = first;
		} else {
			current = last;
			currentIndex = size - 1;
		}

		while(currentIndex != index) {
			if(currentIndex < index) {
				current = current.next;
				currentIndex++;
			} else {
				current = current.previous;
				currentIndex--;
			}
		}

		return current;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * Valid indexes are 0 to size - 1.
	 * <p>
	 * This method never has a complexity greater than O(n/2 + 1).
	 * 
	 * @throws IndexOutOfBoundsException if index is invalid.
	 */
	@Override
	public Object get(int index) {
		if((index < 0) || (index > size - 1)) {
			throw new IndexOutOfBoundsException();
		}

		return this.getNode(index).value;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * The average complexity of this method is O(n).
	 */
	@Override
	public int indexOf(Object value) {
		final int INDEX_NOT_FOUND = -1; // value to return if value was not found

		if(value == null) {
			return INDEX_NOT_FOUND;
		}

		ListNode current = first;
		int index = 0;
		while(current != null) {
			if(current.value.equals(value)) {
				return index;
			}

			current = current.next;
			index++;
		}

		return INDEX_NOT_FOUND;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * Valid positions are 0 to size.
	 * <p>
	 * The average complexity of this method is O(n/2 + 1).
	 * 
	 * @throws NullPointerException if <code>value</code> is <code>null</code>
	 * @throws IndexOutOfBoundsException if <code>position</code> is invalid
	 */
	@Override
	public void insert(Object value, int position) {
		Objects.requireNonNull(value);

		if((position < 0) || (position > size)) {
			throw new IndexOutOfBoundsException();
		}

		if(size == 0 || position == size) {
			this.add(value);
			return;
		}

		ListNode insertionPoint = this.getNode(position);
		insertElementAndLinkNodes(insertionPoint, value);
		modificationCount++;
		return;
	}

	/**
	 * This method creates a new {@link ListNode} object containing <code>value</code> and
	 * properly inserts it into this list by correctly linking the nodes before and after the new node
	 * to maintain list integrity and structure.
	 * 
	 * @param insertionPoint - list node at whose position to insert a new element
	 * @param value - an object to be inserted
	 */
	private void insertElementAndLinkNodes(ListNode insertionPoint, Object value) {
		/* Create a new node to insert */
		ListNode newNode = new ListNode(insertionPoint, insertionPoint.previous, value);

		/* Link nodes correctly */
		if(insertionPoint != first) {
			/* We are inserting a middle element in the list */
			insertionPoint.previous.next = newNode;
		} else {
			/* We are inserting the first element in the list */
			first = newNode;
		}

		insertionPoint.previous = newNode;
		size++;
	}

	/**
	 * This method removes the given {@link ListNode} object from this list
	 * and properly links the nodes before and after the removal point to maintain
	 * list integrity and structure.
	 * 
	 * @param removalPoint - list node to remove from this list
	 */
	private void removeElementAndLinkNodes(ListNode removalPoint) {
		/* Link nodes correctly */
		if(removalPoint != first && removalPoint != last) {
			/* We are removing a middle element in the list */
			removalPoint.previous.next = removalPoint.next;
			removalPoint.next.previous = removalPoint.previous;
		} else if(removalPoint == first && removalPoint != last) {
			/* We are removing the first element in the list */
			removalPoint.next.previous = null;
			first = removalPoint.next;
		} else if(removalPoint != first && removalPoint == last) {
			/* We are removing the last element in the list */
			removalPoint.previous.next = null;
			last = removalPoint.previous;
		}

		/* We always set the node we are removing to null so resources can be correctly released by garbage collector. */
		removalPoint = null;
		size--;
		modificationCount++;
		return;
	}

	@Override
	public boolean remove(Object value) {
		/* Null elements cannot exist in this list */
		if(value == null) {
			return false;
		}

		ListNode current = first; // initially set the current node we are at as the first element in the list
		while(current != null) { // loop until we reach the end of the list
			if(current.value.equals(value)) { // test if the node containing the corresponding value has been found
				removeElementAndLinkNodes(current);
				return true;
			}
			current = current.next;
		}

		return false;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * <p>
	 * Valid indexes are 0 to size - 1.
	 * 
	 * @throws IndexOutOfBoundsException if <code>index</code> is invalid
	 */
	@Override
	public void remove(int index) {
		if((index < 0) || (index > size - 1)) {
			throw new IndexOutOfBoundsException();
		}

		ListNode removalPoint = this.getNode(index);
		removeElementAndLinkNodes(removalPoint);
		return;
	}

	@Override
	public Object[] toArray() {
		Object[] arrayFromList = new Object[size];
		ListNode current = first;

		for(int i = 0; i < size; i++, current = current.next) {
			arrayFromList[i] = current.value;
		}

		return arrayFromList;
	}
	
	@Override
	public ElementsGetter createElementsGetter() {
		return new LinkedListElementsGetter(this);
	}
	
	@Override
	public void clear() {
		first = null;
		last = null;
		size = 0;
		modificationCount++;
		return;
	}

}
