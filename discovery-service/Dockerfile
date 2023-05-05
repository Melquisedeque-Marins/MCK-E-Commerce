FROM openjdk:17
WORKDIR /app
ENV APP_PROFILE=docker
COPY ./target/*.jar ./discovery-server.jar
ENTRYPOINT [ "java", "-jar", "discovery-server.jar" ]
EXPOSE 8761


