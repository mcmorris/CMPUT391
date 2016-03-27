<!-- Specifies a form for uploading images to the database -->
<!DOCTYPE html>
<html>
<head>
    <title>Upload Images to Online Storage</title>
    <%@ page import="java.util.*" %>
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
    <%@include file="../util/dbLogout.jsp"%>
    <link rel="stylesheet" type="text/css" href="/proj1/util/mystyle.css">
    <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
    <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
    <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
    <link rel="stylesheet" href="/resources/demos/style.css" />
    <script>
        $(function() {
            $( "#time" ).datepicker({
                defaultDate: "+1w",
                changeMonth: true,
                numberOfMonths: 1,
            });
        });
    </script>
</head>
<body>
<%@include file="../util/addHeader.jsp"%>
    <div id="container">
        <div id="subContainer" style="width:450px">
            <p class="homePage">Go back to <A class="homePage" href=<%=encodeHomePage%>>Home Page</a></p>
            <Fieldset>
                <legend>Upload Image(s)</legend>
                Please input or select the path of the image(s)
                <form name="upload-image" method="POST" enctype="multipart/form-data" action=<%=encodeUpload%>>
                <table>
                    <tr>
                        <th>File path(s): </th>
                        <td><input name="file-path" type="file" size="30" multiple></input></td>
                    </tr>
                    <tr>
                        <th>Description: </th>
                        <td><input name="description" type="textfield" value=""></td>
                    </tr>
                    <tr>
                        <th>Place: </th>
                        <td><input name="place" type="textfield" value=""></td>
                    </tr>
                    <tr>
                        <th>Subject: </th>
                        <td><input name="subject" type="textfield" value=""></td>
                    </tr>
                    <tr>
                        <th>Security: </th>
                        <td><select name="security">
                        <% // For each valid group_id and group_name for the user,
                           // add the group to the select menu
                            for (int i = 0; i < group_ids.size(); i += 1) { %>
                                <option value='<%=group_ids.get(i)%>'><%=group_names.get(i)%></option>
                            <%}%>
                        </select></td>
                    </tr>
                    <tr>
                        <th>Date: </th>
                        <td><input id="time" name="time" type="textfield" value=""></td>
                    </tr>
                    <tr>
                        <td><input type="submit" ID="buttonstyle" name=".submit" value="Upload"></td>
                    </tr>
                </table>
                </form>
            </fieldset>
        </div>
    </div>
</body>
</html>
