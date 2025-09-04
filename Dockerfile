# Primeira etapa: Build
FROM maven:3.9.5-eclipse-temurin-17-focal AS builder

# Define o diretório de trabalho
WORKDIR /build

# Copia todo o conteúdo do projeto
COPY . .

# Executa o build usando mvn diretamente
RUN mvn clean package -DskipTests

# Segunda etapa: Runtime
FROM eclipse-temurin:17-jre-focal

WORKDIR /app

# Copia o JAR da etapa de build
COPY --from=builder /build/target/*.jar app.jar

# Variáveis de ambiente
ENV SPRING_DATA_MONGODB_URI=mongodb://mongodb:27017/fiap_auteticacao
ENV SERVER_PORT=8080

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]