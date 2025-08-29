# Use a lightweight JDK
FROM eclipse-temurin:17-jdk-jammy

# Set working directory
WORKDIR /app

# Copy the jar built by Maven/Gradle
COPY target/glow-apex-admin-1.0.0.jar app.jar

# Expose the port (optional, for documentation)
EXPOSE 8080

# Run the jar
ENTRYPOINT ["java","-jar","/app.jar"]
