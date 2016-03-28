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
public class Group {
  
  // Add a group to groups.
  public void add(Connection conn, String gName, String curUser) {
	  if (conn == null) return;
		
		// Real world, would check to ensure a group with this name for this user does not already exist, and that group is not named "private" or "public".  Not included here, not in assignment specs.
		PreparedStatement pstmt = conn.prepareStatement("INSERT INTO group values(0, ?, ?, SYSDATE);");
		pstmt.setString(1, gName);
		pstmt.setString(2, curUser);
    
		pstmt.executeUpdate();
  }
  
  // Get a group from groups.
  public ResultSet get(Connection conn, int groupId) {
    ResultSet results = null;
    
    if (conn != null) {
      PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM groups WHERE group_id = ?;");
      pstmt.setInt(1, groupId);
      
      results = pstmt.executeQuery();
    }
    
    return results;
  }
  
    // Get a group from groups.
  public ResultSet getByName(Connection conn, String name) {
    ResultSet results = null;
    
    if (conn != null) {
      PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM groups WHERE group_name = ?;");
      pstmt.setString(1, name);
      
      results = pstmt.executeQuery();
    }
    
    return results;
  }
  
  // Get owner groups from groups.
  public ResultSet getGroups(Connection conn, String curUser) {
    ResultSet results = null;
    
    if (conn != null) {
      PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM groups WHERE user_name = ?;");
      pstmt.setString(1, curUser);
      
      results = pstmt.executeQuery();
    }
    
    return results;
  }
  
  // Update and delete group not in assignment specs, and so are not included here.  We had enough concerns to manage as is.
  
}
