# custom class path
custclass = -classpath ../lib/ojdbc6.jar:../lib/servlet-api.jar:../lib/commons-fileupload-1.3.1.jar:../lib/commons-io-2.4.jar:../lib/thumbnailator-0.4.8.jar
# define a custom source path to use
custsource = -sourcepath ./:util 


all:
	make analysis
	make audio
	make image
	make login
	make register
	make scalar
	make search
	make sensor
	make subscription
	make updateInfo
	make UpdatePass
analysis:
	javac $(custsource) $(custclass) AnalysisServlet.java	
audio:
	javac $(custsource) $(custclass) AudioServlet.java	
image:
	javac $(custsource) $(custclass) ImageServlet.java
login:
	javac $(custsource) $(custclass) LoginServlet.java	
register:
	javac $(custsource) $(custclass) RegServlet.java	
scalar:
	javac $(custsource) $(custclass) ScalarServlet.java	
search:
	javac $(custsource) $(custclass) SearchServlet.java	
sensor:
	javac $(custsource) $(custclass) SensorServlet.java	
subscription:
	javac $(custsource) $(custclass) SubscriptionServlet.java	
updateInfo:
	javac $(custsource) $(custclass) UpdateInfoServlet.java	
UpdatePass:
	javac $(custsource) $(custclass) UpdatePassServlet.java	
short:
	javac $(custsource) $(custclass) *.java
	
clean:
	rm -rf *./.class
	rm -rf ./util/*.class
