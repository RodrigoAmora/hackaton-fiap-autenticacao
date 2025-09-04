# Primeira etapa: Build
FROM eclipse-temurin:17-jdk-focal AS builder

WORKDIR /build

# Copia os arquivos do projeto
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN mkdir ./src
COPY src ./src/

# Dá permissão de execução ao mvnw
RUN chmod +x ./mvnw

# Executa o build
RUN ./mvnw clean package -DskipTests

# Segunda etapa: Runtime
FROM eclipse-temurin:17-jre-focal

WORKDIR /app

COPY --from=builder /build/target/*.jar app.jar

# Configurações do MongoDB e da aplicação
ENV SERVER_PORT=8080
ENV SPRING_DATA_MONGODB_URI=mongodb://mongo:27017/fiap_auteticacao
ENV JWT_SECRET=aeiou

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]