FROM openjdk:18-jdk-alpine
EXPOSE 8080
ADD build/libs/springAuthorization-0.0.1-SNAPSHOT.jar app.jar
CMD ["java", "-jar", "app.jar"]
