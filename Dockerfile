# Define a imagem base
FROM eclipse-temurin:17-jdk-focal

LABEL maintainer="rodrigo.amora.freitas@gmail.com"
LABEL version="1.0.7"
LABEL name="Rodrigo Amora"

# Define as variáveis APP_NAME e VERSION (corrigido o nome da variável APP_NAME)
ENV APP_NAME=FiapAutenticacao
ENV VERSION=1.0

# Copia o arquivo JAR do seu projeto para dentro do container
COPY ./target/${APP_NAME}-${VERSION}.jar  /app/${APP_NAME}.jar

# Define o diretório de trabalho primeiro
WORKDIR /app

# Copia os arquivos necessários para build
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Configura permissões e executa build
RUN chmod +x mvnw
RUN ./mvnw package -DskipTests

# Configura a aplicação
FROM eclipse-temurin:17-jre-focal
WORKDIR /app
COPY --from=0 /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
