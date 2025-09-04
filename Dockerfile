
FROM eclipse-temurin:17-jdk-focal as builder

# Define o diretório de trabalho
WORKDIR /build

# Copia os arquivos do projeto
COPY . .

# Configura permissões e executa build
RUN chmod +x mvnw && ./mvnw package -DskipTests

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