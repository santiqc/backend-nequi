FROM openjdk:17

WORKDIR /app

COPY target/nequi-0.0.1-SNAPSHOT.jar .

EXPOSE 8080

CMD ["java", "-jar", "nequi-0.0.1-SNAPSHOT.jar"]