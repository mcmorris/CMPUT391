/**
 * 
 */
package com.cmput391.servlet.session;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class LoginServlet
 * @author mcmorris
 * Adapted from sources: http://www.journaldev.com/1907/java-servlet-session-management-tutorial-with-examples-of-cookies-httpsession-and-url-rewriting by Pankaj and https://webdocs.cs.ualberta.ca/~yuan/servlets/login.jsp
 */
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// get request parameters for userID and password
	 	String user = request.getParameter("USERID");
	 	String pwd = request.getParameter("PASSWD");
		boolean valid = false;		
    
		PrintWriter out = response.getWriter();
		
		try {
			//establish connection to the underlying database
			valid = CredentialHandler.getInstance().isValidLogin(user, pwd);
			if(valid) {
				CredentialHandler.getInstance().createSession(request, response, 30);
            	response.sendRedirect("LoginSuccess.jsp");
        	}
 
		}
		catch(Exception ex) {
			out.println("<hr>" + ex.getMessage() + "<hr>");
		}

        if (valid == false) {
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.html");
            out.println("<font color=red>There has been a mistake in your credential submission.  Please try again.</font>");
            rd.include(request, response);
        }
 
    }


 
}
