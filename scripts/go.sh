#! /bin/sh
git pull origin master
mvn clean compile install
cd client
mvn dependency:copy-dependencies
ant devmode &
cd ../server-mock
mvn exec:java -Dexec.mainClass=com.biosimilarity.emeris.RunBiosimServer &