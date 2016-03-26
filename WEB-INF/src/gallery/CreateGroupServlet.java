**
 * 
 */
package gallery;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author mcmorris
 *
 */
public class CreateGroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			// get request parameters for group name
	 	String gName = request.getParameter("GNAME");
		boolean valid = false;		
    
		PrintWriter out = response.getWriter();
		Connection conn = null;
		
		try {
			
			//establish connection to the underlying database
			conn = DBHandler.getInstance().getConnection();
			valid = isValidGroup(gName);
			if(valid) {
				conn.setAutoCommit(false);

				user = CredentialHandler.getInstance().getSessionUserName(request, response);
				String sqlGroups = "INSERT INTO group values('" + AUTOID + "', '" + user + "', '" + gName + "', SYSDATE);";
				
				Statement state = conn.createStatement();
				state.executeUpdate(sqlGroups);
				conn.commit();
				
	            RequestDispatcher rd = getServletContext().getRequestDispatcher("/addFriend.html");
	            out.println("<font color=green>Your group has been processed.  Please add friends to your groups now.</font>");
	            rd.include(request, response);
      }
			
			DBHandler.getInstance().safeCloseConn(conn);

		} catch (SQLException sqle) {
			DBHandler.getInstance().safeCloseTrans(conn);
		} catch (Exception ex) {
			out.println("<hr>" + ex.getMessage() + "<hr>");
		}
		
    if (valid == false) {
      RequestDispatcher rd = getServletContext().getRequestDispatcher("/addGroup.html");
      out.println("<font color=red>This group cannot be added.  Please try a different group.</font>");
      rd.include(request, response);
    }

	        
  }
	
	/*
	 * Check for valid group name.
	 */
	protected boolean isValidGroup(String gName) {
		boolean isValid = false;
		
		// TODO: Prevent name of 'public' and 'private'?
		isValid = (gName.isEmpty?() == false);
		
		return isValid;
	}


}
