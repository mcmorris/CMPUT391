/**
 * 
 */
package security;

import java.sql.*; 

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import session.CredentialHandler;
import session.DBHandler;

/**
 * @author mcmorris
 *
 */
public class SecurityHandler {
	private static SecurityHandler instance = null;

	protected SecurityHandler() {
		// Exists only to defeat instantiation.
	}
	public static SecurityHandler getInstance() {
		if(instance == null) {
			instance = new SecurityHandler();
		}
		return instance;
	}
	
	/*
	 * Executes query about whether a given picture is owned by current user. 
	 */
	public boolean isPicOwner(HttpServletRequest request, HttpServletResponse response, int pictureId) {
		//select the user table from the underlying db and validate the user name and password
        Statement query = null;
		ResultSet results = null;
		
		String trimmedOwnerName = "";
		Connection conn = null;
		boolean isOwner = false;
		
		try
		{
			String picSql = "select owner_name from images where photo_id = '" + pictureId + "'";
			String user = CredentialHandler.getInstance().getSessionUserName(request);
			
			conn = DBHandler.getInstance().getConnection();
			
			query = conn.createStatement();
			results = query.executeQuery(picSql);
			while(results != null && results.next()) {
				trimmedOwnerName = (results.getString(1)).trim();
			}
			
			isOwner = user.equals(trimmedOwnerName);
			
		}
		catch (SQLException sqlEx) {
			System.out.println("<hr>" + sqlEx.getMessage() + "<hr>");
		}
		catch (Exception ex) {
			System.out.println("<hr>" + ex.getMessage() + "<hr>");
		}
		
		DBHandler.getInstance().safeCloseConn(conn);
		return isOwner;
	}
	
	/*
	 * Executes query about whether a given group is owned by current user. 
	 */
	public boolean isGroupOwner(HttpServletRequest request, HttpServletResponse response, int groupId) {
		// Select the user table from the underlying DB and validate the user name and password
        Statement query = null;
		ResultSet results = null;
		
		String trimmedOwnerName = "";
		Connection conn = null;
		boolean isOwner = false;
		
		try
		{
			String groupSql = "select user_name from groups where group_id = '" + groupId + "'";
			String user = CredentialHandler.getInstance().getSessionUserName(request);
			
			conn = DBHandler.getInstance().getConnection();
			
			query = conn.createStatement();
			results = query.executeQuery(groupSql);
			while(results != null && results.next()) {
				trimmedOwnerName = (results.getString(1)).trim();
			}
			
			isOwner = user.equals(trimmedOwnerName);
			
		}
		catch (SQLException sqlEx) {
			System.out.println("<hr>" + sqlEx.getMessage() + "<hr>");
		}
		catch (Exception ex) {
			System.out.println("<hr>" + ex.getMessage() + "<hr>");
		}
		
		DBHandler.getInstance().safeCloseConn(conn);
		return isOwner;
	}

}