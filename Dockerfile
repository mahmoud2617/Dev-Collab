FROM maven:3.9.11-eclipse-temurin-25 AS builder

WORKDIR /app

COPY pom.xml .
RUN mvn dependency:go-offline -B

COPY src ./src

RUN mvn clean package -DskipTests


FROM eclipse-temurin:25-jre

WORKDIR /app
COPY --from=builder /app/target/*.jar devCollab.jar

EXPOSE 8080

CMD ["java", "-jar", "devCollab.jar"]
