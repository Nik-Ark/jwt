# MAVEN BUILD:
FROM maven:3.9.2-eclipse-temurin-17 AS builder
COPY pom.xml /app/
COPY src /app/src
RUN --mount=type=cache,target=/root/.m2 mvn -f /app/pom.xml clean package -DskipTests

# RUN:
FROM eclipse-temurin:17-jre-jammy
COPY --from=builder /app/target/jwt-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
