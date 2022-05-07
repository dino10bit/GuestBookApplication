Option 1 : Eclipse
-----------------------------------------------------
1. Import the source code(zip) to the eclipse.
2. Add maven plugin to eclipse if not present.
3. Go to Run Configurations...
4. Select maven build in the list of items and click new launch configuration.
5. In the main tab, for the Base directory field, select the workspace option and select the Project "GuestBook".
6. In the goals field, enter "clean install".
7. Point to the custom maven settings.xml location if any in the User Settings field. Click Apply and Click on Run.
8. Maven should automatically download the dependencies and the build should be successful.(Eclipse should point to JDK 1.8 instead of JRE for the build to be successful).
9. Once the build is successful, open GuestBookApplication class and run as java application. The application should start at port 8080 by default and can see the logs in the console window of eclipse.
10.Open http://localhost:8080/ in the browser to see the login page.
11. Login as guest with username as guest and password as password. Proceed to create an entry.
12. Login as admin with username as admin and password as password. Proceed to perform different actions.


Option 2 : CMD(command prompt)
-----------------------------------------------------
1. Copy the Source code folder to a location of choice.
2. Open the command prompt.
3. Set the environment variables for maven and jdk either in command prompt or in the system user environment variables in control panel.
4. cd to the root location of the project. e.g cd D:\GuestBook
5. Enter "mvn clean install" and click enter. The build should be successful.
6. Enter "mvn spring-boot:run" and click enter. The application should be started successfully.
7.Open http://localhost:8080/ in the browser to see the login page.
8. Login as guest with username as guest and password as password. Proceed to create an entry.
9. Login as admin with username as admin and password as password. Proceed to perform different actions.