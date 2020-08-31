package hr.fer.zemris.java.hw14;

/**
 * Models an entry in table "polloptions".
 * <p>
 * Each poll option has its id, title, link, pollID and number of votes.
 * 
 * @author Ivan Skorupan
 */
public class PollOptionsEntry {
	
	/**
	 * This poll option's id.
	 */
	private long id;
	
	/**
	 * This poll option's title.
	 */
	private String optionTitle;
	
	/**
	 * This poll option's link.
	 */
	private String optionLink;
	
	/**
	 * This poll option's pollID.
	 */
	private long pollID;
	
	/**
	 * This poll option's number of votes.
	 */
	private long votesCount;
	
	/**
	 * Constructs a new {@link PollOptionsEntry} object.
	 */
	public PollOptionsEntry() {
	}

	/**
	 * Getter for this poll option's id.
	 * 
	 * @return poll option id
	 */
	public long getId() {
		return id;
	}

	/**
	 * Setter for this poll option's id.
	 * 
	 * @param id - poll option id
	 */
	public void setId(long id) {
		this.id = id;
	}

	/**
	 * Getter for this poll option's title.
	 * 
	 * @return poll option title
	 */
	public String getOptionTitle() {
		return optionTitle;
	}

	/**
	 * Setter for this poll option's title.
	 * 
	 * @param optionTitle - poll option title
	 */
	public void setOptionTitle(String optionTitle) {
		this.optionTitle = optionTitle;
	}

	/**
	 * Getter for this poll option's link.
	 * 
	 * @return poll option link
	 */
	public String getOptionLink() {
		return optionLink;
	}

	/**
	 * Setter for this poll option's link.
	 * 
	 * @param optionLink - poll option link
	 */
	public void setOptionLink(String optionLink) {
		this.optionLink = optionLink;
	}

	/**
	 * Getter for this poll option's poll id.
	 * 
	 * @return poll option poll id
	 */
	public long getPollID() {
		return pollID;
	}

	/**
	 * Setter for this poll option's poll id.
	 * 
	 * @param pollID - poll option poll id
	 */
	public void setPollID(long pollID) {
		this.pollID = pollID;
	}

	/**
	 * Getter for this poll option's number of votes.
	 * 
	 * @return poll option's number of votes
	 */
	public long getVotesCount() {
		return votesCount;
	}

	/**
	 * Setter for this poll option's number of votes.
	 * 
	 * @param votesCount - poll option's number of votes
	 */
	public void setVotesCount(long votesCount) {
		this.votesCount = votesCount;
	}
	
}
