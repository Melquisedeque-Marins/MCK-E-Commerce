FROM openjdk:17
WORKDIR /app
COPY ./target/*.jar ./api-gateway.jar
ENTRYPOINT [ "java", "-jar", "api-gateway.jar" ]
EXPOSE 8080
