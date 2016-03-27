<!doctype html public "-//w3c//dtd html 4.0 transitional//en">

<html>
<head> 
    <title>Upload Images to Online Storage</title>
<!--    <%@ page import="java.util.*" %>
    <%@ page import="java.sql.*" %>
    <%@include file="../util/dbLogin.jsp"%>
    <%
        // Encode the successful redirect
        String encodeUpload = response.encodeURL("/proj1/uploading/UploadImage");
        String username = String.valueOf(session.getAttribute("username"));
        Statement stmt = conn.createStatement();
        ResultSet rset = null;
        String sql = "";
        
        // Initialize the list of group names and IDs, and add the defaults (public, private)
        ArrayList<String> group_names = new ArrayList<String>();
        ArrayList<String> group_ids = new ArrayList<String>();
        group_ids.add("1");
        group_ids.add("2");
        group_names.add("public");
        group_names.add("private");
        // Have to separate the following two to handle the case where the group is empty
        // Get the list of group ids where the user is the group owner
        sql = "select group_id from groups where user_name='" + username + "'";
        try {
            rset = stmt.executeQuery(sql);
            while (rset.next())
                group_ids.add(rset.getString("group_id"));
        } catch (Exception ex) {
            out.println("<hr>" + ex.getMessage() + "<hr>");
        }
        // Get the list of group ids where the user is a friend
        sql = "select group_id from group_lists where friend_id='" + username + "'";
        try {
            rset = stmt.executeQuery(sql);
            while (rset.next())
                group_ids.add(rset.getString("group_id"));
        } catch (Exception ex) {
            out.println("<hr>" + ex.getMessage() + "<hr>");
        }
        
        // Convert the list of group ids into group names
        for (int i = 2; i < group_ids.size(); i++) {
            sql = "select group_name from groups where group_id='" + group_ids.get(i) + "'";
            rset = stmt.executeQuery(sql);
            while (rset.next())
                group_names.add(rset.getString("GROUP_NAME"));
        }
    %>
    <%@include file="../util/dbLogout.jsp"%>-->
<link rel="stylesheet"
	href="//code.jquery.com/ui/1.10.4/themes/smoothness/jquery-ui.css">
<script src="//code.jquery.com/jquery-1.9.1.js"></script>
<script src="//code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
<link rel="stylesheet" href="/resources/demos/style.css">
<style>
.datepicker {
	
}
</style>
<script>
	$(function() {
		$(".datepicker").datepicker({
			changeMonth : true,
			changeYear : true
		});
	});
</script>
</head>
<body> 
	<%@ page import="java.sql.*"%>

	<%!
		public Connection getConnection(String oracleId,String oraclePassword){
			Connection con = null;
			String driverName = "oracle.jdbc.driver.OracleDriver";
			String dbstring = "jdbc:oracle:thin:@wang8.cs.ualberta.ca:16270:CRS";
			try{
				Class drvClass = Class.forName(driverName);
				DriverManager.registerDriver((Driver)drvClass.newInstance());
				con=DriverManager.getConnection(dbstring,oracleId,oraclePassword);
				con.setAutoCommit(true);
			}
			catch(Exception e){
			
			}
			return con;
		}
	%>
	<%
		String userName=null;
		if(request.getParameter("UploadRecord") != null && ((String)session.getAttribute("class"))!=null){
			userName = (String)session.getAttribute("USERNAME");
		}
		else{
			response.sendRedirect("Login.html");
		}
     %>
	<H1>
		<CENTER>
			<font color=Teal>Please Enter the information needs to be
				upload: </font>
		</CENTER>
	</H1>
	<BR></BR>
	<BR></BR>
	<h4>Upload Image below
	</h4>
	Please input or select the path of the image!
	<FROM name="upload-image" method="POST" enctype="multipart/form-data" action="servlet/UploadImageLogicSQL">
		<TABLE style="margin: 0px auto">
		<!-- <TR>
				<TD><B><I><font color=Maroon>Choose Patient: </font></I></B></TD>
				<TD>
					<SELECT NAME='patientID'>
					<%
						Connection con=getConnection((String)session.getAttribute("ORACLE_ID"),(String)session.getAttribute("ORACLE_PASSWORD"));
						Statement s=con.createStatement();
						String sql="SELECT * FROM persons p WHERE p.person_id=ANY(SELECT u.person_id FROM users u WHERE u.class='p')";
						ResultSet resSet=s.executeQuery(sql);
						while(resSet.next()){
							Integer id=resSet.getInt("person_id");
							String fname=resSet.getString("first_name");
							String lname=resSet.getString("last_name");
							out.println("<OPTION VALUE='"+id+"' SELECTED> "+fname+" "+lname+" ID: "+id+" </OPTION>");
						}
					%>
					</SELECT>
				</TD>
			</TR> -->
			<TR>
				<TD><B><I><font color=Maroon>Share with: </font></I></B></TD>
				<TD>
					<SELECT NAME='PERMITTED'>
					<%
						sql="SELECT group_name FROM groups g WHERE g.group_id=ANY"(SELECT gl.group_id FROM group_lists gl WHERE gl.friend_id = '" + user + "')";
						results.executeQuery(sql);
						while(results.next()){
							String group_name=results.getString("group_name");
						
							out.println("<OPTION VALUE='"+group_name+"' SELECTED> "+group_name+"  </OPTION>");
						}
						con.close();
					%>
					</SELECT>
				</TD>
			</TR>
		<!--	<TR>
				<TD><B><I><font color=Maroon>Test type: </font></I></B></TD>
				<TD><INPUT TYPE="text" NAME="    where the images were taken,
    when the images were taken,
    subject of the images,
    who shall be able to access the images.testType" VALUE=""></TD>
			</TR>-->
	
			<TR>
				<TD><B><I><font color=Maroon>Date: 
								(MM-DD-YYYY): </font></I></B></TD>
				<TD><p>
						<INPUT TYPE="text" class="date" NAME="DATE" VALUE="" />
					</p></TD>
			</TR>
			<TR>
				<TD><B><I><font color=Maroon>Location:  </font></I></B></TD>
				<TD><INPUT TYPE="text" NAME="LOCATION" VALUE=""></TD>
			</TR>
			<TR>
				<TD><B><I><font color=Maroon>Subject: </font></I></B></TD>
				<TD><INPUT TYPE="text" NAME="SUBJECT" VALUE=""></TD>
			</TR>
			<TR>
				<TD><B><I><font color=Maroon>Description: </font></I></B></TD>
				<TD><INPUT TYPE="text" NAME="DESCRIPTION" VALUE=""></TD>
			</TR>
  <tr>
    <th>File path: </th>
    <td><input name="file-path" type="file" size="30" ></input></td>
  </tr>
  <tr>
    <td ALIGN=CENTER COLSPAN="2"><input type="submit" name=".submit" 
     value="Upload"></td>
  </tr>

</table>


</html>

