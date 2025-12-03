# --- STAGE 1: BUILD THE APPLICATION ---
# Usamos una imagen base con JDK 21 y Maven para compilar la aplicación.
FROM maven:3.9.5-eclipse-temurin-21 AS build

# Establecemos el directorio de trabajo dentro del contenedor.
WORKDIR /app

# Copiamos el archivo pom.xml para que Maven pueda descargar las dependencias.
# Esto permite que Docker cachee esta capa si el pom.xml no cambia.
COPY pom.xml .

# Descargamos las dependencias de Maven.
# El comando 'dependency:go-offline' descarga todas las dependencias sin compilar el código.
# Esto ayuda a que la capa de descarga de dependencias se cachee eficientemente.
RUN mvn dependency:go-offline

# Copiamos el resto del código fuente de la aplicación.
COPY src ./src

# Compilamos la aplicación y generamos el JAR ejecutable.
RUN mvn package -DskipTests

# --- STAGE 2: RUN THE APPLICATION ---
# Usamos una imagen base con JRE 21 (más ligera) para ejecutar la aplicación.
FROM eclipse-temurin:21-jre AS runtime

# Establecemos el directorio de trabajo.
WORKDIR /app

# Copiamos el JAR compilado desde la etapa 'build'.
# El nombre del JAR se obtiene del pom.xml (artifactId-version.jar).
# Asegúrate de que el artifactId y la version en tu pom.xml coincidan con el nombre del JAR.
# En tu caso, es 'event-catalog-0.0.1-SNAPSHOT.jar'
COPY --from=build /app/target/event-catalog-0.0.1-SNAPSHOT.jar app.jar

# Exponemos el puerto en el que la aplicación Spring Boot escuchará.
# Tu aplicación usa el puerto 9000, así que lo exponemos.
EXPOSE 9000

# Definimos variables de entorno para la aplicación.
# Estas pueden ser sobrescritas por docker-compose o al ejecutar el contenedor.
ENV SPRING_PROFILES_ACTIVE=docker
ENV SPRING_DATASOURCE_URL=jdbc:postgresql://db:5432/eventcatalog_db
ENV SPRING_DATASOURCE_USERNAME=user
ENV SPRING_DATASOURCE_PASSWORD=password
ENV JWT_SECRET=your-super-secret-key-that-is-long-enough-for-hs256
ENV JWT_EXPIRATION=86400000

# Comando para ejecutar la aplicación cuando el contenedor se inicie.
# 'java -jar app.jar' ejecuta el JAR.
ENTRYPOINT ["java", "-jar", "app.jar"]