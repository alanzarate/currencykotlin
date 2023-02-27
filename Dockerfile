FROM openjdk:17-jdk-slim
COPY target/*.jar app.jar

ENV USERNAME "postgres"
ENV PASSWORD "mypass"
ENV URL "jdbc:postgresql://172.17.0.1:5432/currency"

ENTRYPOINT ["java","-jar","/app.jar"]