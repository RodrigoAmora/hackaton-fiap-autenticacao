# Primeira etapa: Construir a aplicação
FROM maven:3.9.5-amazoncorretto-17 AS build

WORKDIR /workspace

# Copia todo o conteúdo do projeto
COPY . .
RUN mvn dependency:go-offline

# Copie o código fonte e construa o JAR
COPY src /src
RUN mvn clean package -DskipTests

# Segunda etapa: Rodar a aplicação
FROM amazoncorretto:17-alpine-jdk

LABEL maintainer="ricardo@ricardo.net"
LABEL version="1.0"
LABEL description="FIAP - Tech Chalenger"
LABEL name="Tech Chalenger"

EXPOSE 8080

# Copie o JAR da primeira etapa
COPY --from=build /workspace/target/FiapAutenticacao-1.0.jar app.jar

ENTRYPOINT ["java", "-jar", "/app.jar"]