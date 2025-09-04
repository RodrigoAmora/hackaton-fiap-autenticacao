FROM maven:3.9-eclipse-temurin-17 as builder

WORKDIR /build

# Copia apenas o POM primeiro para aproveitar o cache das dependências
COPY pom.xml .
COPY .mvn .mvn
COPY mvnw .
COPY mvnw.cmd .

# Downloads das dependências
RUN mvn dependency:go-offline

# Agora copia o código fonte
COPY src src

# Executa o build
RUN mvn package -DskipTests

# Segunda etapa - imagem final
FROM eclipse-temurin:17-jre-focal

WORKDIR /app

# Copia apenas o JAR gerado
COPY --from=builder /build/target/*.jar app.jar

# Configuração das variáveis de ambiente
ENV SPRING_DATA_MONGODB_URI=mongodb://mongodb:27017/fiap_auteticacao
ENV SERVER_PORT=8080

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
