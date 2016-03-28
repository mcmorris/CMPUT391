<%@ page import="java.sql.*" %>
<%@ page import="session.*" %>
<%@ page import="model.Group*" %>

<HTML>
<HEAD>
<TITLE>User Groups</TITLE>
</HEAD>

<BODY>

<% 
	out.println("<UL>");
	out.println("<LI><a href=""login.html"">Logout</a></LI>");
	out.println("<LI><a href=""profile.html"">Profile</a></LI>");
	out.println("<LI><a href=""groups.jsp"">Privacy</a></LI>");
	out.println("<LI><a href=""upload.html"">Share</a></LI>");
	out.println("<LI><a href=""display.html"">Browse</a></LI>");
	out.println("<LI><a href=""search.html"">Search</a></LI>");
	out.println("<LI><a href=""analytics.html"">Analytics</a></LI>");
	out.println("</UL>");

	out.println("<h1>User Groups</h1>");
	try
	{
		//establish connection to the underlying database
		conn = DBHandler.getInstance().getConnection();
			 		
		String user = CredentialHandler.getInstance().getSessionUserName(request);
		if(user.isEmpty() == true)
		{
%>
<jsp:forward page="login.html" />
<%
		}
		
		Groups grps = new Groups();
		results = grps.getOwnedGroups(conn, user);
      		
		if (!rs.next()) {                            // Then there are no rows.
			System.out.println("<p>You do not own any groups.  Why not create one?</p>");
		} else {
			out.println("<table>");
			do {
				out.println("<tr>");
				// Get data from the current row and use it
				out.println("<td>");
				out.println(results.getInt(1));
				out.println("</td>");
				out.println(results.getString(2));
				out.println("</td>");
				out.println("<td>");
				out.println(results.getString(3));
				out.println("</td>");
				out.println("<td>");
				out.println(results.getString(4));
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
	
	out.println("<FORM NAME=""GroupServlet"" ACTION=""groupservlet"" METHOD=""post"">");
	out.println("<INPUT TYPE=""text"" NAME=""group"" VALUE=""group name""><br>");
	out.println("<INPUT TYPE=""radio"" name=""mode"" value=""add"">Create<br>");
  	out.println("<INPUT TYPE=""radio"" name=""mode"" value=""edit"">Modify<br>");
  	out.println("<INPUT TYPE=""submit"" NAME="".submit"" VALUE=""Submit""><br>");
	out.println("</FORM>");
	DBHandler.getInstance().safeCloseConn(conn);
%>

</BODY>
</HTML>
