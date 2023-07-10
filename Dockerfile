
FROM ubuntu:latest AS build
RUN apt-get update
RUN apt-get install openjdk-11-jdk-y
COPY . .
RUN ./gradlew build

FROM openjdk:11-jdk-slim
EXPOSE 8080
COPY --from=build /build/libs/JOLServer-1.0.3-plain.war jolserver.war
ENTRYPOINT ["java","-war","/jolserver.war"]