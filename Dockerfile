# Use official OpenJDK 17 runtime as a parent image
FROM eclipse-temurin:17-jdk-jammy

# Set environment variables for Cloud Run
ENV JAVA_OPTS=""

# Set the working directory inside the container
WORKDIR /app

# Copy the built jar file into the container
COPY target/glow-apex-admin-1.0.0.jar app.jar

# Expose the port that Cloud Run will use
EXPOSE 8080

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/app.jar"]