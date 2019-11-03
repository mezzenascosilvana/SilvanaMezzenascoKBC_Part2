INSTALLATIONS:
 
Install Java (Preferably Eclipse).
Install jdk8
Install TestNG libraries.
Download Selenium drivers for Chrome and Mozilla.
Download the project from the URL provided.

CONFIGURATIONS:
 
Start Eclipse and import the project as a maven project.
Within the project locate the global properties file and configure the user key.

The approach used was:

ïCodification with JAVA using Eclipse, Maven, TestNG.
Once the project is imported, for all the dependencies to synchronize correctly you will need to update the Maven project
Within src/test /java: you will find the test case package with all the test classes made. 
Within the POMP file:  You will find all the dependencies that were considered necessary. 

How to execute the test cases?

If you have already imported the project, have TestNG installed and synchronized Maven, locate the file .xml within
 src/main/ resource. After that, right click on it and select execute with TestNG. This will trigger all the test suites created within the test package.
