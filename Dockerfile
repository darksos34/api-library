# dockerfile
FROM eclipse-temurin:25-jre

WORKDIR /api-library

# set desired runtime port (can be overridden at runtime)
ENV SERVER_PORT=8081

# copy prebuilt jar from CI build step
COPY target/*.jar app.jar

EXPOSE 8081

# pass the server.port explicitly so the app binds to the expected container port
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${SERVER_PORT}"]
