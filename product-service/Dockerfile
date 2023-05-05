FROM openjdk:17
WORKDIR /app
COPY ./target/*.jar ./product-service.jar
ENTRYPOINT [ "java", "-jar", "product-service.jar" ]
EXPOSE 8083
