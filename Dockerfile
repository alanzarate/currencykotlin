# changing from this :
# https://www.docker.com/blog/9-tips-for-containerizing-your-spring-boot-code/

#FROM eclipse-temurin:17-jdk-alpine
#WORKDIR /app

#COPY .mvn/ .mvn
#COPY mvnw pom.xml ./
#RUN chmod +x mvnw
#RUN ./mvnw dependency:go-offline

#COPY src ./src

#ENV DB_PASSWORD=$DB_PASSWORD
#ENV DB_USERNAME=$DB_USERNAME
#ENV DB_URL=$DB_URL
#ENV API_KEY=$API_KEY

#CMD ["./mvnw", "spring-boot:run"]

# OTRA APROXIMACION ES LA DE LA MISMA PAGINA ANTERIOR

#FROM eclipse-temurin:17-jdk-alpine as builder
#WORKDIR /opt/app
#ENV DB_PASSWORD=$DB_PASSWORD
#ENV DB_USERNAME=$DB_USERNAME
#ENV DB_URL=$DB_URL
#ENV API_KEY=$API_KEY
#COPY .mvn/ .mvn
#COPY mvnw pom.xml ./
#RUN chmod +x mvnw

#COPY src ./src
#RUN ./mvnw clean install

#FROM eclipse-temurin:17-jdk-alpine
#WORKDIR /opt/app
#COPY --from=builder /opt/app/target/*.jar /opt/app/*.jar
#ENV DB_PASSWORD=$DB_PASSWORD
#ENV DB_USERNAME=$DB_USERNAME
#ENV DB_URL=$DB_URL
#ENV API_KEY=$API_KEY

#ENTRYPOINT ["java", "-jar", "/opt/app/*.jar"]

# APROXIMACION DE UN BETTER DOCKER FILE


# tercera aproximacion de docker file

#FROM eclipse-temurin:17-jdk-alpine as build
#WORKDIR /workspace/app

#COPY mvnw .
#COPY .mvn .mvn
#COPY pom.xml .
#RUN chmod +x mvnw
#COPY src src

#RUN ./mvnw install -DskipTests
#RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

#FROM eclipse-temurin:17-jdk-alpine
#RUN ./mvnw install -DskipTests

#WORKDIR /tmp
#ARG DEPENDENCY=/workspace/app/target/dependency
#COPY --from=build ${DEPENDENCY}/BOOT-INF/lib /app/lib
#COPY --from=build ${DEPENDENCY}/META-INF /app/META-INF
#COPY --from=build ${DEPENDENCY}/BOOT-INF/classes /app
#ENV DB_PASSWORD=$DB_PASSWORD
#ENV DB_USERNAME=$DB_USERNAME
#ENV DB_URL=$DB_URL
##ENV API_KEY=$API_KEY
#ENTRYPOINT ["java", "-cp","app:app/lib/*", "com.lan.bo.currencykotlin.CurrencykotlinApplicationKt.class"]


# APROXIMACION MAS SIMPLE

FROM eclipse-temurin:17-jdk-alpine
ARG JAR_FILE=target/currencykotlin-0.0.1-SNAPSHOT.jar

WORKDIR /opt/app
ENV DB_PASSWORD=$DB_PASSWORD
ENV DB_USERNAME=$DB_USERNAME
ENV DB_URL=$DB_URL
ENV API_KEY=$API_KEY

COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]