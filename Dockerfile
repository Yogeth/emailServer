# Step 1: Build the application
FROM gradle:8-jdk21 AS build
WORKDIR /app
COPY . .
RUN chmod +x gradlew
RUN ./gradlew bootJar --no-daemon

# Step 2: Run the application
FROM eclipse-temurin:21-jdk-alpine
WORKDIR /email

# Copy the exact file we named in build.gradle
COPY --from=build /app/build/libs/email.jar /email/app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/email/app.jar"]
