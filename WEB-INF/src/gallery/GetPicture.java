/**
 * 
 */
package gallery;

import java.io.*;
import java.util.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;
import oracle.jdbc.driver.*;
import java.text.*;
import java.net.*;

/**
 *  @author  Michael Morris
 *  Credits: Li-Yan Yuan, http://luscar.cs.ualberta.ca:8080/yuan/GetOnePic.java
 *  
 *  This servlet sends one picture stored in the table below to the client 
 *  who requested the servlet.
 *
 *   picture( photo_id: integer, title: varchar, place: varchar, 
 *            sm_image: blob,   image: blob )
 *
 *  The request must come with a query string as follows:
 *    GetPicture?12:        sends the picture in sm_image with photo_id = 12
 *    GetPicture?big12:     sends the picture in image  with photo_id = 12
 *
 */
public class GetPicture extends HttpServlet implements SingleThreadModel {
    
    /**
     *    This method first gets the query string indicating PHOTO_ID,
     *    and then executes the query:
     *          select image from yuan.photos where photo_id = PHOTO_ID   
     *    Finally, it sends the picture to the client
     */
    public void doGet(HttpServletRequest request, HttpServletResponse res) throws ServletException, IOException {

      	//  send out the HTML file
      	res.setContentType("text/html");
      	PrintWriter out = res.getWriter();
      	
        Connection conn = null;
        
      	out.println("<html>");
      	out.println("<head>");
      	out.println("<title> Image List </title>");
      	out.println("</head>");
      	out.println("<body bgcolor=\"#000000\" text=\"#cccccc\" >");
      	out.println("<center>");
      	out.println("<h3>Most Popular Images</h3>");
        
      	/*
      	 *   to execute the given query
      	 */
      	try {
            String query = "select photo_id from pictures";
            
            conn = DBHandler.getInstance().getConnection();
            Statement stmt = conn.createStatement();
            ResultSet rset = stmt.executeQuery(query);
            String p_id = "";
            
            while( rset.next() ) {
                p_id = (rset.getObject(1)).toString();
                
                // specify the servlet for the image
                out.println("<a href=\"/yuan/servlet/GetOnePic?big"+p_id+"\">");
                // specify the servlet for the thumbnail
                out.println("<img src=\"/yuan/servlet/GetOnePic?"+p_id + "\"></a>");
            }
            
            stmt.close();
            
            DBHandler.getInstance().safeCloseConn(conn);
            
        } catch(Exception ex){ out.println(ex.toString()); }
            
        out.println("</body>");
        out.println("</html>");
    }
    
}

import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import java.sql.*;

public class GetOnePic extends HttpServlet 
    implements SingleThreadModel {


    public void doGet(HttpServletRequest request,
		      HttpServletResponse response)
	throws ServletException, IOException {
	
	//  construct the query  from the client's QueryString
	String picid  = request.getQueryString();
	String query;

	if ( picid.startsWith("big") )  
	    query = 
	     "select image from pictures where photo_id=" + picid.substring(3);
	else
	    query = "select sm_image from pictures where photo_id=" + picid;

	ServletOutputStream out = response.getOutputStream();

	/*
	 *   to execute the given query
	 */
	Connection conn = null;
	try {
	    conn = getConnected();
	    Statement stmt = conn.createStatement();
	    ResultSet rset = stmt.executeQuery(query);

	    if ( rset.next() ) {
		response.setContentType("image/gif");
		InputStream input = rset.getBinaryStream(1);	    
		int imageByte;
		while((imageByte = input.read()) != -1) {
		    out.write(imageByte);
		}
		input.close();
	    } 
	    else 
		out.println("no picture available");
	} catch( Exception ex ) {
	    out.println(ex.getMessage() );
	}
	// to close the connection
	finally {
	    try {
		conn.close();
	    } catch ( SQLException ex) {
		out.println( ex.getMessage() );
	    }
	}
    }


}
