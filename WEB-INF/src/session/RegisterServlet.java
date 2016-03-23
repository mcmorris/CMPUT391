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

/**
 * @author mcmorris
 *
 */
public class RegisterServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// get request parameters for userID and password
	 	String user = request.getParameter("USERID");
	 	String pwd = request.getParameter("PASSWD");
	 	String fName = request.getParameter("FNAME");
	 	String lName = request.getParameter("LNAME");
	 	String address = request.getParameter("ADDRESS");
	 	String email = request.getParameter("EMAIL");
	 	String phone = request.getParameter("PHONE");
	 	
		boolean valid = false;		
    
		PrintWriter out = response.getWriter();
		Connection conn = null;
		
		try {
			
			//establish connection to the underlying database
			conn = CredentialHandler.getInstance().getConnection();
			valid = isValidRegistration(user, pwd);
			if(valid) {
				conn.setAutoCommit(false);
				String sqlUsers = "INSERT INTO users values('" + user + "', '" + pwd + "', SYSDATE);";
				String sqlPersons = "INSERT INTO persons values('" + user + "', '" + fName + "', '" + lName + "', '" + address + "', '" + email + "', '" + phone + "');";
				
				Statement state = conn.createStatement();
				state.executeUpdate(sqlUsers);
				state.executeUpdate(sqlPersons);
				conn.commit();
				
	            RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.html");
	            out.println("<font color=green>Your registration has been processed.  Please login now.</font>");
	            rd.include(request, response);
			}
			
			conn.close();

		// Must get rid of the fugliness...
		} catch (SQLException sqle) {
			try {
				if (conn != null) { 
					conn.rollback();
					conn.close();
				}
			} catch (SQLException sqle1) {
				out.println("<hr>" + sqle1.getMessage() + "<hr>");
			}
		} catch (Exception ex) {
			out.println("<hr>" + ex.getMessage() + "<hr>");
		}
		
        if (valid == false) {
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/register.html");
            out.println("<font color=red>The credentials in your registration request cannot be used.  Please try again.</font>");
            rd.include(request, response);
        }
 
    }
	
	/*
	 * Check username/password do not match an existing account.
	 */
	protected boolean isValidRegistration(String user, String pass) {
		boolean isValid = false;
		
		try {
			isValid = CredentialHandler.getInstance().isValidLogin(user, pass);
		}
		catch(Exception ex) {
			System.out.println("<hr>" + ex.getMessage() + "<hr>");
		}		
		
		return isValid;
	}


     
}
