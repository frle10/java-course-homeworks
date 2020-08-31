package hr.fer.zemris.java.hw14.dao.sql;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import hr.fer.zemris.java.hw14.PollEntry;
import hr.fer.zemris.java.hw14.PollOptionsEntry;
import hr.fer.zemris.java.hw14.dao.DAO;
import hr.fer.zemris.java.hw14.dao.DAOException;

/**
 * This is an implementation of DAO subsystem using SQL technology. This
 * concrete implementation expects that a database connection is available
 * through {@link SQLConnectionProvider} class, which means that someone
 * else must set a connection there before the execution comes to this point.
 * <p>
 * In web-applications a typical solution is to configure a single filter
 * that will intercept servlet calls and before that put a single connection
 * here from a connection pool and upon the end of processing will remove it.
 * 
 * @author Ivan Skorupan
 */
public class SQLDAO implements DAO {
	
	@Override
	public List<PollEntry> fetchAllPolls() {
		List<PollEntry> polls = new ArrayList<>();
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement("SELECT id, title, message FROM polls ORDER BY id");
			try {
				ResultSet rs = pst.executeQuery();
				try {
					while(rs != null && rs.next()) {
						PollEntry poll = new PollEntry();
						poll.setId(rs.getLong(1));
						poll.setTitle(rs.getString(2));
						poll.setMessage(rs.getString(3));
						polls.add(poll);
					}
				} finally {
					try { rs.close(); } catch(Exception ignorable) {}
				}
			} finally {
				try { pst.close(); } catch(Exception ignorable) {}
			}
		} catch(Exception e) {
			throw new DAOException("Failure while fetching the list of poll entries!", e);
		}
		
		return polls;
	}

	@Override
	public PollEntry fetchPollByID(long id) {
		PollEntry poll = null;
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement("SELECT id, title, message FROM polls WHERE id=?");
			pst.setLong(1, id);
			try {
				ResultSet rs = pst.executeQuery();
				try {
					while(rs != null && rs.next()) {
						poll = new PollEntry();
						poll.setId(rs.getLong(1));
						poll.setTitle(rs.getString(2));
						poll.setMessage(rs.getString(3));
					}
				} finally {
					try { rs.close(); } catch(Exception ignorable) {}
				}
			} finally {
				try { pst.close(); } catch(Exception ignorable) {}
			}
		} catch(Exception e) {
			throw new DAOException("Failure while fetching the list of poll entries!", e);
		}
		
		return poll;
	}

	@Override
	public List<PollOptionsEntry> fetchPollOptionsByPollID(long pollID) {
		List<PollOptionsEntry> pollOptions = new ArrayList<>();
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement("SELECT id, optionTitle, optionLink, pollID, votesCount FROM polloptions WHERE pollID=? ORDER BY id");
			pst.setLong(1, pollID);
			try {
				ResultSet rs = pst.executeQuery();
				try {
					while(rs != null && rs.next()) {
						PollOptionsEntry pollOption = new PollOptionsEntry();
						pollOption.setId(rs.getLong(1));
						pollOption.setOptionTitle(rs.getString(2));
						pollOption.setOptionLink(rs.getString(3));
						pollOption.setPollID(pollID);
						pollOption.setVotesCount(rs.getLong(5));
						pollOptions.add(pollOption);
					}
				} finally {
					try { rs.close(); } catch(Exception ignorable) {}
				}
			} finally {
				try { pst.close(); } catch(Exception ignorable) {}
			}
		} catch(Exception e) {
			throw new DAOException("Failure while fetching the list of poll entries!", e);
		}
		
		return pollOptions;
	}

	@Override
	public PollOptionsEntry fetchPollOptionByID(long id) {
		PollOptionsEntry pollOption = null;
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement("SELECT id, optionTitle, optionLink, pollID, votesCount FROM polloptions WHERE id=?");
			pst.setLong(1, id);
			try {
				ResultSet rs = pst.executeQuery();
				try {
					while(rs != null && rs.next()) {
						pollOption = new PollOptionsEntry();
						pollOption.setId(rs.getLong(1));
						pollOption.setOptionTitle(rs.getString(2));
						pollOption.setOptionLink(rs.getString(3));
						pollOption.setPollID(rs.getLong(4));
						pollOption.setVotesCount(rs.getLong(5));
					}
				} finally {
					try { rs.close(); } catch(Exception ignorable) {}
				}
			} finally {
				try { pst.close(); } catch(Exception ignorable) {}
			}
		} catch(Exception e) {
			throw new DAOException("Failure while fetching the list of poll entries!", e);
		}
		
		return pollOption;
	}

	@Override
	public void setVotesCountForOption(long id, long votesCount) {
		Connection con = SQLConnectionProvider.getConnection();
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement("UPDATE polloptions SET votesCount=? WHERE id=?");
			pst.setLong(1, votesCount);
			pst.setLong(2, id);
			try {
				pst.executeUpdate();
			} finally {
				try { pst.close(); } catch(Exception ignorable) {}
			}
		} catch(Exception e) {
			throw new DAOException("Failure while fetching the list of poll entries!", e);
		}
	}
	
}