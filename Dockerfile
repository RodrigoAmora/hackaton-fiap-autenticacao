FROM maven:3.8.5-openjdk-17 as builder
WORKDIR /app

COPY src/ ./src/
COPY .mvn .mvn
COPY mvnw pom.xml ./

RUN chmod +x ./mvnw
#RUN mvn dependency:go-offline -B
RUN mvn package -DskipTests

######

FROM eclipse-temurin:17-jdk as prod
RUN mkdir /app
COPY --from=builder /app/target/*.jar /app/app.jar
ENV SERVER_PORT=6060
WORKDIR /app
EXPOSE 6060
ENTRYPOINT ["java","-jar","/app/app.jar"]