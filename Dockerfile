# Stage 1: build
# Lấy từ docker hub một image là một hdh có sẵn đầy đủ từ java cho tới các tool cần thiết
# Đặt tên là build
FROM maven:3.9.8-amazoncorretto-21 AS build

# Tạo một thư mục và chuyển thư mục hiện tại (vừa mới kéo về) về /app
# Sau đó copy pom.xml và toàn bộ src vào /app (bấy nhiêu là đủ)
WORKDIR /app
COPY pom.xml .
COPY src ./src

# Chạy câu lệnh để build ứng dụng
RUN mvn package -DskipTests

# Stage 2: Tạo image
# Start with Amazon Correto JDK 21
FROM amazoncorretto:21.0.4

# Set working folder to App and copy complied file from above step
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]