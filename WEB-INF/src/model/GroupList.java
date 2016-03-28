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
public class GroupList {
  
  // Add an entry to GroupList 
  public void add(Connection conn, int groupId, String friend) {
	  if (conn == null) return;
		
		// Real world, would check to ensure a group with this name for this user does not already exist, and that group is not named "private" or "public".  Not included here, not in assignment specs.
		PreparedStatement pstmt = conn.prepareStatement("INSERT INTO group_lists values(?, ?, SYSDATE, null);");
    pstmt.setInt(1, groupId);
    pstmt.setString(2, friend);
    
		pstmt.executeUpdate();
  }
  
  // Remove an entry from GroupList 
  public void remove(Connection conn, int groupId, String friend) {
	  if (conn == null) return;
		
		// Real world, would check to ensure a group with this name for this user does not already exist, and that group is not named "private" or "public".  Not included here, not in assignment specs.
		PreparedStatement pstmt = conn.prepareStatement("DELETE FROM grouplist WHERE group_id = ? AND friend_id = ?;");
    pstmt.setInt(1, groupId);
    pstmt.setString(2, friend);
    
		pstmt.executeUpdate();
  }
  
  // Get an entry from GroupList.
  public ResultSet get(Connection conn, int groupId) {
    ResultSet results = null;
    
    if (conn != null) {
      PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM group_lists gl WHERE gl.group_id = ?;");
      pstmt.setInt(1, groupId);
      
      results = pstmt.executeQuery();
    }
    
    return results;
  }
  
  // Get all GroupList entries containing user.
  public ResultSet getGroups(Connection conn, String curUser) {
    ResultSet results = null;
    
    if (conn != null) {
      PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM groups g WHERE g.group_id = ANY(SELECT gl.group_id FROM group_lists gl WHERE gl.friend_id = ?);");
      pstmt.setString(1, curUser);

      results = pstmt.executeQuery();
    }
    
    return results;
  }
  
  // Get all owned GroupList entries.
  public ResultSet getOwnedGroups(Connection conn, String curUser) {
    ResultSet results = null;
    
    if (conn != null) {
      PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM groups g WHERE g.user_name = ?;");
      pstmt.setString(1, curUser);
      
      results = pstmt.executeQuery();
    }
    
    return results;
  }  

}
