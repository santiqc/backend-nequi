FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/nequi-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} app_nequi.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app_nequi.jar"]