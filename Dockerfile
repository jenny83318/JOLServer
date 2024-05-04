# 使用具有 Java Development Kit 和 Gradle 的基础映像
FROM gradle:jdk11-jammy AS builder

# 设置容器内的工作目录。这是所有后续命令的基础目录。
WORKDIR /home/gradle/project

# 复制当前目录下的所有文件到容器内的工作目录中。
COPY . .

# 执行 Gradle 构建命令，生成 WAR 文件。
# 使用 --no-daemon 参数避免在容器中启动后台进程。
RUN gradle build --no-daemon

# 使用 Tomcat 官方镜像作为基础镜像
FROM tomcat:9-jdk11-openjdk-slim AS tomcat

# 删除默认的 Tomcat Web 应用，以防止与您的应用冲突
RUN rm -rf /usr/local/tomcat/webapps/*

# 从构建阶段复制生成的 WAR 文件到 Tomcat 的 webapps 目录，并命名为 jolserver.war
COPY --from=builder /home/gradle/project/build/libs/JOLServer-1.1.1-plain.war /usr/local/tomcat/webapps/jolserver.war

# 暴露 Tomcat 容器的 8080 端口
EXPOSE 8080

# 設定 Tomcat 容器启动时执行的命令
CMD ["catalina.sh", "run"]
