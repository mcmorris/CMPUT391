/**
 * 
 */
package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;

/**
 * @author mcmorris
 *
 */
public class User {

	/*
	 * Add a profile to Person.
	 */
	public void add(Connection conn, HttpServletRequest request) throws SQLException {
	    	if (conn == null) return;
			
		String user = request.getParameter("USERID");
		String pwd = request.getParameter("PASSWD");
		
		PreparedStatement pstmt = conn.prepareStatement("INSERT INTO users values(?, ?, SYSDATE);");
		pstmt.setString(1, user);
		pstmt.setString(2, pwd);
		pstmt.executeUpdate();
	}
	
	/*
	 * Check username/password do not match an existing account.
	 */
	public boolean isAvailable(Connection conn, String userName, String passwd) throws Exception, SQLException {
		ResultSet results = null;
		boolean available = false;
		
		if (conn != null) {
			PreparedStatement pstmt = conn.prepareStatement("COUNT(*) FROM users WHERE user_name = ?;");
			pstmt.setString(1, userName);
			
			results = pstmt.executeQuery();
			if (results != null && results.getInt(1) == 0) {
				available = true;
			}
		}
		
		return available;
	}
	
	// Update and delete group not in assignment specs, and so are not included here.  We had enough concerns to manage as is.
	
}