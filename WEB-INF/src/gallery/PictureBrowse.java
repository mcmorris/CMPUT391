/**
 * 
 */
package session;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import oracle.jdbc.driver.*;
import java.text.*;
import java.net.*;

/**
 *  @author  Li-Yan Yuan
 *  Credits: Li-Yan Yuan, http://luscar.cs.ualberta.ca:8080/yuan/PictureBrowse.java
 */
public class PictureBrowse extends HttpServlet implements SingleThreadModel {
    
  /**
   *  Generate and then send an HTML file that displays all the thumbnail
   *  images of the photos.
   *
   *  Both the thumbnail and images will be generated using another 
   *  servlet, called GetOnePic, with the photo_id as its query string
   *
   */
  public void doGet(HttpServletRequest request, HttpServletResponse res) throws ServletException, IOException {

  	//  send out the HTML file
  	res.setContentType("text/html");
  	PrintWriter out = res.getWriter();
  	
    Connection conn = null;
    
  	out.println("<html>");
  	out.println("<head>");
  	out.println("<title> Photo List </title>");
  	out.println("</head>");
  	out.println("<body bgcolor=\"#000000\" text=\"#cccccc\" >");
  	out.println("<center>");
  	out.println("<h3>The List of Images </h3>");
    
  	/*
  	 *   to execute the given query
  	 */
  	try {
  	    String query = "select photo_id from pictures";
    
  	    conn = CredentialHandler.getInstance().getConnection();
  	    Statement stmt = conn.createStatement();
  	    ResultSet rset = stmt.executeQuery(query);
  	    String p_id = "";
    
  	    while (rset.next() ) {
  		    p_id = (rset.getObject(1)).toString();
    
  	       // specify the servlet for the image
          out.println("<a href=\"/yuan/servlet/GetOnePic?big"+p_id+"\">");
  	       // specify the servlet for the thumbnail
  	       out.println("<img src=\"/yuan/servlet/GetOnePic?"+p_id + "\"></a>");
    	  }
    	    
    	  stmt.close();
    	  conn.close();
    	} catch ( Exception ex ){ out.println( ex.toString() );}
      
    	out.println("</body>");
    	out.println("</html>");
    }
  
}
