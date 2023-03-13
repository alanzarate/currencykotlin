FROM openjdk:17-jdk-slim
COPY target/*.jar app.jar



ENV USERNAME "postgres"
ENV PASSWORD "mypass"
ENV URL "jdbc:postgresql://192.168.10.10:8080/currency"
ENV API_KEY ${API_KEY}


ENTRYPOINT ["java","-jar","/app.jar"]