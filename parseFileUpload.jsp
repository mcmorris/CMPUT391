<%@ page language="java" import="java.io.*, java.sql.*, java.util.*" %>
<%@ page import="org.apache.commons.fileupload.*, org.apache.commons.fileupload.disk.*, org.apache.commons.fileupload.servlet.*" %>
<%
  //Initialization for chunk management.
  boolean bLastChunk = false;
  int numChunk = 0;

  response.setContentType("text/plain");
  try {
    // Get URL Parameters.
    Enumeration paraNames = request.getParameterNames();
    out.println(" ------------------------------ ");
    String pname;
    String pvalue;
    while(paraNames.hasMoreElements()) {
      pname = (String)paraNames.nextElement();
      pvalue = request.getParameter(pname);
      out.println(pname + " = " + pvalue);
      if(pname.equals("jufinal")) {
        bLastChunk = pvalue.equals("1");
      } else if(pname.equals("jupart")) {
      	numChunk = Integer.parseInt(pvalue);
      }
    }
    out.println(" ------------------------------ ");
    
    // Directory to store all the uploaded files
    String tempDirectory = "/tmp/";
    int maxRequestSize = 2000000000;
    int maxMemorySize  = 10000000;
    
  ///////////////////////////////////////////////////////////////////////////////////////////////////////
  //The code below is directly taken from the jakarta fileupload common classes
  //All informations, and download, available here : http://jakarta.apache.org/commons/fileupload/
  ///////////////////////////////////////////////////////////////////////////////////////////////////////
  
  // Create a factory for disk-based file items
  DiskFileItemFactory factory = new DiskFileItemFactory();
  
  // Set factory constraints
  factory.setSizeThreshold(maxMemorySize);
  factory.setRepository(new File(tempDirectory));
  
  // Create a new file upload handler
  ServletFileUpload upload = new ServletFileUpload(factory);
  
  // Set overall request size constraint
  upload.setSizeMax(maxRequestSize);
  
  // Parse the request
  List /* FileItem */ items = upload.parseRequest(request);
  // Process the uploaded items
  Iterator iter = items.iterator();
  FileItem fileItem;
  File fout;
  
  Picture pic = new Picture();
  Connection conn = startUpload();
  
  out.println(" Let's read input files ...");
  while (iter.hasNext()) {
    fileItem = (FileItem) iter.next();
    
    if (!fileItem.isFormField()) {
      // Create a factory for disk-based file items
      DiskFileItemFactory factory = new DiskFileItemFactory();
      
      // Set factory constraints
      factory.setSizeThreshold(maxMemorySize);
      factory.setRepository(new File(tempDirectory));
      
      // Create a new file upload handler
      ServletFileUpload upload = new ServletFileUpload(factory);
      upload.setSizeMax(maxRequestSize);
      
      // Process the uploaded items
      List /* FileItem */ items = upload.parseRequest(request);
      Iterator iter = items.iterator();
      FileItem fileItem;
      File fout;
      
      while (iter.hasNext()) {
        // Form Fields are not images and should not be uploaded as such.
        if (!fileItem.isFormField()) {
          // http://jakarta.apache.org/commons/fileupload/
          // If we are in chunk mode, we add ".partN" at the end of the file, where N is the chunk number.
          String uploadedFilename = fileItem.getName() + ( numChunk > 0 ? ".part" + numChunk : "") ;
          fout = new File(ourTempDirectory + (new File(uploadedFilename)).getName());
          fileItem.write(fout);

          //////////////////////////////////////////////////////////////////////////////////////
          //Chunk management: if it was the last chunk, let's recover the complete file
          //by concatenating all chunk parts.
          //
          if (bLastChunk) {	        
            System.out.println(" Last chunk received: let's rebuild the complete file (" + fileItem.getName() + ")");
            // First: construct the final filename.
            FileInputStream fis;
            FileOutputStream fos = new FileOutputStream(tempDirectory + fileItem.getName());
            int nbBytes;
            byte[] byteBuff = new byte[1024];
            String filename;
            
            for (int i = 1; i <= numChunk; i += 1) {
              filename = fileItem.getName() + ".part" + i;
              System.out.println("  Concatenating " + filename);
              fis = new FileInputStream(tempDirectory + filename);
              while( (nbBytes = fis.read(byteBuff)) >= 0 ) {
                System.out.println("     Nb bytes read: " + nbBytes);
                fos.write(byteBuff, 0, nbBytes);
              }
              
              fis.close();
            }
            
            fos.close();
            
          // Chunked item received in full and put together, add to DB.
          pic.add(conn, request, fileItem);
        } 
        // End of chunk management
        //////////////////////////////////////////////////////////////////////////////////////
        else {
          // Add uploaded image to DB
          pic.add(conn, request, fileItem);
        }
      }
    } // while
  } catch(Exception e) {
    out.println("Exception e = " + e.toString());
  }
  
  pic.endUpload(conn);
  out.close();
%>
