FROM eclipse-temurin:21-jdk-alpine

WORKDIR /email

COPY /build/libs/email-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java","-jar","app.jar"]


