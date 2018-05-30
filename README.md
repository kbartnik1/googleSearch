# Prerequisites
*  Docker installed
*  Maven installed and added to system's PATH
*  (Recommended) IntelliJ Community Edition IDE

##### Running the application
*  Before running the application build it using maven.  
   *  To do so, execute following command in project's root directory.  
       *  ```mvn clean install -U```  
*  After building, it is time to execute the application.
   *  To do so, make sure you are in project's root folder and execute following command.  
      *  ```java -jar target/output_jar_name.jar```

##### Check the logs
*  Logs are located in /logs folder. They are timestamped, so it should be easy for you to find desired one !   