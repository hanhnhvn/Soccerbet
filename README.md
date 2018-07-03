# Soccerbet - simple bet app for World Cup 2018 using available jsons at:
# https://worldcup.sfg.io/matches
# https://worldcup.sfg.io/teams

1. git clone https://github.com/hanhnhvn/Soccerbet
2. set-up a blank oracle database schema
3. set-up tomcat web server (e.g Tomcat 7/JDK 7)
4. update database configuration in src\main\resources\META-INF\spring\database.properties
5. update bootstrap=true for the first time of running
6. mvn install -DskipTests
7. deploy the war file to the tomcat web server
