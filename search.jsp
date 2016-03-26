<!-- Module which displays the search form that the user can find pictures with -->
<!DOCTYPE html>
<html>
<head>
    <title>Search</title>
    <link rel="stylesheet" type="text/css" href="/proj1/util/mystyle.css">
    <!--Got this code from "http://jqueryui.com/datepicker/#date-range" -->    
    <link rel="stylesheet" href="http://code.jquery.com/ui/1.10.3/themes/smoothness/jquery-ui.css" />
    <script src="http://code.jquery.com/jquery-1.9.1.js"></script>
    <script src="http://code.jquery.com/ui/1.10.3/jquery-ui.js"></script>
    <link rel="stylesheet" href="/resources/demos/style.css" />
    <script>
        $(function() {
            $( "#from" ).datepicker({
                defaultDate: "+1w",
                changeMonth: true,
                numberOfMonths: 1,
                onClose: function( selectedDate ) {
                    $( "#to" ).datepicker( "option", "minDate", selectedDate );
                }
            });
            $( "#to" ).datepicker({
                defaultDate: "+1w",
                changeMonth: true,
                numberOfMonths: 1,
                onClose: function( selectedDate ) {
                    $( "#from" ).datepicker( "option", "maxDate", selectedDate );
                }
            });
        });
    </script>
</head>
<body> 
<%@include file="../util/addHeader.jsp"%>
    <div id="container">
       <p class='homePage'>Go back to <A class='homePage' href='<%= encodeHomePage %>'>Home Page</a></p>
       <% String encodeSearch = response.encodeURL("/proj1/search/searchResult.jsp");%>
       <div id= 'searching'>
       <form action=<%=encodeSearch%>>  
           <Fieldset>
               <legend>Search</legend>
               <TABLE>
                   <TR VALIGN=TOP ALIGN=LEFT>          
                       <TD><B>Search:</B></TD>
                       <TD><INPUT TYPE="text" NAME="query" MAXLENGTH="24"></TD>
                   </TR>
                   <TR VALIGN=TOP ALIGN=LEFT>
                       <TD><B>From:</B></TD>
                       <TD><input type="text" id="from" name="from"/></TD>
                   </TR>
                   <TR VALIGN=TOP ALIGN=LEFT>
                       <TD><B>To:</B></TD>
                       <TD><input type="text" id="to" name="to"/></TD>
                   </TR>
                   <TR VALIGN=TOP ALIGN=LEFT>
                       <TD><p>&nbsp;</p></TD>
                   </TR>
                   <TR VALIGN=TOP ALIGN=LEFT>
                       <TD>Display Image Option</TD>
                   </TR>
                   <TR VALIGN=TOP ALIGN=LEFT>
                       <TD><B>Most recent first :</B></TD>
                       <TD><input type="radio" value="Most Recent First" name="rank"/></TD>
                   </TR>
                   <TR VALIGN=TOP ALIGN=LEFT>
                       <TD><B>Most recent last :</B></TD>
                       <TD><input type="radio" value="Most Recent Last" name="rank"/></TD>
                   </TR>
                   <TR VALIGN=TOP ALIGN=LEFT>
                       <TD><B>Default :</B></TD>
                       <TD><input type="radio" value="Default" name="rank" checked/></TD>
                   </TR>
                </TABLE>
                <input type="submit" id="buttonstyle" name="dateSubmit" value="Search">
            </Fieldset>
        </form>
        </div>
    </div>
</body>
</html>
