FROM gradle:8-jdk21 AS build
WORKDIR /app
COPY . .
RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:21-jdk-alpine
WORKDIR /email

COPY --from=build /app/build/libs/*[!plain].jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
