# Primeira etapa: Build
FROM maven:3.9.5-eclipse-temurin-17-focal AS builder

WORKDIR /build

# Copia o pom.xml primeiro para aproveitar o cache das dependências
COPY pom.xml ./
RUN mvn dependency:go-offline
# Depois copie o resto do código
COPY src ./src/


# Executa o build com mais detalhes de log
RUN mvn clean package -DskipTests -X

# Segunda etapa: Runtime
FROM eclipse-temurin:17-jre-focal

WORKDIR /app

COPY --from=builder /build/target/*.jar app.jar

ENV SPRING_DATA_MONGODB_URI=mongodb://mongodb:27017/fiap_auteticacao
ENV SERVER_PORT=8080

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
