FROM openjdk:17

WORKDIR /app

COPY sgdea-0.0.1-SNAPSHOT.jar .
EXPOSE 8080

CMD ["java", "-jar", "sgdea-0.0.1-SNAPSHOT.jar"]