package hr.fer.zemris.java.hw14;

/**
 * Models an entry in table "polls".
 * <p>
 * Each poll entry has an id, title and a message.
 * 
 * @author Ivan Skorupan
 */
public class PollEntry {
	
	/**
	 * This poll's id.
	 */
	private long id;
	
	/**
	 * This poll's title.
	 */
	private String title;
	
	/**
	 * This poll's message.
	 */
	private String message;
	
	/**
	 * Constructs a new {@link PollEntry} object.
	 */
	public PollEntry() {
	}
	
	/**
	 * Getter for this poll's id.
	 * 
	 * @return poll id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Setter for this poll's id.
	 * 
	 * @param id - poll id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Getter for this poll's title.
	 * 
	 * @return poll title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Setter for this poll's title.
	 * 
	 * @param title - poll title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Getter for this poll's message.
	 * 
	 * @return poll message
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Setter for this poll's message.
	 * 
	 * @param message - poll message
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
}
