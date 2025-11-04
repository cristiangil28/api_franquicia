# Etapa 1: construir el JAR
FROM eclipse-temurin:17-jdk AS builder
WORKDIR /app
COPY . .

# Dar permisos de ejecución al wrapper de Gradle
RUN chmod +x ./api-franchise/gradlew
RUN ./api-franchise/gradlew clean bootJar --no-daemon --stacktrace

# Etapa 2: imagen ligera de ejecución
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/build/libs/*.jar app.jar
EXPOSE 8001
ENTRYPOINT ["java","-jar","app.jar"]