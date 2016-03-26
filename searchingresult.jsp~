<!-- Module which handles the logic of searching the database and displaying
     the appropriate images from the information specified by the user in the 
     search.jsp form -->
<!DOCTYPE html>
<html>
<head>
    <title>Search Result</title>
    <link rel="stylesheet" type="text/css" href="/proj1/util/mystyle.css">
    <p>&nbsp;</p>
<%  
    //If there is such attribute as username, this means the user entered 
    //this page through correct navigation (logging in) and is 
    //supposed to be here.
    String session_user = "";
    if(request.getSession(false).getAttribute("username") != null) {
        session_user = String.valueOf(session.getAttribute("username"));
        String encode = response.encodeURL("/proj1/user_management/logout.jsp"); 
        String encode2 = response.encodeURL("/proj1/util/userdoc.jsp"); %>
        <p id='username'>You are logged in as <%= session_user %></p>
        <a id='signout' href='<%= response.encodeUrl(encode) %>'>(Logout)</a>
        <a id='userdoc' href='<%= response.encodeUrl(encode2) %>'>Help Menu</a>
 <% } else {
        //If user entered this page without logging in or after logging out, 
        //redirect user back to main.jsp
        response.sendRedirect("/proj1/user_management/main.jsp");
    }
    //Encode the homePage link
    //String encodeHomePage = response.encodeURL("/proj1/home.jsp");
