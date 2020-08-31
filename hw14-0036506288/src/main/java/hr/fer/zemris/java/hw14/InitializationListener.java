package hr.fer.zemris.java.hw14;

import java.beans.PropertyVetoException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Properties;
import java.util.Set;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.DataSources;

/**
 * This listener makes sure that upon the application start a databse connection
 * is established and a connection pool is created.
 * <p>
 * Also, the listener tests if the database has necessary tables. If that is not
 * the case, the tables are created and filled with initial data.
 * <p>
 * Upon application exit, the connection pool is destroyed and all resources
 * appropriately released.
 * 
 * @author Ivan Skorupan
 */
@WebListener
public class InitializationListener implements ServletContextListener {

	/**
	 * Name of polls table in the database.
	 */
	private static final String POLLS_TABLE_NAME = "polls";
	
	/**
	 * Name of poll options table in the database.
	 */
	private static final String POLL_OPTIONS_TABLE_NAME = "polloptions";

	@Override
	public void contextInitialized(ServletContextEvent sce) {
		String dbSettingsPath = sce.getServletContext().getRealPath("/WEB-INF/dbsettings.properties");
		Properties properties = new Properties();
		try {
			properties.load(Files.newInputStream(Paths.get(dbSettingsPath)));
		} catch (IOException e) {
			e.printStackTrace();
		}

		String connectionURL = "jdbc:derby://" + properties.getProperty("host") + ":" +
				properties.getProperty("port") + "/" + properties.getProperty("name");

		ComboPooledDataSource cpds = new ComboPooledDataSource();
		try {
			cpds.setDriverClass("org.apache.derby.jdbc.ClientDriver");
		} catch (PropertyVetoException e) {
			throw new RuntimeException("Failure during pool initialization.", e);
		}
		cpds.setJdbcUrl(connectionURL);
		cpds.setUser(properties.getProperty("user"));
		cpds.setPassword(properties.getProperty("password"));

		try {
			createTables(cpds);
		} catch (SQLException e) {
			e.printStackTrace();
		}

		sce.getServletContext().setAttribute("hr.fer.zemris.dbpool", cpds);
	}

