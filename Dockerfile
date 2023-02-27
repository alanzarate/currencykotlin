FROM openjdk:17-jdk-slim
COPY target/*.jar app.jar

ENV USERNAME "postgres"
ENV PASSWORD "mysecretpassword"
ENV URL "jdbc:postgresql://localhost:5432/recordsCurrencyKotlin"

ENTRYPOINT ["java","-jar","/app.jar"]