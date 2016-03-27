/**
 * 
 */
package session;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.io.File;
import java.io.FileInputStream;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;

/**
 * @author wang8
 *
 */
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// get request parameters for userID and password
	 	String owner_name = request.getParameter("OWNERNAME");
	 	String photodate = request.getParameter("DATE");
	 	String location = request.getParameter("LOCATION");
	 	String subject = request.getParameter("SUBJECT");
	  	String description = request.getParameter("DESCRIPTION");
		String url= request.getParameter("DESCRIPTION");
		int pic_id ; 



		PrintWriter out = response.getWriter();
		Connection conn = null;
		
		try {
			if (photodate == ""){
				photodate = "SYSDATE";
			}
			 //Parse the HTTP request to get the image stream
	   		 DiskFileUpload fu = new DiskFileUpload();
	    	 List FileItems = fu.parseRequest(request);
	    
	    	 // Process the uploaded items, assuming only 1 image file uploaded
	    	 Iterator i = FileItems.iterator();
	    	 FileItem item = (FileItem) i.next();
	    	 while (i.hasNext() && item.isFormField()) {
		     	item = (FileItem) i.next();
			}

 			//Get the image stream
	    	InputStream instream = item.getInputStream();

			//establish connection to the underlying database
			conn = DBHandler.getInstance().getConnection();
			String user = CredentialHandler.getInstance().getSessionUserName(request, response);
		

			if(valid) {
				conn.setAutoCommit(false);
				File image = new File("D:\\a.gif");
    			FileInputStream   fis = new FileInputStream(image);
    			stmt.setBinaryStream(3, fis, (int) image.length());
    			stmt.execute();
				String sqlImage = "INSERT INTO images values(0, '" + owner_name + "', '" + permitted + "' , '" + subject + "', '" + location + "', '" + photodate + "','" + description + "','" + "empty_blob(), empty_blob()");
				//String sqlPersons = "INSERT INTO images values('" + photo_id + "', '" + owner_name + "', '" +  + "', '" + address + "', '" + email + "', '" + phone + "');";
			//Write the image to the blob object
			OutputStream outstream = myblob.getBinaryOutputStream();
			int size = myblob.getBufferSize();
			byte[] buffer = new byte[size];
			int length = -1;
			while ((length = instream.read(buffer)) != -1)
			outstream.write(buffer, 0, length);
			instream.close();
			outstream.close();

		    stmt.executeUpdate("commit");
			response_message = " Upload OK!  ";
		    conn.close();
            //sends the statement to the database server
            int row = pstmtSave.executeUpdate();
            if (row > 0) {
                message = "File uploaded and saved into database";
            }
				Statement state = conn.createStatement();
				state.executeUpdate(sqlImage);
				conn.commit();
				
			        RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.html"); //TODO: ATTACH CORRECT HTML
			        out.println("<font color=green>Your photo has been processed.</font>");
			        rd.include(request, response);
			}
			
			DBHandler.getInstance().safeCloseConn(conn);

		} catch (SQLException sqle) {
			DBHandler.getInstance().safeCloseTrans(conn);
		} catch (Exception ex) {
			out.println("<hr>" + ex.getMessage() + "<hr>");
		}
		
	        if (valid == false) {
	            RequestDispatcher rd = getServletContext().getRequestDispatcher("/register.html");//TODO: ATTACH CORRECT HTML
	            out.println("<font color=red>Your entry cannot be fulfilled </font>");
	            rd.include(request, response);
	        }
	        
    	}
	
}
