/**
 * 
 */
package session;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * @author mcmorris
 *
 */
public class DBHandler {
	private static String dbUser = "mcmorris";
    private static String dbPassword = "oracleseat5mules";
	private static String dbDriverName = "oracle.jdbc.driver.OracleDriver";
	private static String dbString = "jdbc:oracle:thin:@gwynne.cs.ualberta.ca:1521:CRS";
	private static DBHandler instance = null;

	protected DBHandler() {
		// Exists only to defeat instantiation.
	}
	public static DBHandler getInstance() {
		if(instance == null) {
			instance = new DBHandler();
		}
		return instance;
	}

	/*
	 *   Connect to the specified database
	 */
	public Connection getConnection() throws Exception {
		@SuppressWarnings("rawtypes")
		Class drvClass = Class.forName(dbDriverName); 
		DriverManager.registerDriver((Driver) drvClass.newInstance());
		return( DriverManager.getConnection(dbString, dbUser, dbPassword) );
	}

	/*
	 *   Start a connection for use of upload.
	 */
	public Connection startUpload() throws Exception {
		Connection conn = getConnection();
		conn.setAutoCommit(false);
		return conn;
	}

	/*
	 *   End connection for use with upload, commit transaction or rollback as needed.
	 */	
	public void endUpload(Connection conn) {
		if (conn == null) return;
		
		try {
			conn.commit();
		} catch (SQLException ex) {
			DBHandler.getInstance().safeCloseTrans(conn);
		}
	}

	/*
	 *	 Safely close a connection with running transaction.
	 */
	public void safeCloseTrans(Connection conn) {
		if (conn == null) return;
	
		try {
			if (conn != null) { 
				conn.rollback();
				conn.close();
			}
		}
		// Catch rollback / close error.
		catch (SQLException sqle) {
			System.out.println("<hr>" + sqle.getMessage() + "<hr>");
		}
		// Regardless of rollback, attempt close.
		finally 
		{
			safeCloseConn(conn);
		}
			
	}
	
	public void safeCloseConn(Connection conn) {
		if (conn == null) return;
		
		try {
			conn.close();
		}
		// Close can still fail, check for failure.
		catch (SQLException sqle) {
			System.out.println("<hr>" + sqle.getMessage() + "<hr>");
		}
	}
}