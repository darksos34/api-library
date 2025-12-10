# dockerfile
FROM maven:3.9.8-eclipse-temurin-21 AS build

WORKDIR /api-library

COPY pom.xml .
COPY src ./src

RUN mvn -B -DskipTests package

FROM eclipse-temurin:21-jre

WORKDIR /api-library

# set desired runtime port (can be overridden at runtime)
ENV SERVER_PORT=8081

# copy built jar from build stage
COPY --from=build /api-library/target/*.jar app.jar

EXPOSE 8081

# pass the server.port explicitly so the app binds to the expected container port
ENTRYPOINT ["sh", "-c", "java -jar app.jar --server.port=${SERVER_PORT}"]
