package hr.fer.zemris.java.hw14.dao;

import java.util.List;

import hr.fer.zemris.java.hw14.PollEntry;
import hr.fer.zemris.java.hw14.PollOptionsEntry;

/**
 * Interface towards a subsystem for data persistence.
 * 
 * @author Ivan Skorupan
 */
public interface DAO {
	
	/**
	 * This method fetches all polls available in this voting application
	 * as a list of {@link PollEntry} objects.
	 * 
	 * @return a list of polls as {@link PollEntry} objects
	 */
	public List<PollEntry> fetchAllPolls();
	
	/**
	 * This method fetches a poll with the given <code>id</code>.
	 * 
	 * @param id - id of a poll that needs to be fetched
	 * @return a {@link PollEntry} object representing the poll with id equal to <code>id</code>
	 */
	public PollEntry fetchPollByID(long id);
	
	/**
	 * This method fetches all poll options that belong to a poll specified by its <code>pollID</code>.
	 * 
	 * @param pollID - the id of a poll that is used to filter the poll options to be fetched
	 * @return a list of {@link PollOptionsEntry} objects that contains all poll options that belong to a poll
	 * with id equal to <code>pollID</code>
	 */
	public List<PollOptionsEntry> fetchPollOptionsByPollID(long pollID);
	
	/**
	 * This method fetches a poll option with given <code>id</code>.
	 * 
	 * @param id - id of a poll option to be fetched
	 * @return a {@link PollOptionsEntry} object representing the poll with id equal to <code>id</code>
	 */
	public PollOptionsEntry fetchPollOptionByID(long id);
	
	/**
	 * This method sets the given number of votes for a given poll option.
	 * <p>
	 * The values used are <code>id</code> for identifying the poll option to update
	 * and <code>votesCount</code> as a number to set as the new number of votes for
	 * the specified poll option.
	 * 
	 * @param id - id of a poll option to be updated
	 * @param votesCount - new number of votes for specified poll option
	 */
	public void setVotesCountForOption(long id, long votesCount);
	
}