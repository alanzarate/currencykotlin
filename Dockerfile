FROM openjdk:17-jdk-slim
COPY target/*.jar app.jar



ENV DB_USERNAME ${DB_USERNAME}
ENV DB_PASSWORD ${DB_PASSWORD}
ENV DB_URL ${DB_URL}
ENV API_KEY ${API_KEY}


ENTRYPOINT ["java","-jar","/app.jar"]