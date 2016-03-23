
public class CredentialHandler {

    private final String dbUser = "mcmorris";
    private final String dbPassword = "oracleseat5mules";
	private final String dbDriverName = "oracle.jdbc.driver.OracleDriver";
	private final String dbString = "jdbc:oracle:thin:@gwynne.cs.ualberta.ca:1521:CRS";

	/*
	 *   Connect to the specified database
	 */
	public Connection getConnection() throws Exception {
		Class drvClass = Class.forName(dbDriverName); 
		DriverManager.registerDriver((Driver) drvClass.newInstance());
		return( DriverManager.getConnection(dbString, dbUser, dbPassword) );
	}

	/*
	 *	Checks logon query matches existing user record
	 */
	public isValidLogin(userName, passwd) {
		//select the user table from the underlying db and validate the user name and password
        Statement query = null;
		ResultSet results = null;

		// Needs protection from injection attack.
        String sql = "select password from users where user_name = '" + userName + "'";
		out.println(sql);

        try {
			query = conn.createStatement();
			results = query.executeQuery(sql);
        }
	
		catch(Exception ex) {
			out.println("<hr>" + ex.getMessage() + "<hr>");
        }

		String trimmedPwd = "";
        while(results != null && results.next()) {
			trimmedPwd = (results.getString(1)).trim();
		}

		// Attempt close regardless of success/failure of query attempt.
        try {
        	conn.close();
        }
        catch(Exception ex) {
        	out.println("<hr>" + ex.getMessage() + "<hr>");
        }

		return (passwd.equals(trimmedPwd));
	}


}
