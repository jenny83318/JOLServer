
FROM ubuntu:latest AS build
RUN apt-get update
RUN apt-get install -y openjdk-11-jdk

FROM openjdk:11-jdk
EXPOSE 8080
COPY target/JOLServer-1.0.3-plain.war jolserver.war
ENTRYPOINT ["java","-jar","/jolserver.war"]