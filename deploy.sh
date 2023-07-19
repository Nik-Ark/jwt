#!/bin/bash

echo "Building Big Fat JAR..."
mvn -f ./pom.xml clean package -DskipTests

echo "Creating Tar Archive Made Of Big Fat JAR..."
mv ./target/spring-jwt.jar .
tar -cvzf sjwt.tar.gz spring-jwt.jar

echo "Sending Tar Archive on Oracle Cloud..."
scp sjwt.tar.gz oracle:/var/www/s_app_dir/

echo "Removing Tar Archive and Big Fat JAR..."
rm -rf sjwt.tar.gz spring-jwt.jar
