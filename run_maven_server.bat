cd server
mvn exec:java -Dexec.mainClass="controller.MultiServer"
cd ..
cd client
mvn exec:java -Dexec.mainClass="com.group52.client.actions.Main"
pause