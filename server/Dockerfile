# 使用官方 Maven 镜像作为基础镜像
FROM maven:3.8.4-openjdk-17-slim AS build
# 设置工作目录
WORKDIR /app
# jar 文件
COPY target/server-0.0.1-SNAPSHOT.jar /app/server.jar
# 运行
CMD ["java", "-jar", "/app/server.jar","--spring.profiles.active=prod"]