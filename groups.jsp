<%@ page import="java.sql.*" %>
<%@ page import="session.*" %>
<%@ page import="model.Group*" %>

<HTML>
<HEAD>
<TITLE>User Groups</TITLE>
</HEAD>

<BODY>

<% 
	out.println("<h1>User Groups</h1>");
    
	try
	{
		//establish connection to the underlying database
		conn = DBHandler.getInstance().getConnection();
		conn.setAutoCommit(false);
			 		
		String user = CredentialHandler.getInstance().getSessionUserName(request);
    
		Groups grps = new Groups();
		results = grps.getOwnedGroups(conn, user);
      
		if (!rs.next()) {                            // Then there are no rows.
			System.out.println("<p>Not a member of any user groups.</p>");
		} else {
			out.println("<table>");
			do {
				out.println("<tr>");
				// Get data from the current row and use it
				out.println("<td>");
				out.println(results.getInt("group_id"));
				out.println("</td>");
				out.println(results.getString("user_name"));
				out.println("</td>");
				out.println("<td>");
				out.println(results.getString("group_name"));
				out.println("</td>");
				out.println("<td>");
				out.println(results.getString("date_created"));
				out.println("</td>");
        
				out.println("</tr>");
			} while (rs.next());
			out.println("<table>");
		}
	
	} catch(SqlException sqlEx){
		out.println("<hr>" + sqlEx.getMessage() + "<hr>");
	} catch(Exception ex){
		out.println("<hr>" + ex.getMessage() + "<hr>");
	}
	
	DBHandler.getInstance().safeCloseConn(conn);
	
%>

</BODY>
</HTML>
