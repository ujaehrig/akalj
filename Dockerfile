FROM eclipse-temurin:17-alpine
VOLUME /tmp
COPY target/*.jar app.jar
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "/app.jar"]
