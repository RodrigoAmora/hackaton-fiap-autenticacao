FROM maven:3.8.5-openjdk-17 as builder
WORKDIR /app

# Primeiro, copie apenas os arquivos necessários para resolver dependências
# Isso ajuda a cachear as dependências
COPY pom.xml ./
COPY .mvn/ .mvn/
COPY mvnw ./

# Dê permissão de execução ao mvnw
RUN chmod +x ./mvnw

# Download das dependências
RUN ./mvnw dependency:go-offline -B

# Agora copie o código fonte
COPY src/ ./src/

# Execute o build
RUN ./mvnw package -DskipTests

# Stage final
FROM eclipse-temurin:17-jdk-focal as prod
WORKDIR /app
COPY --from=builder /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
