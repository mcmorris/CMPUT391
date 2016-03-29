<%@ page import="java.sql.*" %>
<%@ page import="session.*" %>
<%@ page import="model.Profile*" %>

<HTML>
<HEAD>
<TITLE>User Profile</TITLE>
</HEAD>

<BODY>
<UL>
	<LI><a href="login.html">Logout</a></LI>
	<LI><a href="profile.jsp>Profile</a></LI>
	<LI><a href="groups.jsp">Privacy</a></LI>
	<LI><a href="upload.html">Share</a></LI>
	<LI><a href="display.html">Browse</a></LI>
	<LI><a href="search.html">Search</a></LI>
	<LI><a href="analytics.html">Analytics</a></LI>
</UL>
<h1>User Profile</h1>
<% 
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
		
		Profile p = new Profile();
		results = p.get(conn, user);
		
		if (!rs.next()) {                            // Then there are no rows, in this case, should not be possible.
			System.out.println("<p>Your profile does not exist.  Please contact admin, this should not have occurred.</p>");
		} else {
			out.println("<h2>" + results.getString(1) + "</h2><br>");
			out.println("Name: " + results.getString(2) + " " + results.getString(3) + "<br>");
			out.println("Address: " + results.getString(4) + "<br>");
			out.println("Email: " + results.getString(5) + "<br>");
			out.println("Phone: " + results.getString(6) + "<br>");
		}
		
	} catch(SqlException sqlEx){
		out.println("<hr>" + sqlEx.getMessage() + "<hr>");
	} catch(Exception ex){
		out.println("<hr>" + ex.getMessage() + "<hr>");
	}
	
	DBHandler.getInstance().safeCloseConn(conn);
%>
<br>
<a href="groups.jsp">Change your privacy settings here</a>
<br>

</BODY>
</HTML>
