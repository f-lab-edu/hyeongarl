#syntax=docker/dockerfile:1

# Base image
FROM bellsoft/liberica-openjdk-alpine:17

# Set the working directory
WORKDIR /app

# Copy the build.gradle and gradle folder
COPY api/build.gradle ./
COPY gradlew ./gradlew
COPY gradle ./gradle
COPY settings.gradle ./

COPY api/src ./src

# Build the application
RUN ./gradlew clean build --no-daemon

ARG JAR_FILE=/api/build/libs/*.jar

COPY ${JAR_FILE} /app/app.jar

# Expose the port the app runs on
EXPOSE 8080

# Run the application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
