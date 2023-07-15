
FROM ubuntu:latest AS build
RUN apt-get update
RUN apt-get install -y openjdk-11-jdk
COPY . /build
WORKDIR . /build
RUN ./gradlew build

FROM openjdk:11-jdk
EXPOSE 8080
COPY --from=build /build/libs/JOLServer-1.0.3-plain.war jolserver.war
ENTRYPOINT ["java","-jar","/jolserver.war"]