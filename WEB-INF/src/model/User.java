/**
 * 
 */
package model;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import session.CredentialHandler;
import session.DBHandler;

/**
 * @author mcmorris
 *
 */
public class User {
    // Add a profile to Profile.
    public void add(Connection conn, Request request) {
    if (conn == null) return;
		
		String user = request.getParameter("USERID");
		String pwd = request.getParameter("PASSWD");
    
		PreparedStatement pstmt = conn.prepareStatement("INSERT INTO users values(?, ?, SYSDATE);");
		pstmt.setString(1, user);
		pstmt.setString(2, pwd);
		pstmt.executeUpdate();
	}
  
	// Get a person from persons.
  	public ResultSet get(Connection conn, String user) {
		ResultSet results = null;
		
		if (conn != null) {
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM persons WHERE user_name = ?;");
			pstmt.setString(1, user);
			
			results = pstmt.executeQuery();
		}
		
		return results;
	}
	
	// Update and delete group not in assignment specs, and so are not included here.  We had enough concerns to manage as is.
	
}
