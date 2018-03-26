cd server
mvn exec:java -Dexec.mainClass="controller.MultiServer"
cd ..
cd client
mvn exec:java -Dexec.mainClass="RomanClient"
pause