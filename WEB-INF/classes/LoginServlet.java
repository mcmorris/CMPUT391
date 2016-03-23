package com.journaldev.servlet.session;
 
import java.io.IOException;
import java.io.PrintWriter;
 
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
 
/**
 * Servlet implementation class LoginServlet
 * Adapted from sources: http://www.journaldev.com/1907/java-servlet-session-management-tutorial-with-examples-of-cookies-httpsession-and-url-rewriting by Pankaj and https://webdocs.cs.ualberta.ca/~yuan/servlets/login.jsp
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// get request parameters for userID and password
	 	String user = request.getParameter("USERID");
	 	String pwd = request.getParameter("PASSWD");
		    
		//establish connection to the underlying database
		Connection conn = null;
	
		try {
			conn = ConnectionHandler.getConnection();
			conn.setAutoCommit(false);
		}
		catch(Exception ex) {
			out.println("<hr>" + ex.getMessage() + "<hr>");
		}

        if(CredentialHandler.isValidLogin(user, pwd)) {
            HttpSession session = request.getSession();
            session.setAttribute("user", "Pankaj");
            //setting session to expiry in 30 mins
            session.setMaxInactiveInterval(30*60);
            Cookie userName = new Cookie("user", user);
            userName.setMaxAge(30*60);
            response.addCookie(userName);
            response.sendRedirect("LoginSuccess.jsp");
        }else{
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.html");
            PrintWriter out = response.getWriter();
            out.println("<font color=red>Either user name or password is wrong.</font>");
            rd.include(request, response);
        }
 
    }

	protected boolean attemptLogin() {
		
	}
 
}