	/**
	 * Helper method which creates necessary tables in the database
	 * in case they do not already exist and then fills them with
	 * data.
	 * 
	 * @param cpds - a data source object which provides a connection to the database
	 * @throws SQLException if there is a problem processing SQL commands or methods
	 */
	private void createTables(ComboPooledDataSource cpds) throws SQLException {
		Connection con = cpds.getConnection();
		DatabaseMetaData dmd = con.getMetaData();
		ResultSet rs = dmd.getTables(null, null, null, new String[] {"TABLE"});

		Set<String> tables = new HashSet<>();
		while(rs.next()) {
			tables.add(rs.getString("TABLE_NAME").toLowerCase());
		}

		if(!tables.contains(POLLS_TABLE_NAME)) {
			try {
				Statement stmt = con.createStatement();
				stmt.executeUpdate("CREATE TABLE Polls\r\n" + 
						" (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,\r\n" + 
						" title VARCHAR(150) NOT NULL,\r\n" + 
						" message CLOB(2048) NOT NULL)");
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}

		if(!tables.contains(POLL_OPTIONS_TABLE_NAME)) {
			try {
				Statement stmt = con.createStatement();
				stmt.executeUpdate("CREATE TABLE PollOptions\r\n" + 
						" (id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,\r\n" + 
						" optionTitle VARCHAR(100) NOT NULL,\r\n" + 
						" optionLink VARCHAR(150) NOT NULL,\r\n" + 
						" pollID BIGINT,\r\n" + 
						" votesCount BIGINT,\r\n" + 
						" FOREIGN KEY (pollID) REFERENCES Polls(id))");
			} catch(SQLException e) {
				e.printStackTrace();
			}
		}

		int bandPollID = -1;
		int tvShowPollID = -1;
		boolean isEmpty = !executeRowCount(con, POLLS_TABLE_NAME);
		if(isEmpty) {
			bandPollID = insertPoll(con, "Glasanje za omiljeni bend", "Od sljedećih bendova, koji Vam je bend najdraži? Kliknite na link kako biste glasali!");
			tvShowPollID = insertPoll(con, "Glasanje za najbolju seriju", "Klikom na link odaberite Vašu najdražu seriju:");
		}

		isEmpty = !executeRowCount(con, POLL_OPTIONS_TABLE_NAME);
		if(isEmpty) {
			insertPollOption(con, "Coldplay", "https://www.youtube.com/watch?v=RB-RcX5DS5A", bandPollID, 0);
			insertPollOption(con, "AC/DC", "https://www.youtube.com/watch?v=pAgnJDJN4VA", bandPollID, 0);
			insertPollOption(con, "Guns N' Roses", "https://www.youtube.com/watch?v=1w7OgIMMRc4", bandPollID, 0);
			insertPollOption(con, "Imagine Dragons", "https://www.youtube.com/watch?v=7wtfhZwyrcc", bandPollID, 0);
			insertPollOption(con, "U2", "https://www.youtube.com/watch?v=98W9QuMq-2k", bandPollID, 0);
			insertPollOption(con, "Arctic Monkeys", "https://www.youtube.com/watch?v=bpOSxM0rNPM", bandPollID, 0);
			insertPollOption(con, "Red Hot Chili Peppers", "https://www.youtube.com/watch?v=YlUKcNNmywk", bandPollID, 0);
			
			insertPollOption(con, "Doctor Who", "https://www.youtube.com/watch?v=Pa74e8oAvIM", tvShowPollID, 0);
			insertPollOption(con, "Supernatural", "https://www.youtube.com/watch?v=4CeFXaQqI5Y", tvShowPollID, 0);
			insertPollOption(con, "Star Trek", "https://www.youtube.com/watch?v=xfDpDCsULn0", tvShowPollID, 0);
		}

		try { con.close(); } catch(SQLException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Inserts a poll option inside the {@link #POLL_OPTIONS_TABLE_NAME} table.
	 * <p>
	 * Each option has its id, title, link, pollID and number of votes.
	 * 
	 * @param con - connection object to use for communicating with the database
	 * @param title - option title
	 * @param link - option link
	 * @param pollID - poll id this option belongs to
	 * @param votesCount - number of votes for this option
	 */
	private void insertPollOption(Connection con, String title, String link, int pollID, int votesCount) {
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement("INSERT INTO " + POLL_OPTIONS_TABLE_NAME + "(optionTitle, optionLink, pollID, votesCount) values (?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, title);
			pst.setString(2, link);
			pst.setInt(3, pollID);
			pst.setInt(4, votesCount);
			pst.executeUpdate();
		} catch(SQLException ex) {
			ex.printStackTrace();
		} finally {
			try { pst.close(); } catch(SQLException ex) {
				ex.printStackTrace();
			}
		}
	}

	/**
	 * Inserts a new poll in {@link #POLLS_TABLE_NAME} table.
	 * <p>
	 * Each poll has its id, title and message.
	 * 
	 * @param con - connection object to use for communicating with the database
	 * @param title - poll's title
	 * @param message - poll's message
	 * @return the auto-generated id for the poll that was just inserted
	 */
	private int insertPoll(Connection con, String title, String message) {
		PreparedStatement pst = null;
		int pollID = -1;
		try {
			pst = con.prepareStatement("INSERT INTO " + POLLS_TABLE_NAME + "(title, message) values (?,?)", Statement.RETURN_GENERATED_KEYS);
			pst.setString(1, title);
			pst.setString(2, message);
			pst.executeUpdate();
			
			ResultSet rset = pst.getGeneratedKeys();
			pollID = (rset.next()) ? rset.getInt(1) : -1;
		} catch(SQLException ex) {
			ex.printStackTrace();
		} finally {
			try { pst.close(); } catch(SQLException ex) {
				ex.printStackTrace();
			}
		}

		return pollID;
	}

	/**
	 * Calculates the number of rows in <code>table</code> and
	 * then returns a boolean result indicating if there are any entries
	 * in the table.
	 * 
	 * @param con - connection object to use for communicating with the database
	 * @param table - table name whose rows to count
	 * @return <code>true</code> if entries exist in <code>table</code>, <codde>false</code> otherwise
	 */
	private boolean executeRowCount(Connection con, String table) {
		PreparedStatement pst = null;
		boolean resultsExist = true;
		try {
			pst = con.prepareStatement("SELECT COUNT(*) FROM " + table);
			
			ResultSet rset = pst.executeQuery();
			if(rset.next()) {
				int numberOfRows = rset.getInt(1);
				resultsExist = (numberOfRows == 0) ? false : true;
			}
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			try { pst.close(); } catch(SQLException ex) {
				ex.printStackTrace();
			}
		}

		return resultsExist;
	}

	@Override
	public void contextDestroyed(ServletContextEvent sce) {
		ComboPooledDataSource cpds = (ComboPooledDataSource)sce.getServletContext().getAttribute("hr.fer.zemris.dbpool");
		if(cpds!=null) {
			try {
				DataSources.destroy(cpds);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

}