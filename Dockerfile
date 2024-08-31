# Step 1: Use an official OpenJDK runtime as a parent image
FROM openjdk:17-jdk-alpine

# Step 2: Set the working directory in the container
WORKDIR /app

# Step 3: Copy the JAR file from the host machine to the container
COPY backend/build/libs/backend-0.0.1-SNAPSHOT.jar /app/spring-boot-react-app.jar

# Step 4: Define a volume for persistent data storage
VOLUME /app/data

# Step 5: Expose the port that the application will run on
EXPOSE 8080

# Step 6: Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "/app/spring-boot-react-app.jar"]

# Instructions to build and run the Docker image:

# Step 7: Build the Docker image
# In the terminal, navigate to the directory containing this Dockerfile and run:
# docker build -t spring-boot-react-app .

# Step 8: Run the Docker container with a volume
# After the image is built, run the container with the following command:
# docker run -p 8080:8080 -v /path/on/host:/app/data spring-boot-react-app

# The application will be accessible at http://localhost:8080
