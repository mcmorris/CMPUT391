<!-- This module displays a single image and allows the user to
     edit/delete the image in the database.
     A lot of the code for the Edit Photo Information modal form was taken
     from http://jqueryui.com/dialog/#modal-form -->
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8" />
        <title>Image Display</title>
        <% // Get the current photo_id and session username
           String photo_id = request.getParameter("id");
           String username = String.valueOf(session.getAttribute("username"));
           // Outgoing link encodes
           String encodeEdit = response.encodeURL("/proj1/display/EditData");
           String encodePic = response.encodeURL("/proj1/display/pictureBrowse.jsp");
           String imgNotFoundEncode = response.encodeURL("/proj1/error/imgNotFound.jsp");
        %>
        <%@ page import="java.sql.*, java.text.*, java.util.*" %>
        <%@include file="../util/dbLogin.jsp"%>
        <%
           ResultSet rset = null;
           ResultSet rset2 = null;
           ResultSet rset3 = null;
           String sql = "";
           
           String description = "";
           String place = "";
           String owner_name = "";
           String subject = "";
           String timing = "";
           String permitted = "none";
           String group_id = "";
           // Initialize a list of group names and group IDs and add the defaults (public/private)
           ArrayList<String> group_names = new ArrayList<String>();
           ArrayList<String> group_ids = new ArrayList<String>();
           group_ids.add("1");
           group_ids.add("2");
           group_names.add("public");
           group_names.add("private");
           // Add user to picture_hit table if they aren't already in it
           try { 
               Statement hit_stmt = conn.createStatement();
               sql = "insert into picture_hits values("+photo_id+",'"+username+"')";
               hit_stmt.executeUpdate(sql);
           } catch (Exception ex) {
               // Value is already in the table, do nothing
           }
           // Get the description details associated with the image
           try {
               Statement stmt = conn.createStatement();
               rset = stmt.executeQuery("select * from images where photo_id="+photo_id);
           } catch (Exception ex) {
               out.println("<hr>" + ex.getMessage() + "<hr>");
           }
               
           // Assign the description details to variables
           if (rset.next()) {
               description = rset.getString("DESCRIPTION");
               place = rset.getString("PLACE");
               owner_name = rset.getString("OWNER_NAME");
               subject = rset.getString("SUBJECT");
               java.util.Date d_timing = rset.getDate("TIMING");
               SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
               timing = df.format(d_timing);
               group_id = rset.getString("PERMITTED");
           }
           else // User manually tried to access an image that doesn't exist
               response.sendRedirect(imgNotFoundEncode);
           // Translate the group_id to a group_name for display
           try { 
               Statement permitted_stmt = conn.createStatement();
               sql = "select group_name from groups where group_id="+group_id;
               rset2 = permitted_stmt.executeQuery(sql);
               if (rset2.next()) {
                   permitted = rset2.getString("GROUP_NAME");
               }
           } catch (Exception ex) {
               out.println("<hr>" + ex.getMessage() + "<hr>");
           }
           // Have to separate the following two to handle the case where the group is empty
           // Get the list of group ids where the user is the group owner
           Statement groups_stmt = conn.createStatement();
           sql = "select group_id from groups where user_name='" + username + "'";
           try {
               rset = groups_stmt.executeQuery(sql);
               while (rset.next())
                   group_ids.add(rset.getString("group_id"));
           } catch (Exception ex) {
               out.println("<hr>" + ex.getMessage() + "<hr>");
           }
           // Get the list of group ids where the user is a friend
           sql = "select group_id from group_lists where friend_id='" + username + "'";
           try {
               rset = groups_stmt.executeQuery(sql);
               while (rset.next())
                   group_ids.add(rset.getString("group_id"));
           } catch (Exception ex) {
               out.println("<hr>" + ex.getMessage() + "<hr>");
           }
           // Convert the list of group ids into group names
           for (int i = 2; i < group_ids.size(); i++) {
               sql = "select group_name from groups where group_id='" + group_ids.get(i) + "'";
               rset = groups_stmt.executeQuery(sql);
               while (rset.next())
                   group_names.add(rset.getString("GROUP_NAME"));
           }
        %>
        <%@include file="../util/dbLogout.jsp"%>

        <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
        <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
        <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
        <link rel="stylesheet" type="text/css" href="/proj1/util/mystyle.css">
        <script>
        // Javascript for displaying the Edit Image Data modal form
        // Lots of the code was inspired from http://jqueryui.com/dialog/#modal-form
         $(function() {
             $( "#edit-form" ).dialog({
                 autoOpen: false,
                 height: 620,
                 width: 400,
                 modal: true,
                 buttons: {
                     "Update Information": function() {
                         // Use ajax to send values to EditData servlet
                         var new_description = $("#description_field").val();
                         var new_groups = $("#groups_field").val();
                         var new_place = $("#place_field").val();
                         var new_subject = $("#subject_field").val();
                         var new_time = $("#time_field").val();
                         $.ajax({url: '/proj1/display/EditData',
                                 data: {"description": new_description,
                                        "groups": new_groups,
                                        "place": new_place,
                                        "subject": new_subject,
                                        "time": new_time,
                                        "id": <%= photo_id %>
                                        },
                                 async: false,
                                 type: 'POST'
                                });
                         location.reload();
                     },
                     Cancel: function() {
                         $(this).dialog("close");
                     }
                 },
                 close: function() {
                     // After closing the modal window, reset the values 
                     // for the next time it opens
                     $("#description_field").val('<%= description %>');
                     $("#place_field").val('<%= place %>');
                     $("#subject_field").val('<%= subject %>');
                     $("#time_field").val('<%= timing %>');
                 }
             });
             $( "#edit-info" )
             .button()
             .click(function() {
                 $( "#edit-form" ).dialog( "open" );
             });
        // Javascript for the calendar app
             $( "#time_field" ).datepicker({
                defaultDate: "+1w",
                changeMonth: true,
                numberOfMonths: 1,
             });
         });
        // Javascript for deleting an image, using the DeleteImage servlet
        function deleteImage() {
            if (confirm("Are you sure you want to delete this image?") == true) {
                $.ajax({url: '/proj1/display/DeleteImage',
                        data: {"id": <%= photo_id %>},
                        async: false,
                        type: 'POST'
                       });
                // After a successful delete, go back to the pictureBrowse.jsp module
                window.location.replace("<%= encodePic %>");
            }
        }
        </script>
    </head>
    <body>
        <%@include file="../util/addHeader.jsp"%>
        <div id="container">
        <p class='homePage'>Go back to <A class='homePage' href='<%= encodeHomePage %>'>Home Page</a></p>
        <center>
            <img src="/proj1/display/GetOnePic?big<%= photo_id %>">
            <div id="info">
                <p>Description: <%= description %>
                <br>Place: <%= place %>
                <br>Owner: <%= owner_name %>
                <br>Subject: <%= subject %>
                <br>Groups: <%= permitted %>
                <br>Time photo taken: <%= timing %></p>
            </div>
           <%
           if(username.equals(owner_name)) { %>
               <button id=edit-info>Edit Photo Information</button>
               <button id="buttonstyle" onclick="deleteImage()">Delete Photo</button>
         <% } %>
            <form action=<%=encodePic%>>
                <input type='submit' ID="buttonstyle" value='Return to Pictures'>
            </form>
        </center>

        <!-- HTML for the Edit-Data form -->
        <div id="edit-form" title="Edit Photo Information">
            <p class="intro">Edit any subset of the fields and click 'submit'.</p>
            <form method="POST" action=<%=encodeEdit%>>
                <fieldset>
                    <TABLE>
                       <TR VALIGN=TOP ALIGN=LEFT>
                          <TD>
                             <label for="description_field">Description</label>
                          </TD>
                       </TR>
                       <TR VALIGN=TOP ALIGN=LEFT>
                          <TD>
                             <input type='text' name='description_field' id='description_field' value='<%= description %>' class='text ui-widget-content ui-corner-all' />
                          </TD>
                       </TR>
                       <TR VALIGN=TOP ALIGN=LEFT>
                          <TD>
                             <label for="place_field">Place</label>
                          </TD>
                       </TR>
                       <TR VALIGN=TOP ALIGN=LEFT>
                          <TD>
                             <input type='text' name='place_field' id='place_field' value='<%= place %>' class='text ui-widget-content ui-corner-all' />
                          </TD>
                       </TR>
                       <TR VALIGN=TOP ALIGN=LEFT>
                          <TD>
                             <label for="subject_field">Subject</label>
                          </TD>
                       </TR>
                       <TR VALIGN=TOP ALIGN=LEFT>
                          <TD>
                             <input type='text' name='subject_field' id='subject_field' value='<%= subject %>' class='text ui-widget-content ui-corner-all' />
                          </TD>
                       </TR>
                       <TR VALIGN=TOP ALIGN=LEFT>
                          <TD>
                             <label for="groups_label">Groups</label>
                          </TD>
                       </TR>
                       <TR VALIGN=TOP ALIGN=LEFT>
                          <TD>
                             <select name="security" id="groups_field">
                             <% // Only allow the user to select groups that they are a part of
                                for (int i = 0; i < group_ids.size(); i += 1) {
                                   if (group_names.get(i).equals(permitted)) {
                                      out.println("<option selected='true' value='"+group_ids.get(i) +
                                      "'>"+group_names.get(i)+"</option>");
                                   } else {
                                      out.println("<option value='" + group_ids.get(i) +
                                      "'>"+group_names.get(i)+"</option>");
                                   }
                                }
                             %>
                             </select>
                          </TD>
                       </TR>
                       <TR VALIGN=TOP ALIGN=LEFT>
                          <TD>
                             <label for="time_field">Time photo taken</label>
                          </TD>
                       </TR>
                       <TR VALIGN=TOP ALIGN=LEFT>
                          <TD>
                             <input type='text' name='time_field' id='time_field' value='<%= timing %>' class='text ui-widget-content ui-corner-all' />
                          </TD>
                       </TR>
                    </TABLE>
                </fieldset>
            </form>
        </div>
        </div>
    </body>
</html>
