
# 設置容器內的工作目錄。這是所有後續命令的基礎目錄。
WORKDIR /home/gradle/project

# 複製當前目錄下的所有文件到容器內的工作目錄中。
COPY . .

# 執行 Gradle 構建命令，生成 WAR 檔案。
# 使用 --no-daemon 參數避免在容器中啟動後台進程。
RUN gradle build --no-daemon

# 使用 Eclipse Temurin 映像作為運行環境
FROM eclipse-temurin:11-jdk-jammy as runtime

# 設定容器內應用將要使用的端口號
EXPOSE 8080

# 從構建階段複製生成的 WAR 檔案到運行階段的容器內
COPY --from=builder /home/gradle/project/build/libs/JOLServer-1.0.3-plain.war /jolserver.war

# 設定容器啟動時執行的命令，使用 Java 啟動 WAR 檔案。
ENTRYPOINT ["java", "-jar", "/jolserver.war"]