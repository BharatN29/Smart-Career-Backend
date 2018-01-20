Setting up Development Environment

Requirements:

	1. JDK 1.8 or higher (http://www.oracle.com/technetwork/java/javase/downloads/).
	2. Node server v8.1.2 or higher (https://nodejs.org/).
	3. Tomcat v7.0 or higher (https://tomcat.apache.org/download-70.cgi/).
	4. Eclipse or any other Java EE IDE with Spring Boot Plugin (https://www.eclipse.org/downloads/).
	5. MySql 5.7.18 or higher (https://dev.mysql.com/downloads/mysql/).

Steps: 
	
	1. Install the required applications as described above.
	2. Set Database environment using the file "chatbot-DDL.sql".
	3. Navigate to "Backend/Chatbot/src/main/resources/application.properties" and update database connection details.
	4. Import the backend project to the IDE from the "Backend/Chatbot" directory.
	5. Build and Run the backend project.
	6. Open the file "Chatbot/proxy.confg.json" and update the backend server host and port details.
	7. Run the "frontend.bat" file.


	