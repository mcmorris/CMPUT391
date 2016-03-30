/**
 * 
 */
package session;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import model.User;
import model.Person;

/**
 * @author mcmorris
 *
 */
public class RegisterServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		Connection conn = null;
		boolean valid = false;
		
		try {
			//establish connection to the underlying database
			conn = DBHandler.getInstance().getConnection();
			conn.setAutoCommit(false);
			
			User u = new User();
			u.add(conn, request);
			
			String user = request.getParameter("USERID");
			String pwd = request.getParameter("PASSWD");
			
			valid = u.isAvailable(conn, user, pwd);
			if(valid) {
				Person p = new Person();
				u.add(conn, request);
				p.add(conn, request);
				conn.commit();
				
				RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.html");
				out.println("<font color=green>Your registration has been processed.  Please login now.</font>");
				rd.include(request, response);
			}
			
			DBHandler.getInstance().safeCloseTrans(conn);
		
		} catch (SQLException sqle) {
			DBHandler.getInstance().safeCloseConn(conn);
		} catch (Exception ex) {
			out.println("<hr>" + ex.getMessage() + "<hr>");
		}
		
	    if (valid == false) {
	    	RequestDispatcher rd = getServletContext().getRequestDispatcher("/register.html");
	        out.println("<font color=red>The credentials in your registration request cannot be used.  Please try again.</font>");
	        rd.include(request, response);
	    }
	        
    }

}