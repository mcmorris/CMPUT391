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

public class Picture {
  
  UPLOAD FILES
  {
    UPLOAD FILE
    ADD(FILE)
    DELETE FILE
  }
  
  // Add a group to groups.
  public void add(Connection conn, HttpRequest request) {
	  if (conn == null) return;
		
		// isThumbnail - inStreamThumbNail
		// isImage - inStreamImage
		
		PreparedStatement pstmt = conn.prepareStatement("insert into photos values(0, ?, ?, ?, ?, ?, ?, ?, ?)");
		pstmt.setString(1, owner_name);
		pstmt.setInt(2, permitted);
		pstmt.setString(3, subject);
		pstmt.setString(4, place);
		pstmt.setDate(5, timing);
		pstmt.setString(6, description);
		pstmt.setBinaryStream(7, isThumbnail, (int)size);
		pstmt.setBinaryStream(8, isPhoto, (int)size);
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
