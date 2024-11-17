# Use an official OpenJDK image as the base image
FROM openjdk:23-jdk-slim AS builder

# Set the working directory inside the container
WORKDIR /app

# Copy the jar file from the target directory to the container
COPY target/apassignment.jar app.jar

# Expose the port your application will run on
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
