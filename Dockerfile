# Use a lightweight JDK runtime
FROM eclipse-temurin:21-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the JAR to the image
COPY target/*.jar app.jar

# Expose your app port
EXPOSE 6000

# Start the app
ENTRYPOINT ["java", "-jar", "/app/app.jar"]