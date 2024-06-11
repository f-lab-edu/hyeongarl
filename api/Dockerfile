#syntax=docker/dockerfile:1

# Base image
FROM openjdk:17-jdk-alpine as builder

# Set the working directory
WORKDIR /app

# Copy Gradle Wrapper
COPY gradlew ./gradlew
COPY gradle ./gradle

# Copy project files
COPY settings.gradle ./
COPY api/build.gradle ./
COPY api/src ./src

# Build the application
RUN ./gradlew clean build

FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY --from=builder /app/build/libs/*.jar app.jar

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
