FROM openjdk:17
WORKDIR /app
COPY ./target/*.jar ./review-service.jar
ENTRYPOINT [ "java", "-jar", "review-service.jar" ]
EXPOSE 8084
