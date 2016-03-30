/**
 * 
 */
package security;

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
import model.Group;

/**
 * @author mcmorris
 *
 */
public class GroupServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		
		out.println("oh burn your face!");
	}
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// get request parameters for group name
		String gName = request.getParameter("group");
		String mode = request.getParameter("mode");
		boolean valid = false;
		
		PrintWriter out = response.getWriter();
		Connection conn = null;
		int gId = -1;
		
		try {
			//establish connection to the underlying database
			conn = DBHandler.getInstance().getConnection();
			String user = CredentialHandler.getInstance().getSessionUserName(request);
			
			valid = isValidGroup(gName);
			if(valid) 
			{
				Group grp = new Group();
				if (mode == "add") {
					grp.add(conn, gName, user);
					
					RequestDispatcher rd = getServletContext().getRequestDispatcher("/groups.jsp");
					out.println("<font color=green>Your new group has been created.</font>");
					rd.include(request, response);
				}
				else if (mode == "update")
				{
					ResultSet results = grp.getByName(conn, gName);
					if (results.next()) {                            // Then there are rows.
						gId = results.getInt(1);
					}
					
					// Only modify valid group Ids.
					if (gId > 0) {
						RequestDispatcher rd = getServletContext().getRequestDispatcher("/friend.jsp?gid=" + gId);
						rd.include(request, response);
					}
				}
				
			}
			
			DBHandler.getInstance().safeCloseConn(conn);
		
		} catch (SQLException sqle) {
			DBHandler.getInstance().safeCloseConn(conn);
		} catch (Exception ex) {
			out.println("<hr>" + ex.getMessage() + "<hr>");
		}
		
		if (valid == false) {
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/addGroup.html");
			out.println("<font color=red>This group cannot be added.  Please try a different group name.</font>");
			rd.include(request, response);
		}
		
	}
	
	/*
	 * Check for valid group name.
	 */
	protected boolean isValidGroup(String gName) {
		boolean isValid = false;
		
		// TODO: Prevent name of 'public' and 'private'?
		isValid = (gName.isEmpty() == false);
		
		return isValid;
	}

}