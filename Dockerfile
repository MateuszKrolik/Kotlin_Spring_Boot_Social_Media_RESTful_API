FROM eclipse-temurin:21-alpine
RUN mkdir /opt/app
COPY build/libs/*.jar /opt/app/app.jar
CMD ["java", "-jar", "/opt/app/app.jar"]
