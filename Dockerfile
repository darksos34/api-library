FROM eclipse-temurin:25-jre-resolute

WORKDIR /api-library

COPY target/*.jar app.jar

ENV SERVER_PORT=8081

EXPOSE 8081

ENTRYPOINT ["sh","-c","java -jar app.jar --server.port=${SERVER_PORT}"]