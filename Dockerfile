# Define a imagem base
FROM openjdk:17-oracle

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
RUN mkdir /src
COPY src /src

# Configura permissões e executa build
RUN mvn dependency:go-offline -B
RUN mvn package -DskipTests
RUN mkdir -p target/dependency && (cd target/dependency; jar -xf ../*.jar)

# Define o comando de inicialização do seu projeto
CMD ["java", "-jar", "/app/FiapAutenticacao.jar"]

# Expõe a porta do seu projeto
EXPOSE 8080