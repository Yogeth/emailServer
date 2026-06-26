# Step 1: Use Gradle image to build the application
FROM gradle:8-jdk21 AS build
WORKDIR /app
COPY . .

# FIX: Grant execution permissions to the gradlew script
RUN chmod +x gradlew
RUN ./gradlew bootJar --no-daemon

# Step 2: Use lightweight runtime image to run it
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /email

# Copy the built jar from the previous build stage
COPY --from=build /app/build/libs/*[!plain].jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
