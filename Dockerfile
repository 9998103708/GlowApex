# -------------------------------------------------
# Stage 1: Build the JAR
# -------------------------------------------------
FROM maven:3.9.2-eclipse-temurin-17 AS build

WORKDIR /app

# Copy project files
COPY pom.xml .
COPY src ./src

# Build JAR (skip tests for faster build)
RUN mvn clean package -DskipTests

# -------------------------------------------------
# Stage 2: Run the app
# -------------------------------------------------
FROM eclipse-temurin:17-jdk-jammy

WORKDIR /app

# Copy the JAR from build stage
COPY --from=build /app/target/glow-apex-admin-1.0.0.jar ./app.jar

# Expose port
EXPOSE 8080

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]