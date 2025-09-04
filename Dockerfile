# Primeira etapa: Build
FROM maven:3.9.5-eclipse-temurin-17-focal AS builder

WORKDIR /build

# Debug: Mostrar diretório atual
RUN pwd && ls -la

# Copia todo o conteúdo do projeto
COPY . .

# Debug: Mostrar conteúdo após cópia
RUN pwd && ls -la

# Executa o build
RUN mvn clean package -DskipTests

# Segunda etapa: Runtime
FROM eclipse-temurin:17-jre-focal

WORKDIR /app

COPY --from=builder /build/target/*.jar app.jar

ENV SPRING_DATA_MONGODB_URI=mongodb://mongodb:27017/fiap_auteticacao
ENV SERVER_PORT=8080

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]