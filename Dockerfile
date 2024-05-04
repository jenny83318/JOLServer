
FROM gradle:jdk11-jammy AS builder
COPY . .
RUN gradle build

FROM eclipse-temurin:11-jdk-jammy as runtime
EXPOSE 8080
COPY --from=builder /build/libs/JOLServer-1.0.3-plain.war jolserver.war
ENTRYPOINT ["java", "-jar", "/jolserver.war"]