%>
</head>
<body> 
<%@ page import="java.sql.*, java.text.*, java.util.*" %>
    <div id="container">
    <%
        //The list of valid picture ids that the specific user is allowed to view
        List<String> valid_ids = new ArrayList<String>();
        String pic_id="";
        String owner_name="";
        String group_owner="";
        boolean is_friend=false;
        int permitted =0;
        
        Statement stmt1 = conn.createStatement();
        Statement stmt2 = conn.createStatement();
        
        //imgOption=0 represents "default display image option based on score"
        //imgOption=1 represents "most recent first"
        //imgOption=2 represents "most recent last"
        int imgOption=0;
        
        //dateFlag is 0 if the user did not specify a time constraint in the search
        //dateFlag is 1 if the user did specify a time constraint in the search
        int dateFlag=0;
        
        String dFrom="";
        String dTo="";
        String from="";
        String to="";
        
        String username = String.valueOf(session.getAttribute("username"));
        
        //Check for both empty query and empty time constraint
        int check=0;
        
        //If the user did enter a time constraint
        if (request.getParameter("from")!="" && request.getParameter("to")!="") {
            dateFlag=1;
            dFrom = request.getParameter("from");
            dTo = request.getParameter("to");
            
            //Convert string to java.util.date format and then back into 
            //the correct string date format for comparison
            SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
            java.util.Date parsedf = format.parse(dFrom);
            java.util.Date parsedt = format.parse(dTo);
            DateFormat df = new SimpleDateFormat("dd-MMM-yy");  
            from = df.format(parsedf);
            to = df.format(parsedt);
        }
        out.println("<p class='homePage'>Go back to <A class='homePage' href='" + 
                    response.encodeURL("/proj1/home.jsp")+"'>Home Page</a></p>");
        out.println("<center>");
        out.println("<h2>Search Result of '"+request.getParameter("query")+"'</h2>");
        
        //Attempting to search for the pictures
        try {
            //If the user clicked the button search
            if(request.getParameter("dateSubmit") != ""){
                
                PreparedStatement doSearch=null;
                
                //Checking what display image option the user chose
                String displayImgValue = request.getParameter("rank");
                if(displayImgValue.equals("Default")){
                    imgOption=0;
                }else if(displayImgValue.equals("Most Recent First")){
                    imgOption=1;
                }else if(displayImgValue.equals("Most Recent Last")){
                    imgOption=2;
                }
                
                //If user entered something to query and a time period constraint
                if(!(request.getParameter("query").equals("")) && dateFlag==1){
                    //If the user selected "default"
                    if(imgOption==0){
                        doSearch = conn.prepareStatement("SELECT * FROM (SELECT 6*SCORE(1) + 3*SCORE(2) + SCORE(3) AS RANK, subject, description, place, timing, photo_id FROM images i WHERE CONTAINS(i.subject, ?, 1)>0 OR CONTAINS(i.place, ?, 2)>0 OR CONTAINS(i.description, ?, 3)>0) WHERE (timing between ? and ?)  ORDER BY RANK DESC");
                    }
                    //If the user selected "most recent first"
                    else if(imgOption==1){
                        doSearch = conn.prepareStatement("SELECT * FROM (SELECT 6*SCORE(1) + 3*SCORE(2) + SCORE(3) AS RANK, subject, description, place, timing, photo_id FROM images i WHERE CONTAINS(i.subject, ?, 1)>0 OR CONTAINS(i.place, ?, 2)>0 OR CONTAINS(i.description, ?, 3)>0) WHERE (timing between ? and ?)  ORDER BY TIMING DESC");
                    }
                    //If the user selected "most recent last"
                    else if(imgOption==2){
                        doSearch = conn.prepareStatement("SELECT * FROM (SELECT 6*SCORE(1) + 3*SCORE(2) + SCORE(3) AS RANK, subject, description, place, timing, photo_id FROM images i WHERE CONTAINS(i.subject, ?, 1)>0 OR CONTAINS(i.place, ?, 2)>0 OR CONTAINS(i.description, ?, 3)>0) WHERE (timing between ? and ?)  ORDER BY TIMING ASC");
                    }
                    
                    doSearch.setString(1, request.getParameter("query"));
                    doSearch.setString(2, request.getParameter("query"));
                    doSearch.setString(3, request.getParameter("query"));
                    doSearch.setString(4, from);
                    doSearch.setString(5, to);
                    
                    check=1;
                }
                //If the user entered something to query but not a time period constraint
                else if(!(request.getParameter("query").equals("")) && dateFlag==0){
                    //If the user selected "default"
                    if(imgOption==0){
                        doSearch = conn.prepareStatement("SELECT * FROM (SELECT 6*SCORE(1) + 3*SCORE(2) + SCORE(3) AS RANK, subject, description, place, timing, photo_id FROM images i WHERE CONTAINS(i.subject, ?, 1)>0 OR CONTAINS(i.place, ?, 2)>0 OR CONTAINS(i.description, ?, 3)>0)  ORDER BY RANK DESC");
                    }
                    //If the user selected "most recent first"
                    else if(imgOption==1){
                        doSearch = conn.prepareStatement("SELECT * FROM (SELECT 6*SCORE(1) + 3*SCORE(2) + SCORE(3) AS RANK, subject, description, place, timing, photo_id FROM images i WHERE CONTAINS(i.subject, ?, 1)>0 OR CONTAINS(i.place, ?, 2)>0 OR CONTAINS(i.description, ?, 3)>0)  ORDER BY TIMING DESC");
                    }
                    //If the user selected "most recent last"
                    else if(imgOption==2){
                        doSearch = conn.prepareStatement("SELECT * FROM (SELECT 6*SCORE(1) + 3*SCORE(2) + SCORE(3) AS RANK, subject, description, place, timing, photo_id FROM images i WHERE CONTAINS(i.subject, ?, 1)>0 OR CONTAINS(i.place, ?, 2)>0 OR CONTAINS(i.description, ?, 3)>0)  ORDER BY TIMING ASC");
                    }
                    
                    doSearch.setString(1, request.getParameter("query"));
                    doSearch.setString(2, request.getParameter("query"));
                    doSearch.setString(3, request.getParameter("query"));
                    
                    check=1;
                }
                //If the user did not enter anything to query but entered a time period constraint
                else if((request.getParameter("query").equals("")) && dateFlag==1){
                    //If the user selected "default", in this case because there is no query, this will automatically be rank from date ascending
                    if(imgOption==0){
                        doSearch = conn.prepareStatement("SELECT permitted, subject,description,place,timing,photo_id FROM images i where i.timing BETWEEN ? AND ? ORDER BY TIMING ASC");
                    }
                    //If the user selected "most recent first"
                    else if(imgOption==1){
                        doSearch = conn.prepareStatement("SELECT permitted, subject,description,place,timing,photo_id FROM images i where i.timing BETWEEN ? AND ? ORDER BY TIMING DESC");
                    }
                    //If the user selected "most recent last"
                    else if(imgOption==2){
                        doSearch = conn.prepareStatement("SELECT permitted, subject,description,place,timing,photo_id FROM images i where i.timing BETWEEN ? AND ? ORDER BY TIMING ASC");
                    }
                    
                    doSearch.setString(1, from);
                    doSearch.setString(2, to);
                    
                    check =1;
                }
                //If the user did not enter any query and time period constraint
                else{
                    out.println("Please enter either a query and/or time period");
                    check =0;
                }
                
                //If the user did enter a query and/or time period constraint
                if(check==1){
                    ResultSet rset2 = doSearch.executeQuery();
                    
                    //Get all pictures based on search query and/or time period constraint
                    while(rset2.next()){
                        is_friend=false;
                        pic_id = (rset2.getObject(6)).toString();
                        
                        //Determine if you're the owner
                        ResultSet rset3 = stmt1.executeQuery("select owner_name, permitted from images where photo_id="+pic_id);
                        if(rset3.next()){
                            owner_name = rset3.getString(1);
                            permitted = rset3.getInt(2);
                        }
                        
                        //Determine if you're the friend
                        ResultSet rset4 = stmt2.executeQuery("select friend_id from group_lists where group_id="+permitted);
                        while (rset4.next()) {
                            if (rset4.getString(1).equals(username)){
                                is_friend = true;
                            }
                        }
                        //Determine if you're the owner of the group
                        ResultSet rset5 = stmt2.executeQuery("select user_name from groups where group_id=" + permitted);
                        if (rset5.next()) {
                            group_owner = rset5.getString(1);
                            if (group_owner == null)
                                //Happens if the group is public or private
                                group_owner = "";
                        }
                        
                        if (owner_name.equals(username) || permitted == 1 || 
                            username.equals("admin") || is_friend ||
                            group_owner.equals(username)) {
                            valid_ids.add(pic_id);
                        }
                    }
                    stmt1.close();
                    stmt2.close();
                    
                    for(String p_id : valid_ids){  
                        //Encode display.jsp link
                        String encodeDisplay1 = response.encodeURL("/proj1/display/displayImage.jsp");
                        String encodeDisplay2 = encodeDisplay1+"?id="+p_id;
                        out.println("<a href='"+encodeDisplay2+"'>");
                        
                        //Encode the servlet GetOnePic
                        String encodeOne1 = response.encodeURL("/proj1/display/GetOnePic");
                        String encodeOne2 = encodeOne1+"?"+p_id;
                        out.println("<img src='"+encodeOne2+"'></a>");
                    }
                    out.println("</center>");
                }
            }
        }catch(SQLException e){
            out.println("SQLException: " +
            e.getMessage());
            conn.rollback();
        }
    %>
    </div>
</body>
</html>
