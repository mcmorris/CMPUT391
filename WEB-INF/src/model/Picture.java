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
		
		BLOB myThumb = ((OracleResultSet)results).getBLOB(7);
		BLOB myPic = ((OracleResultSet)results).getBLOB(8);
		
		//Write the images to blob objects
		OutputStream outstream = myThumb.getBinaryOutputStream();
		ImageIO.write(thumbNail, "jpg", outstream);
		outstream.close();
		
		OutputStream outstream2 = myPic.getBinaryOutputStream();
		ImageIO.write(img, "jpg", outstream2);
		outstream2.close();
		
		instream.close();
		
		// File added to DB, delete from temp file folder.
		fileItem.delete();
		
		// For batch updates, client is not going to be able to set subject, place, timing, description.  Set to defaults.
	}
  
  	public ResultSet get(Connection conn, HttpRequest request, String picId, String mode) {
  		ResultSet results = null;
		if (conn == null) return;
		
		PreparedStatement pstmt = null;
		
		if (mode == "big") {
			pstmt = conn.prepareStatement("SELECT photo FROM images WHERE photo_id = ?;");	
		} else {
			pstmt = conn.prepareStatement("SELECT thumbnail FROM images WHERE photo_id = ?;");
		}
		
		pstmt.setInt(1, Integer.parse(picId));
		
		ServletOutputStream out = response.getOutputStream();
		results = pstmt.executeQuery();
		if (results.next()) {
			response.setContentType("image/gif");
			InputStream input = results.getBinaryStream(1);
			int imageByte;
			
			while((imageByte = input.read()) != -1) {
				out.write(imageByte);
			}
			
			input.close();
		} else {
			out.println("no picture available");
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
