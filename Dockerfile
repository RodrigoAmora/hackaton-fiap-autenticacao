# Primeira etapa: Build
FROM maven:3.8.5-openjdk-17 as builder

WORKDIR /build

COPY pom.xml .
COPY src/ ./src/
COPY .mvn .mvn
COPY mvnw .

RUN chmod +x ./mvnw
RUN mvn dependency:go-offline -B
RUN mvn package -DskipTests

# Executa o build
RUN ./mvnw clean package -DskipTests

# Segunda etapa: Runtime
FROM eclipse-temurin:17-jre-focal

RUN mkdir /app
WORKDIR /app

COPY --from=builder /build/target/*.jar app.jar

# Configurações do MongoDB e da aplicação
ENV SERVER_PORT=8080
ENV SPRING_DATA_MONGODB_URI=mongodb://mongo:27017/fiap_auteticacao
ENV JWT_SECRET=aeiou

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]