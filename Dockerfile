FROM eclipse-temurin:17-jdk-focal

WORKDIR /app

# Copia os arquivos do projeto
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src ./src

# Configura permissões e executa build
RUN chmod +x mvnw
RUN ./mvnw package -DskipTests

# Configura a aplicação
FROM eclipse-temurin:17-jre-focal
WORKDIR /app
COPY --from=0 /app/target/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
