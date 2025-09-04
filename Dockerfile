# Define a imagem base
FROM openjdk:17-oracle

# Copia o arquivo JAR do seu projeto para dentro do container
COPY ./target/FiapAutenticacao-1.0.jar  /app/app.jar

# Define o diretório de trabalho primeiro
WORKDIR /app

# Copia os arquivos necessários para build
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Configura permissões e executa build
RUN ./mvnw dependency:go-offline -B
RUN ./mvnw package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

# Define o comando de inicialização do seu projeto
CMD ["java", "-jar", "/app/app.jar"]

# Expõe a porta do seu projeto
EXPOSE 8080
