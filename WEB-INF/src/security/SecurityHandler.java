/**
 * 
 */
package security;

import java.sql.*; 

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
		String user = request.getParameter("USERID");
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

}
