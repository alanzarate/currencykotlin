

FROM eclipse-temurin:11-jdk-alpine as build
WORKDIR /workspace/app
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
RUN chmod +x mvnw
COPY src src

RUN ./mvnw install -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

FROM eclipse-temurin:11-jdk-alpine


VOLUME /tmp
ARG DEPENDENCY=/workspace/app/target/dependency
COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
ENV DB_PASSWORD=$DB_PASSWORD
ENV DB_USERNAME=$DB_USERNAME
ENV DB_URL=$DB_URL
ENV API_KEY=$API_KEY
ENV URL_KEYCLOAK=$URL_KEYCLOAK
ENTRYPOINT ["java", "-cp","app:app/lib/*", "com.lan.bo.currencykotlin.CurrencykotlinApplicationKt"]


# APROXIMACION MAS SIMPLE

#FROM eclipse-temurin:17-jdk-alpine
#ARG JAR_FILE=target/currencykotlin-0.0.1-SNAPSHOT.jar

#WORKDIR /opt/app
#ENV DB_PASSWORD=$DB_PASSWORD
#ENV DB_USERNAME=$DB_USERNAME
#ENV DB_URL=$DB_URL
#ENV API_KEY=$API_KEY

#COPY ${JAR_FILE} app.jar
#ENTRYPOINT ["java", "-jar", "app.jar"]