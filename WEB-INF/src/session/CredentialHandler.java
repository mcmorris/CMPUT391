/**
 * 
 */
package session;

import java.io.IOException;
import java.sql.*; 

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author mcmorris
 *
 */
public class CredentialHandler {
	private static CredentialHandler instance = null;

	protected CredentialHandler() {
		// Exists only to defeat instantiation.
	}
	public static CredentialHandler getInstance() {
		if(instance == null) {
			instance = new CredentialHandler();
		}
		return instance;
	}
	
	/*
	 * Create a new session with user from request
	 */
	protected void createSession(HttpServletRequest request, HttpServletResponse response, int timeoutMin) {
		String user = request.getParameter("USERID");

		HttpSession session = request.getSession();
		session.setAttribute("user", user);
		session.setMaxInactiveInterval(timeoutMin*60);
		Cookie userName = new Cookie("user", user);
		response.addCookie(userName);
		return;
	}

	/*
	 * End an existing session with user from request
	 */
	protected void endSession(HttpServletRequest request, HttpServletResponse response) {
		response.setContentType("text/html");
	        Cookie[] cookies = request.getCookies();
	        if(cookies != null) {
		        for(Cookie cookie : cookies) {
		            if(cookie.getName().equals("JSESSIONID")) {
		                System.out.println("JSESSIONID="+cookie.getValue());
		                break;
		            }
		            
		            // Send response invalidating our own cookies.
		            cookie.setMaxAge(0);
		            response.addCookie(cookie);
		        }
	        }
	        
	        //invalidate the session if exists
	        HttpSession session = request.getSession(false);
	        System.out.println("User="+session.getAttribute("user"));
	        if(session != null) {
	            session.invalidate();
	        }
	}
	
	/*
	 *	Checks logon query matches existing user record
	 */
	public boolean isValidLogin(String userName, String passwd) throws Exception, SQLException {
		//select the user table from the underlying db and validate the user name and password
        	Statement query = null;
		ResultSet results = null;

		// Needs protection from injection attack.
        	String sql = "select password from users where user_name = '" + userName + "'";

		String trimmedPwd = "";
		Connection conn = null;		
		
		conn = DBHandler.getInstance().getConnection();
		conn.setAutoCommit(false);

		query = conn.createStatement();
		results = query.executeQuery(sql);
		while(results != null && results.next()) {
			trimmedPwd = (results.getString(1)).trim();
		}

		DBHandler.getInstance().safeCloseConn(conn);
		return (passwd.equals(trimmedPwd));
	}
	
	/*
	 * Checks user has established session, otherwise kicks back to login page.
	 */
	public boolean credentialCheck(HttpServletRequest request, HttpServletResponse response) throws IOException {
	        HttpSession session = request.getSession(false);
	    	if(session.getAttribute("user") == null) {
	    	    response.sendRedirect("login.html");
	    	    return false;
	    	}
	    	
	    	return true;
	}
	
	/*
	 * Gets user name of current session user.
	 */
	public String getSessionUserName(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String userName = null;
		if (credentialCheck(request, response) == true) {
			HttpSession session = request.getSession(false);
			userName = (String) session.getAttribute("user");
			
			// Take value from cookie if available.
			Cookie[] cookies = request.getCookies();
			if(cookies !=null) {
				for(Cookie cookie : cookies) {
				    if(cookie.getName().equals("user")) userName = cookie.getValue();
				}
			}
		}
		
		return userName;
	}
	

}
