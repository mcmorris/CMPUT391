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
public class Profile {
	
  	// Add a profile to Profile.
  	public void add(Connection conn, Request request) {
	  if (conn == null) return;
		
		String user = request.getParameter("USERID");
		String fName = request.getParameter("FNAME");
		String lName = request.getParameter("LNAME");
		String address = request.getParameter("ADDRESS");
		String email = request.getParameter("EMAIL");
		String phone = request.getParameter("PHONE");
		
		PreparedStatement pstmt = conn.prepareStatement("INSERT INTO persons values(?, ?, ?, ?, ?, ?);");
		pstmt.setString(1, user);
		pstmt.setString(2, fName);
		pstmt.setString(3, lName);
		pstmt.setString(4, address);
		pstmt.setString(5, email);
		pstmt.setString(6, phone);
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
