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
public class Person {
	
  	// Add a profile to Person.
  	public void add(Connection conn, HttpServletRequest request) throws SQLException {
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
  	public ResultSet get(Connection conn, String user) throws SQLException {
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