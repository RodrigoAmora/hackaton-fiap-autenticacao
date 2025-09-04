# Primeira etapa: Build
FROM maven:3.8.5-openjdk-17 as builder

WORKDIR /app

# Copie o pom.xml e baixe as dependências, isso melhora o cache do Docker
COPY pom.xml .
RUN mvn dependency:go-offline

# Copie o código fonte e construa o JAR
RUN mkdir /src
COPY src ./src/
ARG MAVEN_SKIP_TEST=true
RUN if [ "$MAVEN_SKIP_TEST" = "true" ] ; then mvn clean package -DskipTests ; else mvn clean package ; fi


# Segunda etapa: Runtime
FROM eclipse-temurin:17-jre-focal

RUN mkdir /app
WORKDIR /app

COPY --from=builder /app/target/*.jar app.jar

# Configurações do MongoDB e da aplicação
ENV SERVER_PORT=8080
ENV SPRING_DATA_MONGODB_URI=mongodb://mongo:27017/fiap_auteticacao
ENV JWT_SECRET=aeiou

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]