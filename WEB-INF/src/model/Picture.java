/**
 * 
 */
package model;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import session.CredentialHandler;
import session.DBHandler;

/**
 * @author mcmorris
 *
 */

public class Picture {
	final String tempDirectory = "/tmp/";
	final int maxRequestSize = 2000000000;
	final int maxMemorySize  = 10000000;
	
	// Upload one or more images to database.
  	public void upload() {
		
		// Create a factory for disk-based file items
		DiskFileItemFactory factory = new DiskFileItemFactory();
		
		// Set factory constraints
		factory.setSizeThreshold(maxMemorySize);
		factory.setRepository(new File(tempDirectory));
		
		// Create a new file upload handler
		ServletFileUpload upload = new ServletFileUpload(factory);
		upload.setSizeMax(maxRequestSize);
		
		// Process the uploaded items
		Iterator iter = items.iterator();
		FileItem fileItem;
		File fout;
		
		while (iter.hasNext()) {
			UPLOAD FILE
			
			// Add uploaded image to DB
			add(conn, request, item);
			
			// File added to DB, delete from temp file folder.
			fileItem.delete();
		} // while
  	}
  
  	// Add a group to groups.
  	public void add(Connection conn, HttpRequest request, FileItem item) {
		if (conn == null) return;
		
		String ownerName = request.getParameter("owner");
		int permitted = request.getParameter("permitted");
		String subject = request.getParameter("subject");
		String place = request.getParameter("place");
		Date timing = request.getParameter("timing");
		String description = request.getParameter("description");
		
		//Get the image stream
		InputStream instream = item.getInputStream();
		
		BufferedImage img = ImageIO.read(instream);
		BufferedImage thumbNail = shrink(img, 10);
		
		// Oracle is the stupid, and requires all blobs to be inserted as empty objects during add.
		PreparedStatement pstmt = conn.prepareStatement("insert into photos values(0, ?, ?, ?, ?, ?, ?, empty_blob(), empty_blob())");
		pstmt.setString(1, owner_name);
		pstmt.setInt(2, permitted);
		pstmt.setString(3, subject);
		pstmt.setString(4, place);
		pstmt.setDate(5, timing);
		pstmt.setString(6, description);
		pstmt.executeUpdate();
		
		// Record inserted, now deal with thumbnail.
		// Note: to retrieve the lob_locator that you must use "FOR UPDATE" in the select statement
		// It would have been nicer to retrieve the new id from the result of pstmt.executeUpdate().  Oh well.
		PreparedStatement pstmt2 = conn.prepareStatement("SELECT images_seq.CURRVAL FROM dual;");
		ResultSet results = pstmt2.executeQuery(cmd);
		results.next();
		BLOB myPic = ((OracleResultSet)results).getBLOB(7);
		
		//Write the image to the blob object
		OutputStream outstream = myPic.getBinaryOutputStream();
		ImageIO.write(thumbNail, "jpg", outstream);
		
		instream.close();
	    outstream.close();
	    
	    // FIXME: I feel like BufferedImage img hasn't been written to DB.  What's up with that?
	    
		// For batch updates, client is not going to be able to set subject, place, timing, description.  Set to defaults.
	}
  
  // Get a group from groups.
  public ResultSet get(Connection conn, int groupId) {
    ResultSet results = null;
    
    if (conn != null) {
      PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM groups WHERE group_id = ?;");
      pstmt.setInt(1, groupId);
      
      results = pstmt.executeQuery();
    }
    
    return results;
  }
  
    // Get a group from groups.
  public ResultSet getByName(Connection conn, String name) {
    ResultSet results = null;
    
    if (conn != null) {
      PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM groups WHERE group_name = ?;");
      pstmt.setString(1, name);
      
      results = pstmt.executeQuery();
    }
    
    return results;
  }
  
  // Get owner groups from groups.
  public ResultSet getGroups(Connection conn, String curUser) {
    ResultSet results = null;
    
    if (conn != null) {
      PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM groups WHERE user_name = ?;");
      pstmt.setString(1, curUser);
      
      results = pstmt.executeQuery();
    }
    
    return results;
	}
  
	//shrink image by a factor of n, and return the shrinked image
	public static BufferedImage shrink(BufferedImage image, int n) {
		
		int w = image.getWidth() / n;
		int h = image.getHeight() / n;
		
		BufferedImage shrunkImage = new BufferedImage(w, h, image.getType());
		
		for (int y=0; y < h; ++y) {
			for (int x=0; x < w; ++x) {
				shrunkImage.setRGB(x, y, image.getRGB(x*n, y*n));
			}
		}
        
        return shrunkImage;
    }
}
