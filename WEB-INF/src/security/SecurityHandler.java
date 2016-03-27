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
	 * Create a new session with user from request
	 */
	protected boolean isPermitted(HttpServletRequest request, HttpServletResponse response, int pictureId) {
		boolean permitted = false;
		
		//select the user table from the underlying db and validate the user name and password
        Statement query = null;
		ResultSet results = null;

		// Needs protection from injection attack.
        String picSql = "select owner_name, permitted from images where photo_id = '" + pictureId + "'";
        
		String trimmedOwnerName = "";
		String trimmedGroupId = "";
		Connection conn = null;		
		
		try
		{
			String user = CredentialHandler.getInstance().getSessionUserName(request, response);
			if (user.equals("admin") == true) return true; 		// Admin gets special privileges.
			
			conn = DBHandler.getInstance().getConnection();
			
			query = conn.createStatement();
			results = query.executeQuery(picSql);
			while(results != null && results.next()) {
				trimmedOwnerName = (results.getString(1)).trim();
				trimmedGroupId = (results.getString(2)).trim();
			}
			
			int groupId = Integer.parseInt(trimmedGroupId);
			
			// Public means always true.
			if (groupId == 1) permitted = true;
			
			// Private means only true if creator.
			else if (groupId == 2) {
				permitted = user.equals(trimmedOwnerName);
			}
			
			// Otherwise, is custom group, true if user is on group_lists as friend_id
			else {
				// Needs protection from injection attack.
		        String isPermittedSql = "select * from images where group_id = '" + groupId + "' and friend_id = '" + user + "';";
	
				conn = DBHandler.getInstance().getConnection();
	
				query = conn.createStatement();
				results = query.executeQuery(isPermittedSql);
				if(results != null && results.next()) {
					permitted = true;
				}
				
			}
			
		}
		catch (SQLException sqlEx) {
			System.out.println("<hr>" + sqlEx.getMessage() + "<hr>");
		}
		catch (Exception ex) {
			System.out.println("<hr>" + ex.getMessage() + "<hr>");
		}
		
		DBHandler.getInstance().safeCloseConn(conn);
		return permitted;
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
			String user = CredentialHandler.getInstance().getSessionUserName(request, response);
			
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
			String user = CredentialHandler.getInstance().getSessionUserName(request, response);
			
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
