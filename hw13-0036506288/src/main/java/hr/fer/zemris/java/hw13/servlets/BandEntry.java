package hr.fer.zemris.java.hw13.servlets;

/**
 * This is a Java Bean class which is used as a helper data
 * structure for handling operations used in our voting application.
 * 
 * @author Ivan Skorupan
 */
public class BandEntry {
	
	/**
	 * Band name.
	 */
	private String bandName;
	
	/**
	 * Link to the band's song.
	 */
	private String songLink;
	
	/**
	 * Number of votes for this band.
	 */
	private Integer numberOfVotes;

	/**
	 * Constructs a new {@link BandEntry} object.
	 * 
	 * @param bandName - name of band
	 * @param songLink - link to the song
	 * @param numberOfVotes - number of votes
	 */
	public BandEntry(String bandName, String songLink, Integer numberOfVotes) {
		this.bandName = bandName;
		this.songLink = songLink;
		this.numberOfVotes = numberOfVotes;
	}

	/**
	 * Getter for band name.
	 * 
	 * @return band name
	 */
	public String getBandName() {
		return bandName;
	}

	/**
	 * Setter for band name.
	 * 
	 * @param bandName - band name to be set
	 */
	public void setBandName(String bandName) {
		this.bandName = bandName;
	}

	/**
	 * Getter for band song link.
	 * 
	 * @return band song link
	 */
	public String getSongLink() {
		return songLink;
	}

	/**
	 * Setter for band song link.
	 * 
	 * @param songLink - band song link to be set
	 */
	public void setSongLink(String songLink) {
		this.songLink = songLink;
	}

	/**
	 * Getter for number of votes.
	 * 
	 * @return number of votes
	 */
	public Integer getNumberOfVotes() {
		return numberOfVotes;
	}

	/**
	 * Setter for number of votes.
	 * 
	 * @param numberOfVotes - number of votes to be set
	 */
	public void setNumberOfVotes(Integer numberOfVotes) {
		this.numberOfVotes = numberOfVotes;
	}
	
}
