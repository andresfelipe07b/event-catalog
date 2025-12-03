# --- STAGE 1: BUILD THE APPLICATION ---
FROM maven:3.9.5-eclipse-temurin-21 AS build

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn package -DskipTests

# --- STAGE 2: RUN THE APPLICATION ---
FROM eclipse-temurin:21-jre AS runtime

WORKDIR /app

COPY --from=build /app/target/gateway-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]