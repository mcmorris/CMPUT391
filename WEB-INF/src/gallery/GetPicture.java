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

        Connection conn = null;

		//  construct the query  from the client's QueryString
		String picId = request.getQueryString();
		String query;

		if(picId.startsWith("big"))  
			query = "select image from pictures where photo_id=" + picid.substring(3);
		else
			query = "select sm_image from pictures where photo_id=" + picid;
		
		ServletOutputStream out = response.getOutputStream();
	
      	/*
      	 *   to execute the given query
      	 */
      	try {
      		conn = DBHandler.getInstance().getConnection();
			Statement stmt = conn.createStatement();
			ResultSet rset = stmt.executeQuery(query);

			if( rset.next() ) {
				response.setContentType("image/gif");
				InputStream input = rset.getBinaryStream(1);	    
				int imageByte;
				while((imageByte = input.read()) != -1) {
				    out.write(imageByte);
				}
				input.close();
			} 
			else {
				out.println("no picture available");
			}
			
            stmt.close();
            
            DBHandler.getInstance().safeCloseConn(conn);
            
        } 
        catch(Exception ex) { 
        	out.println(ex.toString()); 
        }
		
    }
    
}
