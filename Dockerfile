# Use a base image with OpenJDK
#Before running the build make sure to trigger clean - install.
FROM openjdk:17-jdk-alpine

# Set the working directory inside the container
WORKDIR /app

# Copy the JAR file from the target directory into the container
COPY target/visitorproject-*.jar app.jar

# Specify the entry point to run the JAR file
ENTRYPOINT ["java", "-jar", "app.jar"]
