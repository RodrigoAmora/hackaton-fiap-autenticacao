# Etapa 1: Build
FROM maven:3.9.5-eclipse-temurin-17-focal AS builder

WORKDIR /build

# Copiar arquivos principais para cache
COPY . .

# Baixar dependências
RUN ./mvnw dependency:go-offline


# Build do projeto
RUN ./mvnw clean package -DskipTests

# Etapa 2: Runtime
FROM eclipse-temurin:17-jre-jammy

WORKDIR /app

# Copiar JAR final
COPY --from=builder /build/target/*1.0.jar app.jar

# Variáveis de ambiente
ENV SPRING_DATA_MONGODB_URI=mongodb://mongodb:27017/fiap_auteticacao
# A porta só é usada se configurada no Spring
ENV SERVER_PORT=8080

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
