
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*; 

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class CredentialHandler {
    private static String dbUser = "mcmorris";
    private static String dbPassword = "oracleseat5mules";
	private static String dbDriverName = "oracle.jdbc.driver.OracleDriver";
	private static String dbString = "jdbc:oracle:thin:@gwynne.cs.ualberta.ca:1521:CRS";
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
	 *   Connect to the specified database
	 */
	public Connection getConnection() throws Exception {
		Class drvClass = Class.forName(dbDriverName); 
		DriverManager.registerDriver((Driver) drvClass.newInstance());
		return( DriverManager.getConnection(dbString, dbUser, dbPassword) );
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
		
		conn = this.getConnection();
		conn.setAutoCommit(false);

		query = conn.createStatement();
		results = query.executeQuery(sql);
		while(results != null && results.next()) {
			trimmedPwd = (results.getString(1)).trim();
		}

		if (conn != null) conn.close();
		return (passwd.equals(trimmedPwd));
	}


}
