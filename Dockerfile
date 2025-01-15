# Stage 1: Build the application using an OpenJDK base image with Gradle
FROM gradle:7.6.0-jdk17 AS build

# Set the working directory
WORKDIR /workspace/app

# Copy Gradle configuration and source code
COPY build.gradle settings.gradle gradlew ./
COPY gradle gradle
COPY src src

# Build the application
RUN ./gradlew clean build -x test

# Stage 2: Create the runtime image
FROM openjdk:17-jdk-slim
VOLUME /tmp
COPY --from=build /workspace/app/build/libs/TenantApp-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
