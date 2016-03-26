/**
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
public class ModifyGroupServlet extends HttpServlet {
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
			conn.setAutoCommit(false);

			user = CredentialHandler.getInstance().getSessionUserName(request, response);
			String sqlGroups = "";
				
			if(mode == 'add') {
				sqlGroups = "INSERT INTO grouplist values('" + gId + "', '" + friend + "', SYSDATE, null);";
			}
			else {
				sqlGroups = "DELETE FROM grouplist WHERE friendtoadd = '" + selUser + "';";
			}
				
			Statement state = conn.createStatement();
			state.executeUpdate(sqlGroups);
			conn.commit();
				
      RequestDispatcher rd = getServletContext().getRequestDispatcher("/addFriend.html");
      out.println("<font color=green>Your group has been modified.</font>");
      rd.include(request, response);

			DBHandler.getInstance().safeCloseConn(conn);

		} catch (SQLException sqle) {
			DBHandler.getInstance().safeCloseTrans(conn);
			
			RequestDispatcher rd = getServletContext().getRequestDispatcher("/addFriend.html");
		  out.println("<font color=red>Modification of your group failed.  Please try again.</font>");
		  rd.include(request, response);
		} catch (Exception ex) {
			out.println("<hr>" + ex.getMessage() + "<hr>");
		}
		
	}

}
