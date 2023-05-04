FROM maven:3.8.6-openjdk-18 as build
WORKDIR /build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:17
WORKDIR /app
MAINTAINER MCK
ENV APP_PROFILE=docker
COPY --from=build ./build/target/*.jar ./discovery-server.jar
ENTRYPOINT [ "java", "-jar", "discovery-server.jar" ]
EXPOSE 8761
