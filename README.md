# CMPUT391
CMPUT 391 - Group Project - Online Image Sharing System

Online Image Sharing is a JSP HTML5 website that is connected to Oracle sql Database. This is connected through JDBC.The webserver is connected by TomCat. THe website stores user's images and allows targeted access to other users.


Login Module
The login module is contains two parts, the login section and registration section

In the login section the user is given inputs to input a username and password. Depending on what type the user is they are given certain areas of the website to view. For example the if the user is administrator then they are given the view to create and change users, but this view is not available to a general user.


Search Module
This module allows the user to search from the database for pictures depending on keyword. The first field takes the keyword search and the results are placed in an order of importance.

They can either input keyword or time periods.

Upload Module 
This module allows users to upload images and input information pertaining to them. The field that would be optional inputs are security specification, subject, place, and description. The photo's id is handles by an image sequence table in SQL. 

Display Module
The display module allows users to view their previous uploads through thumbnails. Upon clicking on one the image would be viewed at full magnification and the option for changing the security preferences and viewing group is present. 
data analysis module 

This modual allows only the admin to add delete sensors and also admin is the only one that can manage users. The admin can create/delete users and also to change the password for the user.

When the admin click "User and Sensor Management" a popup box will apear and 4 choices will be available :
This is a New User

This will bring admin to a page where there will be a table that have inputs for the admin to add new person. The phone number will check if you entered 10 digits number and the email will check for proper email. This will check if email is not in system.
This is an Existing User

This is adding a new user to an existing person. The admin will input an existing email and username and password. This will check if email exist and that the username does not conflict.
Manage User

This page will populate a list of users that can be deleted or updated. The admin will select a user by clicking the checkbox correspoding to that user. Selecting "Updating Password" will bring the user to a page where they can change their password. "Updating Personal" gives the option to edit the personal information for that user. "Delete" will just remove the user from the system and they will disapear from the list. Note the current administrator will not appear on the list; This is to protect from deleting all admin. If the admin wants to edit his or her info they can click "Account Settings" at the top of the page to change admin's own information.
Manage Sensors

This will bring the user to the sensor page. There will be a table to the left to allow the admin to create or delete sensors. Clicking "Delete/View sensors" will give a list of sensors and also the ability to delete sensors. The "New Sensor" will give the admin the ablility to add new sensors.
Subscribe Module

This module available only to the scientist allows the scientist to subscribe to different sensor datas. They do not subscribe to sensors directly. Once in the subsription page the scientist has two option add subscription and delete/view subscription. The add subscrition will list subscritions that are available to be subscribed. Once a data has been subscribbed to the data will be removed from view. Delete/view subscriptions lists the scientist's subscriptions and once the scientist unsubscibs the data will disappear. The scientist can click on the sensor page to see more detail.
Uploading Module

This modual is only for the data manager. He or she will click on the sensor page and to the left he will be able to see the different types of sensors that he/she can add. Although this page is available to the scientist and the admin the left table will change depending on who accesses that page.
Data Analysis Module

This module is accessed by scientists. He or she will click the drop-box in order to access the sensor_id values that they are subscribed to. Once you select the sensor_id, a table will be printed showing the yearly minimum, maximum and average of each year. Right below, another field will ask for what year you would like to do the data analysis on. Once the year is chosen, select to what time hierarchy you would like to perform the analysis on, choosing from either daily, weekly, or quarterly. Once you click submit, a table is produced below showing the Analysis of drilldown from year onto what time hierarchy was chose.
Installation

Step 1: Since this site uses TomCat please place the souce code in the webbapps folder in TomCat.

Step 2: Once this is done please makes sure you edit Db.java to your database information.

cmput391/WEB-INF/scr/util/Db.java

32    static final String USERNAME = "Username";
33    static final String PASSWORD = "*****";
34    // JDBC driver name and database URL
35    static final String DRIVER_NAME = "oracle.jdbc.driver.OracleDriver";
36    static final String DB_URL = "jdbc:oracle:thin:@gwynne.cs.ualberta.ca:1521:CRS";

Step 3: (Note this is optional) Edit your class path to include all of the following files:

oos-cmput391/WEB-INF/lib/servlet-api.jar
oos-cmput391/WEB-INF/lib/commons-fileupload-1.3.1.jar
oos-cmput391/WEB-INF/lib/commons-io-2.4.jar
oos-cmput391/WEB-INF/lib/ojdbc6.jar
oos-cmput391/WEB-INF/lib/thumbnailator-0.4.8.jar

Step 4: In the classe folder run the make file to compile all the java codes. The classes folder is located in:

oos-cmput391/WEB-INF/classes 

$ make all

Step 5: Start Tomcat

$ catalina start 
$ catalina stop

Note : You may need to run the server from the location catalina is installed and in that case you would run it. (to stop ctrl+C in the terminal where tomcat is and then call the shutdown)

$ ./catalina.sh start
$ ./catalina.sh stop

or

$ starttomcat

Note: Only available in the university of Alberta computer labs. (to stop ctrl+C in the terminal where tomcat is)

Step 6: Now you can go to web and view the site

http://[serverURL]/oos-cmput391/

Sources Used

Used the example code from class

