
FROM maven:3.8.4-openjdk-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests


FROM openjdk:17
WORKDIR /app
COPY --from=builder /app/target/nequi-0.0.1-SNAPSHOT.jar .
EXPOSE 8080
CMD ["java", "-jar", "nequi-0.0.1-SNAPSHOT.jar"]