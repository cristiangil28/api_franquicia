# Etapa 1: construir el JAR
FROM eclipse-temurin:17-jdk AS builder
WORKDIR /app
COPY . .

# Dar permisos de ejecución al wrapper de Gradle
RUN chmod +x ./api-franchise/gradlew
RUN ./api-franchise/gradlew -p ./api-franchise clean bootJar --no-daemon

# Etapa 2: imagen ligera de ejecución
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/api-franchise/build/libs/*.jar app.jar
EXPOSE 8001
ENTRYPOINT ["java","-jar","app.jar"]