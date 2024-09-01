# Use a base image with OpenJDK
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the target directory into the container
COPY target/visitorproject-0.0.1-SNAPSHOT.jar app.jar

# Specify the entry point to run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
